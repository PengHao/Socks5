package com.motherchildren.wx.moudle;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public class Card {

    Long sessionId;
    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    Integer count = 0;
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
