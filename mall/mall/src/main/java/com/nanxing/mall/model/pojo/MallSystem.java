package com.nanxing.mall.model.pojo;

public class MallSystem {
    private Integer id;

    private String sysEmail;

    private String sysAuthorization;

    private Integer boidedMin;

    private Integer boidedMax;

    private Integer graspCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysEmail() {
        return sysEmail;
    }

    public void setSysEmail(String sysEmail) {
        this.sysEmail = sysEmail == null ? null : sysEmail.trim();
    }

    public String getSysAuthorization() {
        return sysAuthorization;
    }

    public void setSysAuthorization(String sysAuthorization) {
        this.sysAuthorization = sysAuthorization == null ? null : sysAuthorization.trim();
    }

    public Integer getBoidedMin() {
        return boidedMin;
    }

    public void setBoidedMin(Integer boidedMin) {
        this.boidedMin = boidedMin;
    }

    public Integer getBoidedMax() {
        return boidedMax;
    }

    public void setBoidedMax(Integer boidedMax) {
        this.boidedMax = boidedMax;
    }

    public Integer getGraspCount() {
        return graspCount;
    }

    public void setGraspCount(Integer graspCount) {
        this.graspCount = graspCount;
    }
}