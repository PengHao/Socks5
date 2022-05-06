package com.wolfpeng.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.wolfpeng.core.Application;

import com.wolfpeng.core.util.Utils;
import com.wolfpeng.core.util.Utils.SockProto;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/11.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */

@Component("application")
public class ServerApplication implements Application, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    SockProto sockProto = SockProto.Sock5;
    int listenPort = 9050;
    Socket acceptSocket;

    void start()
        throws IOException {
        ServerSocket ss = new ServerSocket(listenPort);
        Utils.log("Socks server port : %s listening ...", listenPort);
        while (null != (acceptSocket = ss.accept())) {
            new Thread(new Session(sockProto, acceptSocket)).start();
            acceptSocket = null;
        }
        ss.close();
    }


    public void onApplicationStart() {
        java.security.Security.setProperty("networkaddress.cache.ttl", "86400");
        Utils.log("\n\tUSing port openSock4 openSock5 user pwd");
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onApplicationExit() {
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerApplication.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
    //
    //public static Object getBean(String var1, Object... var2) throws BeansException {
    //    return applicationContext.getBean(var1, var2);
    //}

    public static <T> T getBean(Class<T> var1) throws BeansException{
        return applicationContext.getBean(var1);
    }

    public static <T> T getBean(Class<T> var1, Object... var2) throws BeansException{
        return applicationContext.getBean(var1, var2);
    }
}
