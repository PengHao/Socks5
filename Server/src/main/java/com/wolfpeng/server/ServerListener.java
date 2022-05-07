package com.wolfpeng.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ServerListener extends Thread {
    /**
     * 监听端口
     */
    private int                   port;
    /**
     * 运行标志符
     */
    private boolean               running;
    private Selector              selector;
    /**
     * 监听事件handler
     */
    private ServerListenerHandler listenerHandler;

    public interface ServerListenerHandler {
        boolean onAccept(SocketChannel socketChannel) throws ClosedChannelException;
    }

    /**
     * 创建一个监听器
     * @param port
     */
    public ServerListener(int port, ServerListenerHandler listenerHandler) {
        this.port = port;
        this.listenerHandler = listenerHandler;
    }

    /**
     * @see Thread#run()
     */
    @Override public synchronized void start() {
        this.running = true;
        super.start();
    }

    /**
     * 删除
     */
    public synchronized void destroy() {
        this.running = false;
    }

    /**
     * @see Thread#run()
     */
    @Override public void run() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (running) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.stream().filter(SelectionKey::isValid).map(SelectionKey::channel)
                        .forEach(channel -> {
                            ServerSocketChannel chl = (ServerSocketChannel) channel;
                            try {
                                SocketChannel clientChannel;
                                while (Objects.nonNull(clientChannel = chl.accept())) {
                                    this.listenerHandler.onAccept(clientChannel);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(selector)) {
                    selector.close();
                }
                if (Objects.nonNull(serverSocketChannel)) {
                    serverSocketChannel.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
