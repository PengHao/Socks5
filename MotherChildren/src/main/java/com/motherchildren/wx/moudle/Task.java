package com.motherchildren.wx.moudle;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by penghao on 2017/5/12.
 * Copyright ? 2017Äê Alibaba. All rights reserved.
 */
public class Task {

    public Long id;
    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String cardId = "-1";
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    Long doctorId = -1L;
    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String doctorTime = "-1";
    public String getDoctorTime() {
        return doctorTime;
    }
    public void setDoctorTime(String doctorTime) {
        this.doctorTime = doctorTime;
    }


    String classId = "-1";
    public String getClassId() {
        return classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }

    static public String dateString() {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd");
        String ctime = formatter.format(new Date());
        return ctime;
    }


    //
    String triedTime;
    public String getTriedTime() {
        return triedTime;
    }
    public void setTriedTime(String triedTime) {
        this.triedTime = triedTime;
    }

    public void reset() {
        doctorId = -1L;
        classId = "-1";
        doctorTime = "-1";
    }
}