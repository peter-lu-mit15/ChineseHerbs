package com.nanxing.mall.model.query;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity implements Serializable {

    @Excel(name = "药材名称",width =20 ,orderNum = "1")
    private String name;
    @Excel(name = "介绍信息",width = 20,orderNum = "2")
    private String detail;
    @Excel(name = "单价", orderNum = "3")
    private Integer price;
    @Excel(name = "库存", orderNum = "4")
    private Integer stock;
    @Excel(name = "状态", orderNum = "5")
    private Integer status;
    @Excel(name = "拼音", orderNum = "6")
    private String pyName;
    @Excel(name = "创建时间", width = 20,exportFormat = "yyyy-MM-dd",importFormat="yyyy-MM-dd",orderNum = "7")
    private Date createTime;
    @Excel(name = "更新时间", width = 20, exportFormat = "yyyy-MM-dd",importFormat="yyyy-MM-dd",orderNum = "8")
    private Date updateTime;
    @Excel(name = "变量1", orderNum = "9")
    private String variant1;
    @Excel(name = "变量2", orderNum = "10")
    private String variant2;
    @Excel(name = "变量3", orderNum = "11")
    private String variant3;
    @Excel(name = "变量4", orderNum = "12")
    private String variant4;
    @Excel(name = "变量5", orderNum = "13")
    private String variant5;
    @Excel(name = "变量1价格", orderNum = "14")
    private Integer variantPrice1;
    @Excel(name = "变量2价格", orderNum = "15")
    private Integer variantPrice2;
    @Excel(name = "变量3价格", orderNum = "16")
    private Integer variantPrice3;
    @Excel(name = "变量4价格", orderNum = "17")
    private Integer variantPrice4;
    @Excel(name = "变量5价格", orderNum = "18")
    private Integer variantPrice5;


}