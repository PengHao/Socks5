package com.motherchildren.wx.action;

import com.motherchildren.wx.moudle.Session;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.util.Utils;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/17.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
@Component
public class Classlist extends EmptyAction {
    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=classlist&wx=MbTXENO0O0Ok&date=%s HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        +"Cookie: card_list_url=%2Findex.php%3Fg%3DWap%26m%3DFeyAppointment%26a%3Dcardlist%26wx%3DMbTXENO0O0Ok; "
        + "PHPSESSID=%s\r\n"
        + "Connection: keep-alive\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=selectdate&wx=MbTXENO0O0Ok&id=%s&isNocard=0&isSocialCard=0\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";

    static HttpData makeRequest(Session session) {
        String fullHeader = getFullHeader(headerTemplate, session);
        return sendHttpData(fullHeader);
    }

    @Override
    public HttpData processRequest(HttpConnection connection) {
        Utils.log("processRequest");
        return null;
    }

    @Override
    public HttpData processResponse(HttpConnection connection) {
        try {
            super.processResponse(connection);
            Session session = getSession(connection);
            //return Doctorlist.makeRequest(session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
