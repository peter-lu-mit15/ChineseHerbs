package com.nanxing.mall.model.query;

import java.util.List;

/**
 * @description: 查询商品列表的query
 * @author: Mr.Tang
 * @create: 2021/1/15 23:43
 **/

public class ProductListQuery  {
    private String keyword;
    private String pyName;
    private String firstName;
    private List<Integer> categoryIds;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
