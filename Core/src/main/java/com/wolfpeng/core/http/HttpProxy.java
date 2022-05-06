package com.wolfpeng.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.wolfpeng.core.util.Utils;

/**
 * Created by penghao on 2017/5/8.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */


public class HttpProxy {

    static String endOfHeader = "\r\n\r\n";

    public static boolean receive(InputStream in, HttpData data) {

        ByteArrayOutputStream cache = null;
        long contentLength = 0;

        long tempContentLength = 0;
        int chunkedLen = 0;
        int chunkedLenBeginPosition = 0;
        int position = 0;
        boolean isReadingChunk = false;
        boolean isFinished = false;

        try {
            cache = data.headerCache;
            byte[] bytes = new byte[1];
            while (!isFinished && (in.read(bytes)) > 0) {
                cache.write(bytes);
                if ( cache == data.headerCache && cache.toString().contains(endOfHeader)) {
                    String[] strs = cache.toString().split(endOfHeader);
                    String headerString = strs[0];
                    data.httpHead = new HttpHead(headerString);
                    String transferStr = data.httpHead.getHeaderProperty(HttpHead.TRANSFER_ENCODING);
                    if (0 == contentLength) {
                        if (transferStr != null && transferStr.equals("chunked")) {
                            data.isBodyChunked = true;
                        } else {
                            String str = data.httpHead.getHeaderProperty(HttpHead.CONTENT_LENGTH);
                            contentLength = str == null ? 0 : Long.parseLong(str);
                            tempContentLength = contentLength;
                        }
                    }

                    cache = data.bodyCache;
                    position = 0;
                    chunkedLenBeginPosition = position;
                }

                if (cache == data.bodyCache) {
                    if (tempContentLength > 0) {
                        tempContentLength -=1;
                    } else if (data.isBodyChunked) {
                        //read chunked
                        byte[] tempBytes = cache.toByteArray();

                        if (tempBytes.length > 2 &&
                            tempBytes[tempBytes.length -1] == '\n' &&
                            tempBytes[tempBytes.length -2] == '\r') {
                            if (chunkedLen == 0) {
                                if (isReadingChunk) {
                                    isReadingChunk = false;
                                    chunkedLenBeginPosition = position;
                                } else {
                                    int newLength = position - chunkedLenBeginPosition - 2;
                                    String s = new String(tempBytes, chunkedLenBeginPosition, newLength);
                                    chunkedLen = Integer.parseInt(s, 16);
                                    isReadingChunk = true;
                                    if (chunkedLen == 0) {
                                        cache.write('\r');
                                        cache.write('\n');
                                        isFinished = true;
                                    }
                                }
                            }
                        }
                        if (isReadingChunk) {
                            if (chunkedLen > 0) {
                                chunkedLen -= 1;
                            }
                        }
                    } else if (tempContentLength == 0) {
                        isFinished = true;
                    }
                }
                position += 1;
            }
        } catch (Exception e) {
            Utils.log("exception");
            return false;
        }
        return isFinished;
    }

    public static void send(OutputStream out,HttpData data) throws IOException {
        byte[] h = data.headerCache.toByteArray();
        byte[] b = data.bodyCache.toByteArray();
        out.write(h, 0, h.length);
        if (b.length > 0) {
            out.write(b, 0, b.length);
        }
        out.flush();
    }

}
