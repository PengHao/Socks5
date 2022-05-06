package com.wolfpeng.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by penghao on 2017/1/25.
 */

public class HttpData {
    public boolean isBodyChunked = false;

    public HttpHead httpHead = null;
    public ByteArrayOutputStream headerCache = new ByteArrayOutputStream();
    public ByteArrayOutputStream bodyCache = new ByteArrayOutputStream();

    public HttpData() {
    }

    public String getHeaderFeild(String feild) {
        return httpHead.getHeaderProperty(feild);
    }

    public byte[] encodeChunkedBody(byte[] bytes) {
        String length = Integer.toHexString(bytes.length)+"\r\n";
        ByteArrayOutputStream cache = new ByteArrayOutputStream();
        try {
            cache.write(length.getBytes());
            cache.write(bytes);
            cache.write("\r\n0\r\n\r\n".getBytes());
            byte[] rs = cache.toByteArray();
            cache.close();
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  ByteArrayOutputStream decodeChunkedBody() {
        if (isBodyChunked) {
            byte[] tempBytes = bodyCache.toByteArray();
            int length = tempBytes.length;
            ByteArrayOutputStream originBody = new ByteArrayOutputStream();

            int chunkedLenBeginPosition = 0;
            for (int i = 2; i < length; ++i) {
                if (tempBytes[i] == '\n' && tempBytes[i - 1] == '\r') {
                    String s = new String(tempBytes, chunkedLenBeginPosition, i-chunkedLenBeginPosition-1);
                    int chunkedLen = Integer.parseInt(s, 16);
                    if (chunkedLen == 0) {
                        return originBody;
                    }
                    originBody.write(tempBytes, i+1, chunkedLen);
                    i += chunkedLen;
                    i += 2;
                    chunkedLenBeginPosition = i+1;
                }
            }
            return originBody;
        } else {
            return bodyCache;
        }
    }

    public byte[] getOriginData() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(headerCache.toByteArray());
        stream.write(bodyCache.toByteArray());
        return stream.toByteArray();
    }


}
