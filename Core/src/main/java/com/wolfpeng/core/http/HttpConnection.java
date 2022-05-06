package com.wolfpeng.core.http;


import javax.annotation.Resource;

import com.wolfpeng.core.http.HttpData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/3/9.
 */

public class HttpConnection {
    private String host;
    private Integer port;

    private HttpData request = new HttpData();

    private HttpData response = new HttpData();


    public void init(String host, Integer port){
        this.host = host;
        this.port = port;
    }

    private Long socksUserId = -1L;

    public Long getSocksUserId() {
        return socksUserId;
    }

    public void setSocksUserId(Long socksUserId) {
        this.socksUserId = socksUserId;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public HttpData getRequest() {
        return request;
    }

    public HttpData getResponse() {
        return response;
    }

    public void setRequest(HttpData request) {
        this.request = request;
    }

    public void setResponse(HttpData response) {
        this.response = response;
    }
}
