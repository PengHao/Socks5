package com.wolfpeng.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * 服务器reader
 */
public class ServerReader extends Thread {
    /**
     * 读selector
     */
    private Selector            selector;
    /**
     * 是否还在执行
     */
    private boolean             running;
    /**
     * 读取回调
     */
    private ServerReaderHandler readerHandler;

    /**
     * 读取回调接口
     */
    public interface ServerReaderHandler {
        boolean onReadable(Session session);
    }

    public ServerReader(ServerReaderHandler readerHandler) throws IOException {
        this.readerHandler = readerHandler;
        this.running = false;
        this.selector = Selector.open();
    }

    /**
     * 添加session
     * @param session
     * @throws ClosedChannelException
     */
    void addSession(Session session) throws ClosedChannelException {
        session.getClientSocketChannel().register(selector, SelectionKey.OP_READ, session);
    }

    @Override public void run() {
        super.run();
        while (running) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.stream().filter(SelectionKey::isValid)
                        .filter(SelectionKey::isReadable).forEach(key -> {
                            Session session = (Session) key.attachment();
                            readerHandler.onReadable(session);
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
