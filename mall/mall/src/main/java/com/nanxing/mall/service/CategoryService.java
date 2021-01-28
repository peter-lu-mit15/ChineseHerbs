package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.request.AddCategoryReq;
import com.nanxing.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * @description: 分类目录Service
 * @author: Mr.Tang
 * @create: 2021/1/15 13:17
 **/
public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
