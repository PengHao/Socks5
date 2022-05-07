package com.wolfpeng.server;


import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * 代理session
 * Created by penghao on 2017/1/19.
 */
public class Session implements Closeable {
    /**
     * 客户端channel
     */
    private SocketChannel clientSocketChannel;

    /**
     * getter of clientSocketChannel
     */
    public SocketChannel getClientSocketChannel() {
        return clientSocketChannel;
    }

    /**
     * channel 构造函数
     * @param clientSocketChannel
     */
    public Session(SocketChannel clientSocketChannel) {
        this.clientSocketChannel = clientSocketChannel;
    }

    @Override public void close() throws IOException {
        if (Objects.nonNull(clientSocketChannel)) {
            clientSocketChannel.close();
        }
    }

}
