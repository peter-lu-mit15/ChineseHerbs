package com.nanxing.mall.model.request;

import javax.validation.constraints.NotNull;

public class UpdateMallSystem {
    private Integer id;

    @NotNull(message = "系统邮箱不能为空")
    private String sysEmail;

    @NotNull(message = "系统邮箱授权码不能为空")
    private String sysAuthorization;

    @NotNull(message = "最大邮箱设置数不能为空")
    private Integer graspCount;

    @NotNull(message = "煮药下限价格不能为空")
    private Integer boidedMin;

    @NotNull(message = "煮药上限价格不能为空")
    private Integer boidedMax;

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

    public Integer getGraspCount() {
        return graspCount;
    }

    public void setGraspCount(Integer graspCount) {
        this.graspCount = graspCount;
    }

    public Integer getBoidedMax() {
        return boidedMax;
    }

    public void setBoidedMax(Integer boidedMax) {
        this.boidedMax = boidedMax;
    }

    public Integer getBoidedMin() {
        return boidedMin;
    }

    public void setBoidedMin(Integer boidedMin) {
        this.boidedMin = boidedMin;
    }
}