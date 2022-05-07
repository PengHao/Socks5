package com.motherchildren.wx.action;

import java.util.List;
import com.motherchildren.wx.moudle.Card;
import com.motherchildren.wx.moudle.Task;
import com.motherchildren.wx.moudle.Session;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.http.HttpHead;
import com.wolfpeng.core.util.Utils;
import com.wolfpeng.server.ProxyServer;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/10.
 * Copyright ? 2017�� Alibaba. All rights reserved.
 */
@Component
public class Index extends EmptyAction {

    @Override
    public HttpData processRequest(HttpConnection connection) {
        super.processRequest(connection);
        Utils.log("processRequest");
        return null;
    }


    public Session updateSession(HttpConnection connection) {
        Session requestSession = getSession(connection);
        HttpData response = connection.getResponse();
        String newCookieString = response.getHeaderFeild(HttpHead.SET_COOKIE);
        String oldCookieString = connection.getRequest().getHeaderFeild(HttpHead.COOKIE);
        String phpSession = HttpHead.getCookie(newCookieString, phpSessionKey);
        if (phpSession == null) {
            phpSession = HttpHead.getCookie(connection.getRequest().getHeaderFeild(HttpHead.COOKIE), phpSessionKey);
        }
        if (requestSession == null) {
            requestSession = new Session();
            String expires = response.getHeaderFeild("Expires");
            requestSession.setExpires(expires);
            if (!phpSession.equals(requestSession.getPhpSession())) {
                requestSession.setPhpSessionHasChanged(true);
            }
            requestSession.setPhpSession(phpSession);
            requestSession.setCookies(oldCookieString);
            if (newCookieString != null) {
                requestSession.updateCookies(newCookieString);
            }
            sessionDao.insertSession(requestSession);
        } else {
            requestSession.setCookies(oldCookieString);
            if (newCookieString != null) {
                requestSession.updateCookies(newCookieString);
            }
            sessionDao.updateSession(requestSession);
        }
        return requestSession;
    }

    @Override
    public HttpData processResponse(HttpConnection connection)  {
        try {
            HttpData message = connection.getResponse();
            if ( message.httpHead.getHeaderProperty("first").contains("302 Moved Temporarily")) {
                throw new Exception("302 Moved Temporarily");
            }
            super.processResponse(connection);
            //login success
            Session session = updateSession(connection);
            List<Card> cards = cardDao.getCards(session.getId());
            Doctorlistajax doctorlistajax = ProxyServer.getBean(Doctorlistajax.class);
            for (Card card : cards) {
                List<Task> tasks = taskDao.getTasks(card.getId());
                HttpData data = doctorlistajax.makeRequest(session, card, tasks);
                if (data != null) {
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
