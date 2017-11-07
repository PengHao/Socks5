package com.wolfpeng.server;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */

@Component
public class SocksUserDao {

    @Resource
    private JdbcTemplate serverUserTemplate;

    public SocksUser findUser(String userName, String pass) {
        String sql = String.format("select * from SocksUser WHERE id = %s AND password = %s", userName, pass);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            SocksUser user = new SocksUser();
            user.setUserID(((BigDecimal) map4dept.get("id")).longValue());
            user.setUserName(((String)map4dept.get("name")));
            user.setUserPass(((String)map4dept.get("password")));
            return user;
        }
        return null;
    }

    public int delete(int bid){
        String sql = "delete from DeptInfo where bid =?";
        return serverUserTemplate.update(sql, new Object[]{bid});
    }

}
