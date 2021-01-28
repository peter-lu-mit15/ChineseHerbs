package com.nanxing.mall.model.request;

public class AddCartReq {
    private Integer productId;
    private Integer count;
    private Integer handle=1;
    private Integer formula=1;
    private String remarks;
    private Integer userId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getHandle() {
        return handle;
    }

    public void setHandle(Integer handle) {
        this.handle = handle;
    }

    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AddCartReq{" +
                "productId=" + productId +
                ", count=" + count +
                ", handle=" + handle +
                ", formula=" + formula +
                ", remarks='" + remarks + '\'' +
                ", userId=" + userId +
                '}';
    }
}
