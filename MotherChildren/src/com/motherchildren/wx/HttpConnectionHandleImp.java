package com.motherchildren.wx;

import com.motherchildren.wx.action.Action;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpConnectionHandle;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.util.Utils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by penghao on 2017/5/12.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
@Service("wx.motherchildren.com")
public class HttpConnectionHandleImp implements HttpConnectionHandle,
    ApplicationContextAware{

    static ApplicationContext applicationContext;

    Action getAction(HttpConnection connection)  {

        String actionName = connection.getRequest().httpHead.getGetProperty("a");
        if (!StringUtils.isEmpty(actionName)) {
            actionName = actionName.substring(0, 1).toUpperCase() + (actionName.length() > 1 ? actionName.substring(1) : "");
        } else {
            actionName = "EmptyAction";
        }
        Utils.log("actionName: %s", actionName);

        try {
            Class<Action> cls = (Class<Action>)Class.forName("com.motherchildren.wx.action."+actionName);
            Action rs = applicationContext.getBean(cls);
            return rs;
        } catch (Exception e) {
            Utils.log("no class for action: %s", actionName);
            return null;
        }
    }

    public boolean isPathValidate(String path) {
        return false;
    }

    public HttpData processRequestData(HttpConnection connection) {
        Action action = getAction(connection);
        if (action == null) {
            return null;
        }

        return action.processRequest(connection);
    }

    public HttpData processResponseData(HttpConnection connection) {
        Action action = getAction(connection);
        if (action == null) {
            return null;
        }
        try {
            return action.processResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HttpConnectionHandleImp.applicationContext = applicationContext;
    }
}
