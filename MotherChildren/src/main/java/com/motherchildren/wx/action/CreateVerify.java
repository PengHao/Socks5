package com.motherchildren.wx.action;

import java.util.Random;

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
public class CreateVerify extends EmptyAction {

    static String headerTemplate = "GET /index.php?g=Wap&m=FeyVerify&a=createVerify&v=%s HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Accept: image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5\r\n"
        + "Connection: keep-alive\r\n"
        + "Cookie: card_list_url=%2Findex.php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok"
        + "%26entry%3D1; PHPSESSID=igot7qtj1179tpjnpdi8n3ceb4\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=doctordetail&wx=MbTXENO0O0Ok&doctorid=%s&dateString=%s&time=1\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";

    static HttpData makeRequest(Session session) {
        String fullHeader = getFullHeader(headerTemplate, session);
        Random random = new Random();
        fullHeader = fullHeader.replace("&v=%s", "&v="+random.nextDouble());
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
