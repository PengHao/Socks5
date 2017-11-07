package com.wolfpeng.server;

import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpConnectionHandle;
import com.wolfpeng.core.http.HttpConnectionStreamProcess;
import com.wolfpeng.core.util.Utils;
import com.wolfpeng.core.util.Utils.Authorization;
import com.wolfpeng.core.util.Utils.SockProto;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

/**
 * Created by penghao on 2017/1/19.
 */

public class Session implements Runnable {

    /**
     * ��Դ�Ĵ���socket
     */
    public Socket clientSocket;

    public Socket serverSocket;

    @Resource
    SocksUserDao userDao;

    /**
     * �Ƿ���socks4����
     */
    private SockProto sockProto;

    public String getHost() {
        return serverSocket.getInetAddress().getHostName();
    }

    public Integer getPort() {
        return serverSocket.getPort();
    }

    HttpConnection httpConnection;

    public Session(SockProto sockProto, Socket clientSocket) throws IOException {
        this.sockProto = sockProto;
        this.clientSocket = clientSocket;
    }

    public void run() {
        // ��ȡ��Դ�ĵ�ַ������־��ӡʹ��
        try {
            httpConnection = new HttpConnection();
            serverSocket = sockProto.serverSocket(clientSocket, null);
            httpConnection.init(getHost(), getPort());
            HttpConnectionHandle httpConnectionHandle = null;
            try {
                httpConnectionHandle = (HttpConnectionHandle)ServerApplication.getBean(
                    getHost());
            } catch (Exception e) {
                Utils.log("No Bean of Host %s", getHost());
                //e.printStackTrace();
            }
            CountDownLatch latch = new CountDownLatch(2);
            HttpConnectionStreamProcess.process(clientSocket.getInputStream(), clientSocket.getOutputStream(), serverSocket.getInputStream(), serverSocket.getOutputStream(), httpConnection, httpConnectionHandle, latch);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeIo(serverSocket);
            Utils.closeIo(clientSocket);
            Utils.log("Connection finished!");
        }
    }
}
