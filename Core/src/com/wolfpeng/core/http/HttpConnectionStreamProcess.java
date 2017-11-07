package com.wolfpeng.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;


/**
 * Created by penghao on 2017/5/12.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public class HttpConnectionStreamProcess {


    public static void process(final InputStream clientInputStram, final OutputStream clientOutputStream, final InputStream serverInputStram,
                               final OutputStream serverOutputStream, final HttpConnection connection, final HttpConnectionHandle httpConnectionHandle, final CountDownLatch latch) throws IOException {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HttpProxy.receive(clientInputStram, connection.getRequest());
                    HttpData fixedData = httpConnectionHandle != null ? httpConnectionHandle.processRequestData(connection) : null;
                    if (fixedData != null) {
                        connection.setRequest(fixedData);
                    }
                    HttpProxy.send(serverOutputStream, connection.getRequest());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HttpProxy.receive(serverInputStram, connection.getResponse());
                    HttpData fixedData = httpConnectionHandle != null ? httpConnectionHandle.processResponseData(connection) : null;

                    if (fixedData != null) {
                        connection.setResponse(fixedData);
                    }
                    HttpProxy.send(clientOutputStream, connection.getResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.start();
    }
}
