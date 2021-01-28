package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.request.ProductListReq;
import com.nanxing.mall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 前台商品Controller
 * @author: Mr.Tang
 * @create: 2021/1/15 23:34
 **/

@Api(tags="前台商品")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @ApiOperation("商品详情")
    @GetMapping("product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product=productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("商品列表")
    @PostMapping("productByLogin/list")
    public ApiRestResponse list(@RequestBody ProductListReq productListReq){
        PageInfo list=productService.list(productListReq);
        return ApiRestResponse.success(list);
    }

    @ApiOperation("根据字母返回汉字")
    @PostMapping("product/getCharacterByPyin")
    public ApiRestResponse getCharacterByPyin(@RequestParam String pyin){
        List<String> characters = productService.getCharacterByPyin(pyin);
        return ApiRestResponse.success(characters);
    }

}
