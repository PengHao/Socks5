package com.wolfpeng.core.util;

/**
 * 端口 host
 */
public class HostPort {
    /**
     * 服务器host
     */
    private String host;
    /**
     * 链接端口
     */
    private int port;

    public HostPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
