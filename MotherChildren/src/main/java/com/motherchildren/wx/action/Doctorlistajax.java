package com.motherchildren.wx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.motherchildren.wx.moudle.Card;
import com.motherchildren.wx.moudle.Doctor;
import com.motherchildren.wx.moudle.Session;
import com.motherchildren.wx.moudle.Task;
import com.wolfpeng.core.http.HttpConnection;
import com.wolfpeng.core.http.HttpData;
import com.wolfpeng.core.util.Utils;
import com.wolfpeng.server.ProxyServer;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/18.
 * Copyright ? 2017�� Alibaba. All rights reserved.
 */

@Component
public class Doctorlistajax extends EmptyAction{

    HttpData makeRequest(Session session, Card card, List<Task> tasks) {
        Map<String, List<Task>> taskMap = new HashMap<String, List<Task>>();
        for (Task task : tasks) {
            List<Task> list = taskMap.get(task.getClassId());
            if (list == null) {
                list = new ArrayList<Task>();
                taskMap.put(task.getClassId(), list);
            }
            list.add(task);
        }

        for (Map.Entry<String, List<Task>> entry : taskMap.entrySet()) {
            String classId = entry.getKey();
            String fullHeader = getFullHeader(headerTemplate, session);
            String _15DayString = get15DayLaterString();
            fullHeader = fullHeader.replace("&date=%s", "&date="+_15DayString);
            fullHeader = fullHeader.replace("&classid=%s&", "&classid="+classId+"&");
            HttpData response = sendHttpData(fullHeader);
            List<Doctor> doctorList = parserDoctors(response, classId, false);
            for (Doctor d : doctorList) {
                if (d.getLastCount() <= 0) {
                    continue;
                }
                for (Task task : entry.getValue()) {
                    Doctor doctor = doctorDao.getDoctorById(task.getDoctorId());
                    if ( d.getDoctorId().equals(doctor.getDoctorId())) {
                        Doctordetail doctordetail = ProxyServer.getBean(Doctordetail.class);
                        task.setDoctorTime(doctor.getTime());
                        return doctordetail.makeRequest(session, card, task);
                    }
                }
            }
        }

        return null;
    }

    private Doctor parserDoctor(JSONObject doctorJson, String classid, String time, Boolean updateToDatabase) {
        String doctorid = doctorJson.getString("doctorid");
        Doctor doctor = updateToDatabase ? doctorDao.getDoctorByDoctorId(doctorid) : null;
        String updata_ts = doctorJson.getString("updata_ts");
        Boolean isUpdate = doctor != null;

        if (isUpdate && doctor.getUpdateTime().equals(updata_ts)) {
            return doctor;
        }

        if (!isUpdate) {
            doctor = new Doctor();
        }

        doctor.setDepartmentDesc(doctorJson.getString("DepDesc"));
        doctor.setDepartmentRowId(doctorJson.getString("DepRowid"));
        doctor.setDoctorName(doctorJson.getString("MarkDesc"));
        doctor.setDoctorDesc(doctorJson.getString("description"));
        doctor.setDocCode(doctorJson.getString("doccode"));
        doctor.setDoctorHead(doctorJson.getString("doctorhead"));
        doctor.setDoctorId(doctorJson.getString("doctorid"));
        doctor.setLinkUrl(doctorJson.getString("linkUrl"));
        doctor.setClassId(classid);
        doctor.setProfession(doctorJson.getString("profession"));
        doctor.setLastCount(Integer.valueOf(doctorJson.getString("SeqNoStrLast")));

        doctor.setSections(doctorJson.getString("sections"));
        doctor.setUpdateTime(doctorJson.getString("updata_ts"));
        doctor.setTime(time);

        if (updateToDatabase) {
            if (isUpdate) {
                doctorDao.updateDoctor(doctor);
            } else {
                doctorDao.insertDoctor(doctor);
            }
        }
        return doctor;
    }

    private List<Doctor> parserDoctors(HttpData response, String classid, Boolean updateToDatabase) {
        String body = getDecodeBodyString(response);
        JSONObject object = JSONObject.parseObject(body);
        List<Doctor> doctorList = new ArrayList<Doctor>();
        if (!object.get("code").equals(1)) {
            return doctorList;
        }
        JSONObject data = object.getJSONObject("data");
        for (String time : data.keySet()) {
            JSONArray doctors = data.getJSONArray(time);
            for (Integer i = 0; i < doctors.size(); ++i) {
                Doctor doctor = parserDoctor(doctors.getJSONObject(i), classid, time, updateToDatabase);
                doctorList.add(doctor);
            }
        }
        return doctorList;
    }

    @Override
    public HttpData processRequest(HttpConnection connection) {
        Utils.log("processRequest");
        return null;
    }

    @Override
    public HttpData processResponse(HttpConnection connection) {
        try {
            //����ҽ���б�����
            super.processResponse(connection);
            parserDoctors(connection.getResponse(), connection.getRequest().httpHead.getGetProperty("classid"), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static String headerTemplate = "GET /index.php?g=Wap&m=FeyAppointment&a=doctorlistajax&wx=MbTXENO0O0Ok&classid=%s"
        + "&date=%s HTTP/1.1\r\n"
        + "Host: wx.motherchildren.com\r\n"
        + "Accept-Encoding: gzip, deflate\r\n"
        + "Cookie: card_list_url=%2Findex.php%3Fg%3DWap%26m%3DFeyAppointment%26a%3Dcardlist%26wx%3DMbTXENO0O0Ok; "
        + "PHPSESSID=%s\r\n"
        + "Connection: keep-alive\r\n"
        + "Accept: */*\r\n"
        + "Task-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like "
        + "Gecko) Mobile/14E304 MicroMessenger/6.5.7 NetType/WIFI Language/zh_CN\r\n"
        + "Referer: http://wx.motherchildren.com/index"
        + ".php?g=Wap&m=FeyAppointment&a=doctorlist&wx=MbTXENO0O0Ok&classid=%s&dateString=%s\r\r\n"
        + "Accept-Language: zh-cn\r\n"
        + "X-Requested-With: XMLHttpRequest\r\n"
        + "\r\n";
}
