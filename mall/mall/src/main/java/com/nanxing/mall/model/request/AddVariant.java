package com.nanxing.mall.model.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class AddVariant {

    @NotNull(message="处理名称不能为空")
    private String name;

    @NotNull(message="处理商品不能为空")
    private Integer productId;

    @NotNull(message="处理价格不能为空")
    private Integer price;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


}