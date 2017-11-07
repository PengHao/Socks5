package com.motherchildren.wx.action;

import java.io.IOException;
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
 * Copyright ? 2017�� Alibaba. All rights reserved.
 */
@Component
public class Doctordetail extends EmptyAction {


    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=doctordetail&wx=MbTXENO0O0Ok&doctorid=%s&date=%s"
        + "&time=%s HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Cookie: tgw_l7_route=%s; card_list_url=%2Findex"
        + ".php%3Fg%3DWap%26m%3DFeyAppointment%26a%cardlist%26wx%3DMbTXENO0O0Ok; "
        + "PHPSESSID=%s\r\n"
        + "Connection: keep-alive\r\n"
        + "Upgrade-Insecure-Requests: 1\r\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=doctorlist&wx=MbTXENO0O0Ok&classid=%s&date=%s\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "\r\n";


    HttpData makeRequest(Session session, Card card, Task task) {
        String fullHeader = getFullHeader(headerTemplate, session);
        Long doctorId = task.getDoctorId();
        Doctor doctor = doctorDao.getDoctorById(doctorId);
        String triedString = task.getTriedTime();

        String _15DayString = get15DayLaterString();
        if (triedString != null && getDate(triedString).compareTo(getDate(_15DayString)) >= 0 ) {
            return null;
        }

        String classId = doctor.getClassId();
        fullHeader = fullHeader.replace("&doctorid=%s&", "&doctorid="+doctor.getDoctorId()+"&");
        fullHeader = fullHeader.replace("&date=%s", "&date="+_15DayString);
        fullHeader = fullHeader.replace("&classid=%s&", "&classid="+classId+"&");
        fullHeader = fullHeader.replace("&time=%s", "&time="+doctor.getTime());

        HttpData message = sendHttpData(fullHeader);
        String tmpBodyString = getDecodeBodyString(message);

        String splitString = "var lasttime = parseInt('";
        String[] splits = tmpBodyString.split(splitString);
        String lasttime = splits[1].substring(splits[1].indexOf(");"));
        Integer last = Integer.valueOf(lasttime);
        if (last > 90) {
            return null;
        }

        if ( tmpBodyString.contains("�Ƿ�����") || tmpBodyString.contains("onclick=\"javascript:history.go(-1)\" ")) {
            //�Һ�ʧ��
            task.setTriedTime(get15DayLaterString());
            taskDao.update(task);
            return null;
        }

        tmpBodyString = tmpBodyString.replace("var _lasttime = (endtime - Date.parse(new Date()));", "var _lasttime ="
            + " (endtime - Date.parse(new Date()));\n"
            + "            \n"
            + "            if (_lasttime < 2) {\n"
            + "            \tfeyVerify();\n"
            + "            };");

        tmpBodyString = tmpBodyString.replace("function getCountdown(lasttime) {", functionString
            + "        function getCountdown(lasttime) {");
        tmpBodyString = tmpBodyString.replace("&doctorid=%s\"", "&doctorid="+doctor.getDoctorId()+"\"");


        tmpBodyString = delete(tmpBodyString, "<span class=\"position\">", "</span>");
        tmpBodyString = delete(tmpBodyString, "<ul class=\"doc_nav\">", "��������</a></div></ul>");
        tmpBodyString = delete(tmpBodyString, "<div class=\"doc_infobox doc_content\" style=\"display:none;\">",
            "</dd></dl></div>");
        tmpBodyString = delete(tmpBodyString, "<div class=\"go_yue\">", "ԤԼ</a></div>");
        tmpBodyString = delete(tmpBodyString, "<div id=\"mask_bg\">", "</span>");

        tmpBodyString = tmpBodyString.replace("class=\"doc_headbox\"",
            "class=\"doc_headbox\" style=\" height:40px\"");
        tmpBodyString = tmpBodyString.replace("div class=\"doc_portrait\"",
            "div class=\"doc_portrait\" style=\" "
                + "height:40px\"");
        tmpBodyString = tmpBodyString.replace("class=\"doc_main\"", "");
        tmpBodyString = tmpBodyString.replace("class=\"doc_header\"", "");
        tmpBodyString = tmpBodyString.replace("<div class=\"bg\"></div>", "");
        tmpBodyString = tmpBodyString.replace(
            "<div class=\"tips_ok_box\" id=\"tips_ok_box\" style=\"display:none;\">", "");
        tmpBodyString = tmpBodyString.replace("<div class=\"yue_ok_btn\"><span "
            + "class=\"close\">ȡ��</span><a href=\"javascript:void(0);\" class=\"yue_ok_sent\"></a></div>", "");
        tmpBodyString = tmpBodyString.replace("style=\"display:none;\"", "");
        tmpBodyString = tmpBodyString.replace("<div id=\"mask_bg\"></div>", "");
        tmpBodyString = tmpBodyString.replace("<div class=\"checkimgBox\" id=\"checkDiv\">",
            "<div class=\"checkimgBox\" "
                + "id=\"checkDiv\" stlye=\"background: #FF0000\">");
        tmpBodyString = tmpBodyString.replace("function changeImg(){", "changeImg();\r\nfunction changeImg(){");

        try {
            byte[] compressed = Utils.compress(tmpBodyString, "utf8");
            byte[] chunkedBody = message.encodeChunkedBody(compressed);
            message.bodyCache.reset();
            message.bodyCache.write(chunkedBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
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
            String tmpBodyString = getDecodeBodyString(connection.getResponse());
            String doctorId = connection.getRequest().httpHead.getGetProperty("doctorid");
            Doctor doctor = doctorDao.getDoctorByDoctorId(doctorId);
            String[] sb = tmpBodyString.split("<span>��<font class=\"f_red\">");
            Integer number = 0;
            if (sb.length > 1) {
                String[] numbers = sb[1].split("</font></span>");
                number = Integer.valueOf(numbers[0]);
            }


            Session session = getSession(connection);
            HttpData message = connection.getResponse();
            if (number == 0) {
                //a=payment
                tmpBodyString = tmpBodyString.replace("<span>����ʱ�䣺<font id=\"apponitment_date\">#</font>&nbsp;"
                    + "&nbsp;<font id=\"apponitment_time\">#</font></span>", "<span>���ź�֧��ʱ�䣺<font >�ڶ����糿7��15</font>&nbsp;&nbsp;</span>");
                tmpBodyString = tmpBodyString.replace("<span class=\"yue_tip"
                    + "\">��ʾ������ȷ����������ύ��΢�ŹҺ�һ���ɹ����Ų��˺ţ���ȷ�Ϻ���֧����</span>", "<span class=\"yue_tip\">��ʾ��������糿7:15"
                    + "�������Ÿ������</span>");
                if (taskDao.getTask(session.getActiveCardId(), doctor.getId()) != null) {
                    tmpBodyString = tmpBodyString.replace("&a=payment&", "&a=payment&isSetAuto=false&");
                    tmpBodyString = tmpBodyString.replace("����", "ȡ������");
                    tmpBodyString = tmpBodyString.replace("ԤԼ", "ȡ������");
                    tmpBodyString = tmpBodyString.replace("�Һ�", "ȡ������");
                    tmpBodyString = tmpBodyString.replace("��ʾ��������糿7:15", "��ʾ��������糿7:15֮ǰ");
                } else {
                    tmpBodyString = tmpBodyString.replace("&a=payment&", "&a=payment&isSetAuto=true&");
                    tmpBodyString = tmpBodyString.replace("ԤԼ", "����");
                    tmpBodyString = tmpBodyString.replace("�Һ�", "����");
                }
                byte[] compressed = Utils.compress(tmpBodyString, "utf8");
                byte[] chunkedBody = message.encodeChunkedBody(compressed);
                message.bodyCache.reset();
                message.bodyCache.write(chunkedBody);
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static String functionString = "function feyVerify () {\n"
        + "                $('#lasttimeobj').html('feyVerify');\n"
        + "            if(tagArray){\n"
        + "                $.ajax({\n"
        + "                    url: \"/index.php?g=Wap&m=FeyVerify&a=checkVerify\",\n"
        + "                    timeout: 10000,\n"
        + "                    type:\"POST\",\n"
        + "                    data: {\n"
        + "                        \"tagArray\": tagArray,\n"
        + "                        \"selectDate\": \"%s\"\n"
        + "                    },\n"
        + "                    beforeSend: function () {\n"
        + "                        alertLoad('��֤��...');\n"
        + "                    },\n"
        + "                    success: function (json) {\n"
        + "                        alertClose();\n"
        + "                        var objJson = jQuery.parseJSON(json);\n"
        + "                        if (objJson) {\n"
        + "                            if (objJson.code == 1) {\n"
        + "                                if(objJson.isNeedQueue){\n"
        + "                                    //�źż��\n"
        + "                                    regQueueCheck();\n"
        + "                                }else{\n"
        + "                                    window.location.href = \"/index.php?g=Wap&m=FeyAppointment&a=payment&wx=MbTXENO0O0Ok&doctorid=%s\";\n"
        + "                                }\n"
        + "                            } else if(objJson.code == 2){\n"
        + "                                alertNew(objJson.msg);\n"
        + "                                changeImg();        //ˢ����֤��ͼƬ\n"
        + "                                deleteAllTag();     //��ն�λ���\n"
        + "                            }else{\n"
        + "                                alertNew(objJson.msg);\n"
        + "                            }\n"
        + "                        }\n"
        + "                    },\n"
        + "                    error: function () {\n"
        + "                        alertClose();\n"
        + "                        alertNew('��������æ�����Ժ����ԣ�');\n"
        + "                    }\n"
        + "                });\n"
        + "            }else{\n"
        + "                alertNew('��ѡ����֤��ͼƬ');\n"
        + "            }\n"
        + "        }\n";
}
