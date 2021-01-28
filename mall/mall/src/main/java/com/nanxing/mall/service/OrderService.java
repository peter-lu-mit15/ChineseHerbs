package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.request.CreateOrderReq;
import com.nanxing.mall.model.vo.OrderVO;

/**
 * @description: 订单Service
 * @author: Mr.Tang
 * @create: 2021/1/16 9:26
 **/
public interface OrderService {


    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    OrderVO detailForEmail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize,Integer userId);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize,String orderNo);

    void cancel(String orderNo);

    String qrcode(String orderNo);
}
