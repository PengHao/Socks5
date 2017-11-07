package com.wolfpeng.server;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */

public class SocksUser {
    String userName;
    String userPass;
    Long userID;

    void setUserName(String userName) {
        this.userName = userName;
    }

    void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    void setUserID(Long userID) {
        this.userID = userID;
    }
}
