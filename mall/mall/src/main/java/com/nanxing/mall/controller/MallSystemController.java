package com.nanxing.mall.controller;

import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.model.pojo.MallSystem;
import com.nanxing.mall.model.request.UpdateMallSystem;
import com.nanxing.mall.service.MallSystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description: 系统参数控制器
 * @author: Mr.Tang
 * @create: 2021/1/22 11:48
 **/

@Api(tags = "系统参数设置")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin/system/")
public class MallSystemController {
    @Autowired
    MallSystemService mallSystemService;

    @ApiOperation("更新系统设置")
    @PostMapping("update")
    public ApiRestResponse update(@RequestBody @Valid UpdateMallSystem updateMallSystem){
        MallSystem mallSystem=new MallSystem();
        BeanUtils.copyProperties(updateMallSystem,mallSystem);
        mallSystemService.update(mallSystem);
        return ApiRestResponse.success();
    }

    @ApiOperation("系统设置数据")
    @GetMapping("topOne")
    public ApiRestResponse detail(){
        MallSystem mallSystem=mallSystemService.detail();
        return ApiRestResponse.success(mallSystem);
    }

    @ApiOperation("获取配方数量")
    @GetMapping("boiledCount")
    public ApiRestResponse boiledCount(){
        MallSystem mallSystem=mallSystemService.detail();
        mallSystem.setSysAuthorization("");
        mallSystem.setSysEmail("");
        mallSystem.setId(0);
        return ApiRestResponse.success(mallSystem);
    }

}
