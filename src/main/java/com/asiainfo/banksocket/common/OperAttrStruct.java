package com.asiainfo.banksocket.common;

//操作人属性
public class OperAttrStruct {
    private  Integer staffId;//操作工号标识
    private  Integer operOrgId;//操作组织标识
    private  String operTime;//操作时间 (可为空)
    private  Integer operPost;//操作岗位 (可为空)
    private  String operServiceId;//业务流水标识 (可为空)
    private  Integer lanId;//本地网标识 (可为空)

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getOperOrgId() {
        return operOrgId;
    }

    public void setOperOrgId(Integer operOrgId) {
        this.operOrgId = operOrgId;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public Integer getOperPost() {
        return operPost;
    }

    public void setOperPost(Integer operPost) {
        this.operPost = operPost;
    }

    public String getOperServiceId() {
        return operServiceId;
    }

    public void setOperServiceId(String operServiceId) {
        this.operServiceId = operServiceId;
    }

    public Integer getLanId() {
        return lanId;
    }

    public void setLanId(Integer lanId) {
        this.lanId = lanId;
    }
}
