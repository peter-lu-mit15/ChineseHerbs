package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.request.AddProductReq;
import com.nanxing.mall.model.request.ProductListReq;

import java.util.List;

/**
 * @description: 商品Service
 * @author: Mr.Tang
 * @create: 2021/1/15 13:17
 **/
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer status);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize,String keyword);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);

    List<String> getCharacterByPyin(String Pyin);
}
