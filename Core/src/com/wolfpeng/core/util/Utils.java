package com.wolfpeng.core.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by penghao on 2017/3/9.
 */
public class Utils {
    public interface Authorization {
        boolean login(String user, String password);
    }

    /**
     * socks5代理的登录用户名，如果 不为空表示需要登录验证
     */
    public static String user;
    /**
     * socks5代理的登录密码，
     */
    public static String pwd;

    private static final Integer sock4 = 4;
    private static final Integer sock5 = 5;

    public enum SockProto {
        Sock4(sock4),
        Sock5(sock5);

        private Integer value;
        SockProto(Integer value) {
            this.value = value;
        }

        public Socket serverSocket(Socket clientSocket, Authorization authorization) throws IOException {
            InputStream client_in = clientSocket.getInputStream();
            Socket serverSocket = null;

            byte[] tmp = new byte[1];
            int n = client_in.read(tmp);
            if (n == 1) {
                byte protocol = tmp[0];

                if ((Utils.sock4.equals(value) && 0x04 == protocol)) {

                    serverSocket = createSock4Socket(clientSocket, authorization);

                } else if ((Utils.sock5.equals(value) && 0x05 == protocol)) {
                    serverSocket = createSock5Socket(clientSocket, authorization);
                } else {// 非socks 4 ,5 协议的请求
                    log("not socks proxy : %s  openSock4[] openSock5[]", tmp[0]);
                }
            }
            return serverSocket;
        }


        private Socket createSock4Socket(Socket clientSocket, Authorization authorization) throws IOException {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            Socket proxy_socket = null;
            byte[] tmp = new byte[3];
            in.read(tmp);

            int port = ByteBuffer.wrap(tmp, 1, 2).asShortBuffer().get() & 0xFFFF;
            String host = getHost((byte) 0x01, in);
            in.read();
            byte[] rsv = new byte[8];
            try {
                proxy_socket = new Socket(host, port);
                log("connect [%s] %s:%s", tmp[1], host, port);
                rsv[1] = 90;// 代理成功
            } catch (Exception e) {
                log("connect exception  %s:%s", host, port);
                rsv[1] = 91;// 代理失败.
            }
            out.write(rsv);
            out.flush();
            return proxy_socket;
        }

        private Socket createSock5Socket(Socket clientSocket, Authorization authorization) throws IOException {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            String host;
            Integer port;
            byte[] tmp = new byte[2];
            in.read(tmp);
            boolean isLogin = false;
            byte method = tmp[1];
            int a = 0;
            if (0x02 == tmp[0]) {
                method = 0x00;
                a = in.read();
            }
            if (authorization != null) {
                method = 0x02;
            }
            tmp = new byte[] { 0x05, method };
            out.write(tmp);
            out.flush();
            Object resultTmp = null;
            if (0x02 == method) {
                int b = in.read();
                String user;
                String pwd;
                if (0x01 == b) {
                    b = in.read();
                    tmp = new byte[b];
                    in.read(tmp);
                    user = new String(tmp);
                    b = in.read();
                    tmp = new byte[b];
                    in.read(tmp);
                    pwd = new String(tmp);

                    if (authorization.login(user, pwd)) {
                        isLogin = true;
                        tmp = new byte[] { 0x05, 0x00 };
                        out.write(tmp);
                        out.flush();
                        log("%s login success !", user);
                    } else {
                        log("%s login faild !", user);
                    }
                }
            }
            byte cmd = 0;
            if (authorization == null || isLogin) {
                tmp = new byte[4];
                in.read(tmp);
                cmd = tmp[1];
                host = getHost(tmp[3], in);
                tmp = new byte[2];
                in.read(tmp);
                port = ByteBuffer.wrap(tmp).asShortBuffer().get() & 0xFFFF;
                log("connect %s:%s", host, port);
                ByteBuffer rsv = ByteBuffer.allocate(10);
                rsv.put((byte) 0x05);
                try {
                    if (0x01 == cmd) {
                        resultTmp = new Socket(host, port);
                        rsv.put((byte) 0x00);
                    } else if (0x02 == cmd) {
                        resultTmp = new java.net.ServerSocket(port);
                        rsv.put((byte) 0x00);
                    } else {
                        rsv.put((byte) 0x05);
                        resultTmp = null;
                    }
                } catch (Exception e) {
                    rsv.put((byte) 0x05);
                    resultTmp = null;
                }
                rsv.put((byte) 0x00);
                rsv.put((byte) 0x01);
                rsv.put(clientSocket.getLocalAddress().getAddress());
                Short localPort = (short) ((clientSocket.getLocalPort()) & 0xFFFF);
                rsv.putShort(localPort);
                tmp = rsv.array();
            } else {
                tmp = new byte[] { 0x05, 0x01 };
                log("socks server need login,but no login info .");
            }
            out.write(tmp);
            out.flush();
            if (null != resultTmp && 0x02 == cmd) {
                java.net.ServerSocket ss = (java.net.ServerSocket) resultTmp;
                try {
                    resultTmp = ss.accept();
                } catch (Exception e) {
                    System.out.print(e);
                } finally {
                    closeIo((Socket) resultTmp);
                }
            }
            return (Socket) resultTmp;
        }

    }



    /**
     * 获取目标的服务器地址
     *
     * @createTime 2014年12月14日 下午8:32:15
     * @param type
     * @param in
     * @return
     * @throws IOException
     */
    public static String getHost(byte type, InputStream in) throws IOException {
        String host = null;
        byte[] tmp = null;
        switch (type) {
            case 0x01:// IPV4协议
                tmp = new byte[4];
                in.read(tmp);
                host = InetAddress.getByAddress(tmp).getHostAddress();
                break;
            case 0x03:// 使用域名
                int l = in.read();
                tmp = new byte[l];
                in.read(tmp);
                host = new String(tmp);
                break;
            case 0x04:// 使用IPV6
                tmp = new byte[16];
                in.read(tmp);
                host = InetAddress.getByAddress(tmp).getHostAddress();
                break;
            default:
                break;
        }
        return host;
    }


    /**
     * IO操作中共同的关闭方法
     *
     * @createTime 2014年12月14日 下午7:50:56
     * @param closeable
     */
    public static final void closeIo(Socket closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * IO操作中共同的关闭方法
     *
     * @createTime 2014年12月14日 下午7:50:56
     * @param closeable
     */
    public static final void closeIo(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public final static void log(String message, Object... args) {
        Date dat = new Date();
        String msg = String.format("%1$tF %1$tT %2$-5s %3$s%n", dat, Thread.currentThread().getId(),
                String.format(message, args));
        System.out.print(msg);
    }


    // 压缩
    public static byte[] compress(String str, String encoding) throws IOException {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str.getBytes();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes(encoding));
        gzip.close();
        return out.toByteArray();
    }

    // 解压缩
    public static String uncompressToString(byte[] b, String encoding) {
        if (null != b && b.length > 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(b);

            try {
                GZIPInputStream gzip = new GZIPInputStream(in);
                byte[] buffer;
                buffer = new byte[256];
                int n;
                while ((n = gzip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                return out.toString(encoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public static String uncompressGzipToString(byte[] b, String encoding) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
