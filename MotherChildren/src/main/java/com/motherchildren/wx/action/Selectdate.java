package com.motherchildren.wx.action;

import com.motherchildren.wx.moudle.Card;
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
public class Selectdate extends EmptyAction {

    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=selectdate&wx=MbTXENO0O0Ok&id=%s"
        + "&isNocard=0&isSocialCard=0 HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Cookie: tgw_l7_route=%s; card_list_url=%2Findex"
        + ".php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok%26entry%3D1; "
        + "PHPSESSID=%s\r\n"
        + "Connection: keep-alive\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index.php?ptag=17048.1"
        + ".5&g=Wap&m=FeyUserCenter&a=index&wx=MbTXENO0O0Ok&loginFlag=1\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";

    static HttpData makeRequest(Session session) {
        String fullHeader = getFullHeader(headerTemplate, session);
        HttpData data = sendHttpData(fullHeader);
        return data;
    }

    @Override
    public HttpData processRequest(HttpConnection connection) {
        Utils.log("processRequest");
        super.processRequest(connection);
        return null;
    }

    @Override
    public HttpData processResponse(HttpConnection connection) {
        try {
            super.processResponse(connection);
            HttpData message = connection.getRequest();
            String cardId = message.httpHead.getGetProperty("id");
            Session session = getSession(connection);
            session.setActiveCardId(cardId);
            sessionDao.updateSession(session);
            Card card = cardDao.getCard(cardId);
            if (card == null) {
                card = new Card();
                card.setId(cardId);
                card.setSessionId(session.getId());
                cardDao.insertCard(card);
            } else {
                card.setSessionId(session.getId());
                cardDao.updateCard(card);
            }
            //return Classlist.makeRequest(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
