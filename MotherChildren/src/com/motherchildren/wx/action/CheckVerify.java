package com.motherchildren.wx.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
public class CheckVerify extends EmptyAction {

    static String headerTemplate = "POST /index.php?g=Wap&m=FeyVerify&a=checkVerify HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Accept: */*\r\n"
        + "X-Requested-With: XMLHttpRequest\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n"
        + "Origin: http://wx.motherchildren.com\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Connection: keep-alive\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=doctordetail&wx=MbTXENO0O0Ok&doctorid=%s&dateString=%s&time=1\r\n"
        + "Content-Length: 125\r\n"
        + "Cookie: card_list_url=%2Findex.php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok"
        + "%26entry%3D1; PHPSESSID=%s\r\n"
        + "\r\n";

    static String body = "tagArray%5B0%5D%5B%5D=328&tagArray%5B0%5D%5B%5D=235&tagArray%5B1%5D%5B%5D=758&tagArray%5B1%5D%5B%5D=241&selectDate=2017-05-25";

    static HttpData makeRequest(Session session) {
        String fullHeader = getFullHeader(headerTemplate, session);
        try {
            String decodeBody = URLDecoder.decode(body, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
