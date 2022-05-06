package com.motherchildren.wx.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.motherchildren.wx.moudle.Doctor;
import com.sun.javadoc.Doc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/26.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
@Component
public class DoctorDao {

    @Resource
    private JdbcTemplate serverUserTemplate;

    public Doctor getDoctorByDoctorId(String doctorId) {
        String sql = String.format("select * from MotherChildrenDoctor WHERE `doctorid` = '%s'", doctorId);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            return parserDoctor(map4dept);
        }
        return null;
    }

    public Doctor getDoctorById(Long id) {
        String sql = String.format("select * from MotherChildrenDoctor WHERE `id` = '%d'", id);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            return parserDoctor(map4dept);
        }
        return null;

    }

    //public Doctor getDoctorByDoctorCode(String doctorCode) {
    //    String sql = String.format("select * from MotherChildrenDoctor WHERE `doc_code` = '%s'", doctorCode);
    //    List list = serverUserTemplate.queryForList(sql);
    //    Iterator iterator = list.iterator();
    //    while (iterator.hasNext()) {
    //        Map map4dept = (Map) iterator.next();
    //        return parserDoctor(map4dept);
    //    }
    //    return null;
    //}

    private Doctor parserDoctor(Map map4dept) {
        Doctor doctor = new Doctor();
        doctor.setId(Long.valueOf(map4dept.get("id").toString()));
        doctor.setDoctorId(((String) map4dept.get("doctorid")));
        doctor.setDoctorName(((String) map4dept.get("doctor_name")));
        doctor.setDoctorDesc(((String) map4dept.get("doctor_desc")));
        doctor.setDoctorHead(((String) map4dept.get("doctor_head")));
        doctor.setDepartmentDesc(((String) map4dept.get("department_desc")));
        doctor.setDepartmentRowId(((String) map4dept.get("department_row_id")));
        doctor.setDocCode(((String) map4dept.get("doc_code")));
        doctor.setSections(((String) map4dept.get("sections")));
        doctor.setProfession(((String) map4dept.get("profession")));
        doctor.setClassId(((String) map4dept.get("classid")));
        doctor.setUpdateTime(((String) map4dept.get("updata_time")));
        doctor.setLinkUrl(((String) map4dept.get("url_link")));
        doctor.setTime(((String) map4dept.get("time")));
        return doctor;
    }


    public void updateDoctor(Doctor doctor) {
        String sql = "update MotherChildrenDoctor doctor SET " +
            "doctor.doc_code = '"+ doctor.getDocCode() +
            "', doctor.doctor_desc = '" + doctor.getDoctorDesc() +
            "', doctor.doctor_name = '" + doctor.getDoctorName() +
            "', doctor.doctor_head = '"+ doctor.getDoctorHead() +
            "', doctor.department_desc = '"+ doctor.getDepartmentDesc() +
            "', doctor.department_row_id = '"+ doctor.getDepartmentRowId() +
            "', doctor.doc_code = '"+ doctor.getDocCode() +
            "', doctor.sections = '"+ doctor.getSections() +
            "', doctor.profession = '"+ doctor.getProfession() +
            "', doctor.classid = '"+ doctor.getClassId() +
            "', doctor.updata_time = '"+ doctor.getUpdateTime() +
            "', doctor.url_link = '"+ doctor.getLinkUrl() +
            "', doctor.time = '"+ doctor.getTime() +
            "' where doctor.doctorid = " + doctor.getDoctorId();
        serverUserTemplate.execute(sql);
    }

    public void insertDoctor(Doctor doctor) {
        String sql = String.format("insert into MotherChildrenDoctor "+
            "( id, doctorid, doctor_name, doctor_desc, doctor_head, department_desc, department_row_id, doc_code, sections, profession, classid, updata_time, url_link, time) values (null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            doctor.getDoctorId(), doctor.getDoctorName(), doctor.getDoctorDesc(), doctor.getDoctorHead(), doctor.getDepartmentDesc(), doctor.getDepartmentRowId(), doctor.getDocCode(), doctor.getSections(), doctor.getProfession(), doctor.getClassId(), doctor.getUpdateTime(), doctor.getLinkUrl(), doctor.getTime());
        serverUserTemplate.execute(sql);
    }
}
