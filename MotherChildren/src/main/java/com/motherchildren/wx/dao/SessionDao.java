package com.motherchildren.wx.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.motherchildren.wx.moudle.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
@Component
public class SessionDao {
    static Map<String, Session> userCache = new HashMap<String, Session>();

    @Resource
    private JdbcTemplate serverUserTemplate;

    public Session getSession(String phpSession) {
        String sql = String.format("select * from `MotherChildrenSession` WHERE `php_session`='%s'", phpSession);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            Session session = new Session();
            session.setPhpSession(((String) map4dept.get("php_session")));
            session.setExpires(((String) map4dept.get("expries")));
            session.setId(Long.valueOf(map4dept.get("id").toString()));
            session.setCookies((String)map4dept.get("coockies"));
            session.setActiveClassId((String)map4dept.get("active_class_id"));
            session.setActiveCardId((String)map4dept.get("active_card_id"));
            session.setActiveDate((String)map4dept.get("active_date"));
            userCache.put(phpSession, session);
            return session;
        }
        return null;
    }

    public void insertSession(Session session) {
        ;
        String sql = String.format("INSERT INTO MotherChildrenSession (id, php_session, coockies, expries, active_class_id, active_date, active_card_id) VALUES (null, '%s', '%s', '%s', '%s', '%s', '%s')", session.getPhpSession(), session.getCookies(), session.getExpires(), session.getActiveClassId(), session.getActiveDate(), session.getActiveCardId());
        serverUserTemplate.execute(sql);
    }

    public void updateSession(Session session) {

        String sql = "update MotherChildrenSession session SET "+
            (session.isPhpSessionHasChanged() ?  "session.php_session = '"+ session.getPhpSession() + "'," : "") +
            "session.coockies = '" + session.getCookies() +
            "', session.expries = '"+ session.getExpires() +
            "', session.active_class_id = '"+ session.getActiveClassId() +
            "', session.active_date = '"+ session.getActiveDate() +
            "', session.active_card_id = '"+ session.getActiveCardId() +
            "' where session.id = " + session.getId();
        serverUserTemplate.execute(sql);
    }
}
