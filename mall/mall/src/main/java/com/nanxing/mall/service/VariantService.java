package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.request.AddCategoryReq;
import com.nanxing.mall.model.request.AddVariant;
import com.nanxing.mall.model.request.UpdateVariant;
import com.nanxing.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * @description: 药材处理Service
 * @author: Mr.Tang
 * @create: 2021/1/15 13:17
 **/
public interface VariantService {
    List<Variant> add(AddVariant addVariant);

    List<Variant> update(Variant updateVariant);

    List<Variant> delete(Integer id);

    PageInfo list(Integer pageNum, Integer pageSize);

    PageInfo listByProduct(Integer pageNum, Integer pageSize,Integer productId);

}
