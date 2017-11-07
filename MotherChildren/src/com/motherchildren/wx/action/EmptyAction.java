package com.motherchildren.wx.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.motherchildren.wx.dao.CardDao;
import com.motherchildren.wx.dao.DoctorDao;
import com.motherchildren.wx.dao.TaskDao;
import com.motherchildren.wx.dao.SessionDao;
import com.motherchildren.wx.moudle.Session;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpConnectionHandle;
import com.wolfpeng.core.http.HttpConnectionStreamProcess;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.http.HttpHead;
import com.wolfpeng.core.util.Utils;
import com.wolfpeng.server.ServerApplication;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/12.
 * Copyright ? 2017年 Alibaba. All rights reserved.
 */
@Component
public class EmptyAction implements Action {

    @Resource
    protected SessionDao sessionDao;

    @Resource
    protected TaskDao taskDao;

    @Resource
    protected CardDao cardDao;

    @Resource
    protected DoctorDao doctorDao;

    public static HttpData sendHttpData(String headerString)
    {
        return sendHttpData(headerString, null);
    }

    public static HttpData sendHttpData(String headerString, final byte[] body)
    {
        HttpConnection connection = null;
        try {
            final HttpHead head = new HttpHead(headerString);
            String host = head.getHeaderProperty("Host");
            Socket proxy_socket = new Socket(host, 80);

            OutputStream outputStream = proxy_socket.getOutputStream();
            InputStream inputStream = proxy_socket.getInputStream();

            connection = new HttpConnection();
            final byte[] header = headerString.getBytes();

            InputStream clientInputStream = new InputStream() {
                Integer headerIndex = 0;
                Integer bodyIndex = 0;
                @Override
                public int read() throws IOException {
                    if (header.length > headerIndex) {
                        int r = header[headerIndex];
                        headerIndex += 1;
                        return r;
                    } else if (body != null && body.length > bodyIndex){
                        int r = body[bodyIndex];
                        bodyIndex += 1;
                        return r;
                    } else {
                        return -1;
                    }
                }
            };

            OutputStream clientOutputStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {

                }
            };

            CountDownLatch latch = new CountDownLatch(2);
            HttpConnectionHandle httpConnectionHandle = (HttpConnectionHandle)ServerApplication.getBean(head.getHeaderProperty("Host"));
            HttpConnectionStreamProcess.process(clientInputStream, clientOutputStream, inputStream, outputStream, connection, httpConnectionHandle, latch);
            latch.await();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("发送GET请求出现异常！"+e);
            e.printStackTrace();
        }
        finally
        {
            return connection.getResponse();
        }
    }

    static String delete(String resource ,String begin, String end) {
        String[] splitStrings = resource.split(begin);
        if (splitStrings.length == 1) {
            return splitStrings[0];
        }
        Integer subEndIndex = splitStrings[1].indexOf(end);
        String subString = splitStrings[1].substring(subEndIndex+end.length());
        return splitStrings[0] + subString;
    }

    public HttpData processRequest(HttpConnection connection) {

        return null;
    }

    protected static String getDecodeBodyString(HttpData message) {
        String contentType = message.getHeaderFeild(HttpHead.CONTENT_TYPE);
        String textEncoding = null;
        if (contentType.contains("text")||contentType.contains("json")) {
            String[] subStrings = contentType.split(";");
            for (int i = 0; i < subStrings.length; ++i) {
                String subString = subStrings[i];
                if (subString.contains("charset")) {
                    String[] splitStrings = subString.split("=");
                    if (splitStrings.length > 1) {
                        textEncoding = splitStrings[1];
                    }
                }
            }
        }
        String contentEncoding = message.getHeaderFeild(HttpHead.CONTENT_ENCODING);
        byte[] bodyBytes = message.decodeChunkedBody().toByteArray();
        String body = "";
        if ("gzip".equals(contentEncoding)) {
            if ("utf-8".contains(textEncoding)) {
                textEncoding = "utf8";
            }
            body = Utils.uncompressGzipToString(bodyBytes, textEncoding);
        }
        return body;
    }

    protected static String phpSessionKey = "PHPSESSID";

    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    protected String get15DayLaterString() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 14);
        date=calendar.getTime();
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    protected Date getDate(String dateString) {
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Session getSession(HttpConnection connection) {
        String cookieString = getSessionString(connection);
        String phpSession = HttpHead.getCookie(cookieString, phpSessionKey);
        Session session = sessionDao.getSession(phpSession);
        return session;
    }

    public String getSessionString(HttpConnection connection) {
        HttpData message = connection.getRequest();
        String cookieString = message.getHeaderFeild(HttpHead.COOKIE);
        return cookieString;
    }


    public HttpData processResponse(HttpConnection connection) throws Exception{
        return null;
    }


    static String getFullHeader(String headerTemplate, Session session) {
        String fullHeader = headerTemplate;
        String cookies = session.getCookies();

        Map<String, String> cookiesMap = HttpHead.getCookieMap(cookies);
        for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
            fullHeader = fullHeader.replace(entry.getKey() + "=%s", entry.getKey() + "=" + entry.getValue());
        }
        return fullHeader;
    }
}
