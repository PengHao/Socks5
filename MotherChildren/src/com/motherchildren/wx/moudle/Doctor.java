package com.motherchildren.wx.moudle;

/**
 * Created by penghao on 2017/5/26.
 * Copyright ? 2017�� Alibaba. All rights reserved.
 */
public class Doctor {
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorDesc(String doctorDesc) {
        this.doctorDesc = doctorDesc;
    }
    public String getDoctorDesc() {
        return doctorDesc;
    }

    public void setDoctorHead(String doctorHead) {
        this.doctorHead = doctorHead;
    }
    public String getDoctorHead() {
        return doctorHead;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }
    public String getDepartmentDesc(){
        return departmentDesc;
    }

    public void setDepartmentRowId(String departmentRowId) {
        this.departmentRowId = departmentRowId;
    }
    public String getDepartmentRowId(){
        return departmentRowId;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }
    public String getDocCode() {
        return docCode;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }
    public String getSections() {
        return sections;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getProfession() {
        return profession;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getClassId() {
        return classId;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkUrl() {
        return linkUrl;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getRefreshTime() {
        return refreshTime;
    }
    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Integer getLastCount() {
        return lastCount;
    }
    public void setLastCount(Integer lastCount) {
        this.lastCount = lastCount;
    }

    String refreshTime;
    Integer lastCount;

    String time;

    //����id
    Long id;
    //ҽ��id
    String doctorId;
    //ҽ������
    String doctorName;
    //ҽ������
    String doctorDesc;
    //ҽ��ͷ��
    String doctorHead;
    //��������
    String departmentDesc;
    //���� id
    String departmentRowId;
    //�ĵ�code
    String docCode;
    //ҽ��ͷ��
    String sections;
    //ҽ��ͷ��
    String profession;
    //����id
    String classId;
    //����ʱ��
    String updateTime;
    //link url
    String linkUrl;


}
