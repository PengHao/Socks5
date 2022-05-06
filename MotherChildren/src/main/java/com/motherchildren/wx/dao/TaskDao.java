package com.motherchildren.wx.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.motherchildren.wx.moudle.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by penghao on 2017/5/23.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */

@Component
public class TaskDao {

    @Resource
    private JdbcTemplate serverUserTemplate;

    public List<Task> getTasks(String cardId) {
        List<Task> tasks = new ArrayList<Task>();
        String sql = String.format("select * from MotherChildrenTask WHERE card_id = %s", cardId);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            Task task = new Task();
            task.setDoctorId(Long.valueOf((map4dept.get("doctor_id")).toString()));
            task.setClassId(((String)map4dept.get("class_id")));
            task.setDoctorTime(((String)map4dept.get("doctor_time")));
            task.setCardId(((String) map4dept.get("card_id")));
            task.setTriedTime(((String) map4dept.get("tried_time")));
            tasks.add(task);
        }
        return tasks;
    }

    public Task getTask(String cardId, Long doctorId) {
        String sql = String.format("select * from MotherChildrenTask WHERE card_id = %s AND doctor_id = %d", cardId, doctorId);
        List list = serverUserTemplate.queryForList(sql);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map4dept = (Map) iterator.next();
            Task task = new Task();
            task.setDoctorId(Long.valueOf((map4dept.get("doctor_id")).toString()));
            task.setClassId(((String)map4dept.get("class_id")));
            task.setDoctorTime(((String)map4dept.get("doctor_time")));
            task.setCardId(((String) map4dept.get("card_id")));
            task.setTriedTime(((String) map4dept.get("tried_time")));
            return task;
        }
        return null;
    }

    public void update(Task task) {
        String sql = String.format("update MotherChildrenTask task SET "+
            "task.card_id = '"+ task.getCardId() +
            "', task.doctor_id = '" + task.getDoctorId() +
            "', task.class_id = '"+ task.getClassId() +
            "', task.doctor_time = '"+ task.getDoctorTime() +
            "', task.tried_time = '"+ task.getTriedTime() +
            "' where task.id = " + task.getId());
        serverUserTemplate.execute(sql);
    }

    public void insert(Task task) {
        String sql = String.format("insert into MotherChildrenTask (id, card_id, doctor_id, class_id, doctor_time) values (null, '%s', '%s', '%s', '%d')", task.getCardId(), task.getDoctorId(), task.getClassId(), task.getDoctorId());
        serverUserTemplate.execute(sql);
    }

    public void delete(String cardId, Long doctorId){
        String sql = String.format("delete from MotherChildrenTask where `card_id` = '%s' AND `doctor_id` = '%d'", cardId, doctorId);
        serverUserTemplate.execute(sql);
    }
}
