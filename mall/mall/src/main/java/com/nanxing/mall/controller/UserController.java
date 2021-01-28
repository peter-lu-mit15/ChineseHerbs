package com.nanxing.mall.controller;

import com.mysql.cj.util.StringUtils;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.request.UpdateUserReq;
import com.nanxing.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 用户控制器
 * @author: Mr.Tang
 * @create: 2021/1/14 19:59
 **/

@Api(tags = "用户信息模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public User personalPage(){
        return userService.getUser(1);
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     * @throws MallException
     */
    @ApiOperation("用户注册")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session) throws MallException {
        if(StringUtils.isNullOrEmpty(username)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isNullOrEmpty(password)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于6位
        if(password.length()<6){
            return ApiRestResponse.error(MallExceptionEnum.NEED_TOO_SHORT);
        }
        userService.register(username,password);
        User user=userService.login(username,password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER,user);
        return ApiRestResponse.success(user);
    }




    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ApiRestResponse login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws MallException {
        if(StringUtils.isNullOrEmpty(username)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isNullOrEmpty(password)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }

        User user=userService.login(username,password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER,user);

        return ApiRestResponse.success(user);
    }

    /**
     * 用户完善信息
     * @param session
     * @param updateUserReq
     * @return
     * @throws MallException
     */
    @ApiOperation("用户完善信息")
    @PostMapping("/user/update")
    public ApiRestResponse updateUserInfo(HttpSession session,@Valid @RequestBody UpdateUserReq updateUserReq) throws MallException {
        User currentUser=userService.getUser(updateUserReq.getId());
        if(currentUser==null){
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user=new User();
        BeanUtils.copyProperties(updateUserReq,user);
        user.setId(currentUser.getId());
        userService.update(user);
        return ApiRestResponse.success();
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @ApiOperation("用户退出登录")
    @PostMapping("/user/logout")
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @ApiOperation("管理员登录")
    @PostMapping("/adminLogin")
    public ApiRestResponse adminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws MallException {
        if(StringUtils.isNullOrEmpty(username)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isNullOrEmpty(password)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }

        User user=userService.login(username,password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //保存用户信息时，不保存密码
            user.setPassword(null);
            Map result=new HashMap();
            result.put("data",user);
            result.put("token", UUID.randomUUID().toString());
            session.setAttribute(Constant.MALL_USER,user);
            return ApiRestResponse.success(result);
        }else{
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

}
