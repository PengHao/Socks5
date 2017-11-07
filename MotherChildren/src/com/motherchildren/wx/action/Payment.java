package com.motherchildren.wx.action;

import com.motherchildren.wx.moudle.Card;
import com.motherchildren.wx.moudle.Doctor;
import com.motherchildren.wx.moudle.Task;
import com.motherchildren.wx.moudle.Session;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.util.Utils;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/17.
 * Copyright ? 2017年 Alibaba. All rights reserved.
 */
@Component
public class Payment extends EmptyAction {
    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=payment&wx=MbTXENO0O0Ok&doctorid=%s HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Cookie: tgw_l7_route=%s; card_list_url=%2Findex"
        + ".php%3Fg%3DWap%26m%3DFeyAppointment%26a%3DcardCheselist%26wx%3DMbTXENO0O0Ok%26entry%3D1; "
        + "PHPSESSID=%s\r\n"
        + "Connection: keep-alive\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=doctordetail&wx=MbTXENO0O0Ok&doctorid=%s&date=%s&time=1\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";


    static HttpData makeRequest(Session session) {
        String fullHeader = getFullHeader(headerTemplate, session);
        return sendHttpData(fullHeader);
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

            HttpData request = connection.getRequest();
            Session session = getSession(connection);
            String cardId = session.getActiveCardId();
            String doctorId = request.httpHead.getGetProperty("doctorid");
            Doctor doctor = doctorDao.getDoctorByDoctorId(doctorId);
            String s = request.httpHead.getGetProperty("isSetAuto");
            if (s == null) {
                taskDao.delete(cardId, doctor.getId());
                return null;
            }

            Task task = taskDao.getTask(cardId, doctor.getId());
            Card card = cardDao.getCard(cardId);
            if (task == null) {
                HttpData response = connection.getResponse();

                if ( false /*card.getCount() == 0*/) {
                    String tmpBodyString = "预约抢号失败!请先购买充值";
                    byte[] compressed = Utils.compress(tmpBodyString, "utf8");
                    byte[] chunkedBody = response.encodeChunkedBody(compressed);
                    response.bodyCache.reset();
                    response.bodyCache.write(chunkedBody);
                } else {
                    task = new Task();
                    task.setDoctorId(doctor.getId());

                    task.setCardId(cardId);
                    task.setClassId(session.getActiveClassId());
                    taskDao.insert(task);
                    card.setCount(card.getCount() - 1);
                    cardDao.updateCard(card);
                    String tmpBodyString = "预约抢号成功!务必7:15按时操作";
                    byte[] compressed = Utils.compress(tmpBodyString, "utf8");
                    byte[] chunkedBody = response.encodeChunkedBody(compressed);
                    response.bodyCache.reset();
                    response.bodyCache.write(chunkedBody);
                }
            } else {
                card.setCount(card.getCount() + 1);
                cardDao.updateCard(card);
                taskDao.delete(cardId, doctor.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
