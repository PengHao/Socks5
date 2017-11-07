package com.motherchildren.wx.moudle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.wolfpeng.core.http.HttpHead;
import org.springframework.util.StringUtils;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public class Session {

    String expires;
    public String getExpires() {
        return expires;
    }
    public void setExpires(String expires) {
        this.expires = expires;
    }

    public Date expriesToDate(){
        Date date = null;
        try {
            date = new SimpleDateFormat("EEE, dd MMM yyyy HH:MM:ss z", Locale.ENGLISH).parse(expires);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    boolean phpSessionHasChanged = false;
    String phpSession;
    public void setPhpSession(String phpSession) {
        this.phpSession = phpSession;
    }
    public String getPhpSession() {
        return phpSession;
    }
    public boolean isPhpSessionHasChanged() {
        return phpSessionHasChanged;
    }
    public void setPhpSessionHasChanged(boolean changed) {
        this.phpSessionHasChanged = changed;
    }


    String activeCardId;
    public String getActiveCardId() {
        return activeCardId;
    }

    public void setActiveCardId(String activeCardId) {
        this.activeCardId = activeCardId;
    }

    Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    String cookies;
    public String getCookies() {
        return cookies;
    }
    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public void updateCookies(String cookiesString) {

        if (cookiesString == null) {
            return;
        }

        HashMap<String, String > oldCookieMap = HttpHead.getCookieMap(cookies);
        HashMap<String, String > newCookieMap = HttpHead.getCookieMap(cookiesString);
        for (HashMap.Entry<String, String> entry : newCookieMap.entrySet()) {
            oldCookieMap.put(entry.getKey(), entry.getValue());
        }

        cookies = "";
        for (HashMap.Entry<String, String> entry : oldCookieMap.entrySet()) {
            if (!StringUtils.isEmpty(cookies)) {
                cookies += "; ";
            }
            cookies += entry.getKey() + "=" + entry.getValue();
        }
    }


    String activeClassId;
    public String getActiveClassId() {
        return activeClassId;
    }
    public void setActiveClassId(String activeClassId) {
        this.activeClassId = activeClassId;
    }

    String activeDate;
    public String getActiveDate() {
        return activeDate;
    }
    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }
}
