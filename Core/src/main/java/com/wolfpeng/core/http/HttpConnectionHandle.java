package com.wolfpeng.core.http;

/**
 * Created by penghao on 2017/5/12.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public interface HttpConnectionHandle {
    boolean isPathValidate(String path);

    HttpData processRequestData(HttpConnection connection);

    HttpData processResponseData(HttpConnection connection);

}
