package com.motherchildren.wx.action;

import com.motherchildren.wx.moudle.Task;
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
public class Doctorlist extends EmptyAction {


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
            HttpData message = connection.getResponse();
            String tmpBodyString = getDecodeBodyString(message);
            String[] bodyStrings = tmpBodyString.split("<script src=\"http://hxcdn2.mobimedical.cn/tpl/Wap/default/static//js/doctorlisthandler.js");
            String bodyString1;
            String bodyString2;

            Session session = getSession(connection);
            String classId = connection.getRequest().httpHead.getGetProperty("classid");
            String dateString = connection.getRequest().httpHead.getGetProperty("dateString");

            session.setActiveClassId(classId);
            session.setActiveDate(dateString);
            sessionDao.updateSession(session);

            if (bodyStrings.length > 1) {
                Integer index = bodyStrings[1].indexOf("</script>");
                bodyString1 = bodyStrings[0];
                if (index >= 0) {
                    bodyString2 = bodyStrings[1].substring(index);
                    String sc = "<script src=\"http://blog.wolfpeng.com/wp-content/uploads/2017/05/doctorlisthandler.js\">";
                    tmpBodyString = bodyString1 + sc + bodyString2;
                }
            }
            tmpBodyString = tmpBodyString.replace("var date = \"1970-01-01\";", "var date = \"" + Task
                .dateString()+"\";");
            byte[] compressed = Utils.compress(tmpBodyString, "utf8");
            byte[] chunkedBody = message.encodeChunkedBody(compressed);
            message.bodyCache.reset();
            message.bodyCache.write(chunkedBody);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=doctorlist&wx=MbTXENO0O0Ok&classid=%s&dateString=%s "
        + "HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Cookie: tgw_l7_route=%s; card_list_url=%2Findex"
        + ".php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok%26entry%3D1; "
        + "PHPSESSID=%s\r\r\n"
        + "Connection: keep-alive\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=classlist&wx=MbTXENO0O0Ok&dateString=%s\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";
}
