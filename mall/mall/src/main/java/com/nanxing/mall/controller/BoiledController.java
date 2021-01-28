package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.model.pojo.MallBoiled;
import com.nanxing.mall.model.request.AddMallBoiledReq;
import com.nanxing.mall.model.request.UpdateMallBoiledReq;
import com.nanxing.mall.service.BoiledService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags="煮药价格控制")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/boiled/")
public class BoiledController {
    @Autowired
    BoiledService boiledService;

    @ApiOperation("添加")
    @PostMapping("add")
    public ApiRestResponse add(@RequestBody AddMallBoiledReq addMallBoiledReq){
        boiledService.add(addMallBoiledReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("删除")
    @GetMapping("delete")
    public ApiRestResponse delete(@RequestParam Integer id){
        boiledService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("修改")
    @PostMapping("update")
    public ApiRestResponse add(@RequestBody UpdateMallBoiledReq updateMallBoiledReq){
        MallBoiled boiled=new MallBoiled();
        BeanUtils.copyProperties(updateMallBoiledReq,boiled);
        boiledService.update(boiled);
        return ApiRestResponse.success();
    }

    @ApiOperation("获取列表")
    @GetMapping("list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo boiledList=boiledService.list(pageNum,pageSize);
        return ApiRestResponse.success(boiledList);
    }


}
