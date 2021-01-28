package com.nanxing.mall.model.request;

import javax.validation.constraints.NotNull;

public class AddMallBoiledReq {

    @NotNull(message = "名称不能为空")
    private Integer count;

    @NotNull(message = "价格不能为空")
    private Integer price;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}