package com.wolfpeng.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.wolfpeng.core.util.Utils.SockProto;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/11.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */

@Component("application") public class ProxyServer
        implements ServerReader.ServerReaderHandler, ServerListener.ServerListenerHandler {
    private static ApplicationContext applicationContext;
    private        SockProto          sockProto  = SockProto.Sock5;
    /**
     * ¼àÌý¶Ë¿Ú
     */
    private        int                listenPort = 9050;
    private        Socket             acceptSocket;
    /**
     * ¶ÁÈ¡Æ÷
     */
    private        ServerReader       reader;
    /**
     * ¼àÌýÆ÷
     */
    private        ServerListener     listener;

    public ProxyServer() throws IOException {
        reader = new ServerReader(this);
        listener = new ServerListener(listenPort, this);
    }

    void start() {
        listener.start();
    }

    @Override public boolean onAccept(SocketChannel socketChannel) throws ClosedChannelException {
        reader.addSession(new ProxySession(socketChannel));
        socketChannel.configureBlocking(true);

        return true;
    }

    @Override public boolean onReadable(Session session) {
        ProxySession proxySession = (ProxySession) session;
        proxySession.getClientSocketChannel().read()
        return false;
    }
}
