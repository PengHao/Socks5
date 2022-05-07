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
 * 代理session
 * Created by penghao on 2017/1/19.
 */
public class ProxySession extends Session implements Runnable {
    /**
     * 服务端Channel
     */
    private SocketChannel remoteSocketChannel;
    /**
     * 是否开启socks4代理
     */
    private SockProto     sockProto;
    private ByteBuffer    clientSocketBuffer;
    private ByteBuffer    remoteSocketBuffer;

    /**
     * channel 构造函数
     *
     * @param clientSocketChannel
     */
    public ProxySession(SocketChannel clientSocketChannel) {
        super(clientSocketChannel);
    }

    public void read() throws IOException {

    }

    public void run() {
        // 获取来源的地址用于日志打印使用
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
