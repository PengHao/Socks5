package com.motherchildren.wx.action;

import com.motherchildren.wx.moudle.Card;
import com.motherchildren.wx.moudle.Session;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.util.Utils;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/17.
 * Copyright ? 2017年 Alibaba. All rights reserved.
 * 检测是否有未完成的支付
 */
@Component
public class CheckLockAndNotPay extends EmptyAction {

    static String headerTemplate = "POST /index.php?g=WapApi&m=AppointmentApi&a=checkLockAndNotPay HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Accept: application/json, text/javascript, */*; q=0.01\r\n"
        + "X-Requested-With: XMLHttpRequest\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n"
        + "Origin: http://wx.motherchildren.com\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Connection: keep-alive\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=selectdate&wx=MbTXENO0O0Ok&id=%s&isNocard=0&isSocialCard=0\r\n"
        + "Content-Length: 9\r\n"
        + "Cookie: card_list_url=%2Findex.php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok"
        + "%26entry%3D1; tgw_l7_route=%s; PHPSESSID=%s\r\n"
        + "\r\n";

    static String bodyTemplate = "id=";

    static HttpData makeRequest(Session session, Card card) {
        String fullHeader = getFullHeader(headerTemplate, session);
        String body = bodyTemplate + card.getId();
        return sendHttpData(fullHeader, body.getBytes());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
