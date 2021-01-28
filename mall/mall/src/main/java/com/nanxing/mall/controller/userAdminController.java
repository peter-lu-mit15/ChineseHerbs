package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.request.UpdateProductReq;
import com.nanxing.mall.model.request.UpdateUserReq;
import com.nanxing.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description: 用户后台管理控制器
 * @author: Mr.Tang
 * @create: 2021/1/18 22:55
 **/

@Api(tags = "用户后台管理模块")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("admin/")
public class userAdminController {
    @Autowired
    UserService userService;

    @ApiOperation("后台用户列表")
    @PostMapping("user/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String keyword){
        PageInfo pageInfo=userService.listForAdmin(pageNum,pageSize,keyword);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("后台用户删除")
    @PostMapping("user/delete")
    public ApiRestResponse deleteUser(@RequestParam Integer id){
        userService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台更新用户信息")
    @PostMapping("user/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateUserReq updateUserReq){
        User user=new User();
        BeanUtils.copyProperties(updateUserReq,user);
        userService.update(user);
        return ApiRestResponse.success();
    }
}
