package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.request.AddCategoryReq;
import com.nanxing.mall.model.request.AddVariant;
import com.nanxing.mall.model.request.UpdateCategoryReq;
import com.nanxing.mall.model.request.UpdateVariant;
import com.nanxing.mall.model.vo.CategoryVO;
import com.nanxing.mall.service.CategoryService;
import com.nanxing.mall.service.UserService;
import com.nanxing.mall.service.VariantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @description: 药材处理Controller
 * @author: Mr.Tang
 * @create: 2021/1/15 12:38
 **/
@Api(tags = "药材处理")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("admin/variant/")
public class VariantController {

    @Autowired
    VariantService variantService;


    @ApiOperation("药材处理列表")
    @GetMapping("list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo=variantService.list(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("工具药材ID获取药材处理列表")
    @GetMapping("listByProduct")
    public ApiRestResponse listByProduct(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam Integer productId){
        PageInfo pageInfo=variantService.listByProduct(pageNum,pageSize,productId);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("添加药材处理")
    @PostMapping("add")
    public ApiRestResponse add(@Valid @RequestBody AddVariant addVariant){
        List<Variant> variantList= variantService.add(addVariant);
        return ApiRestResponse.success(variantList);
    }


    @ApiOperation("更新药材处理")
    @PostMapping("update")
    public ApiRestResponse update(@Valid @RequestBody UpdateVariant updateVariant){
        Variant variant=new Variant();
        BeanUtils.copyProperties(updateVariant,variant);
        List<Variant> variantList= variantService.update(variant);
        return ApiRestResponse.success(variantList);
    }

    @ApiOperation("删除药材处理")
    @GetMapping("delete")
    public ApiRestResponse delete(Integer id){
        List<Variant> variantList=variantService.delete(id);
        return ApiRestResponse.success(variantList);
    }

}
