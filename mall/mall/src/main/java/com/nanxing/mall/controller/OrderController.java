package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.model.request.CreateOrderReq;
import com.nanxing.mall.model.vo.OrderVO;
import com.nanxing.mall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 订单管理
 * @author: Mr.Tang
 * @create: 2021/1/16 12:00
 **/
@Api(tags = "订单管理")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("order/")
public class OrderController {

    @Autowired
    OrderService orderService;


    @ApiOperation("创建订单")
    @PostMapping("create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo=orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @ApiOperation("订单详情")
    @GetMapping("detail")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO=orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @ApiOperation("前台订单列表")
    @GetMapping("list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam Integer userId){
        PageInfo pageInfo=orderService.listForCustomer(pageNum,pageSize,userId);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("订单取消")
    @GetMapping("cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("生成支付二维码")
    @GetMapping("qrcode")
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress=orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @CrossOrigin
    @ApiOperation("后台订单列表")
    @PostMapping("listForAdmin")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam("keyword") String keyword){
        PageInfo pageInfo=orderService.listForAdmin(pageNum,pageSize,keyword);
        return ApiRestResponse.success(pageInfo);
    }


}
