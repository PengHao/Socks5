package com.wolfpeng.server;

import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpConnectionHandle;
import com.wolfpeng.core.http.HttpConnectionStreamProcess;
import com.wolfpeng.core.util.HostPort;
import com.wolfpeng.core.util.Utils;
import com.wolfpeng.core.util.Utils.SockProto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * ����session
 * Created by penghao on 2017/1/19.
 */
public class ProxySession extends Session implements Runnable {
    /**
     * �����Channel
     */
    private SocketChannel remoteSocketChannel;
    /**
     * �Ƿ���socks4����
     */
    private SockProto     sockProto;
    private ByteBuffer    clientSocketBuffer;
    private ByteBuffer    remoteSocketBuffer;

    /**
     * channel ���캯��
     *
     * @param clientSocketChannel
     */
    public ProxySession(SocketChannel clientSocketChannel) {
        super(clientSocketChannel);
    }

    public void read() throws IOException {

    }

    public void run() {
        // ��ȡ��Դ�ĵ�ַ������־��ӡʹ��
        try {
            remoteSocket = sockProto.serverSocket(getClientSocketChannel(), null,
                    new Utils.FlowHostPortGetter() {
                        public HostPort get(HostPort hostPort) {
                            return hostPort;
                        }
                    });

            CountDownLatch latch = new CountDownLatch(2);
            HttpConnectionStreamProcess.process(clientSocket.getInputStream(),
                    clientSocket.getOutputStream(), remoteSocket.getInputStream(),
                    remoteSocket.getOutputStream(), httpConnection, httpConnectionHandle, latch);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeIo(remoteSocket);
            Utils.closeIo(clientSocket);
            Utils.log("Connection finished!");
        }
    }

}
