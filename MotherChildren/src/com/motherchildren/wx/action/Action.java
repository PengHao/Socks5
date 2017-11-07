package com.motherchildren.wx.action;

import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;

/**
 * Created by penghao on 2017/5/10.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public interface Action {
    HttpData processRequest(HttpConnection connection);

    HttpData processResponse(HttpConnection connection) throws Exception;
}
