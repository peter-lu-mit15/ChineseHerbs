package com.nanxing.mall.controller;

import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.filter.UserFilter;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.request.AddCartReq;
import com.nanxing.mall.model.request.CreateOrderReq;
import com.nanxing.mall.model.vo.CartVO;
import com.nanxing.mall.service.CartService;
import com.nanxing.mall.service.OrderService;
import com.nanxing.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 购物车Controller
 * @author: Mr.Tang
 * @create: 2021/1/16 9:15
 **/
@Api(tags = "购物车")
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;


    @ApiOperation("添加配方中的商品到购物车")
    @PostMapping("/addList")
    public ApiRestResponse addList(@RequestBody List<AddCartReq> addCartReqList){
        //将商品全部添加进购物车
        int userId=0;
        if(addCartReqList.size()>0)
            userId=addCartReqList.get(0).getUserId();
        //获取用户信息
        User user=userService.getUser(userId);
        if(user==null){
            return ApiRestResponse.error(MallExceptionEnum.NOT_USER);
        }

        for(int i=0;i<addCartReqList.size();i++){
            AddCartReq addCartReq=addCartReqList.get(i);
            cartService.add(user.getId(),addCartReq.getProductId(),addCartReq.getCount(),addCartReq.getHandle(),addCartReq.getFormula());
        }

        //生成订单
        CreateOrderReq createOrderReq=new CreateOrderReq();
        String address=user.getCity()+"-"+user.getAddress();
        createOrderReq.setReceiverAddress(address);
        createOrderReq.setReceiverMobile(user.getPhone());
        createOrderReq.setUserId(user.getId());
        createOrderReq.setReceiverName(user.getFirstName()+""+user.getLastName());
        if(addCartReqList.size()>0)
        createOrderReq.setRemarks(addCartReqList.get(0).getRemarks());
        String orderNo=orderService.create(createOrderReq);

        return ApiRestResponse.success(orderNo);
    }

//    @ApiOperation("添加商品到购物车")
//    @PostMapping("/add")
//    public ApiRestResponse add(@RequestParam Integer productId,Integer count){
//        List<CartVO> cartOVList=cartService.add(UserFilter.currentUser.getId(),productId,count);
//        return ApiRestResponse.success(cartOVList);
//    }

    @ApiOperation("购物车列表")
    @PostMapping("/list")
    public ApiRestResponse list(@RequestParam Integer userId){
        //内部获取用户ID，防止横向越权
        List<CartVO> cartOVList=cartService.list(userId);

        return ApiRestResponse.success(cartOVList);
    }

    @ApiOperation("更新购物车")
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId,@RequestParam Integer count,@RequestParam Integer userId){

        List<CartVO> cartOVList=cartService.update(userId,productId,count);

        return ApiRestResponse.success(cartOVList);
    }

    @ApiOperation("删除购物车")
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam Integer productId,@RequestParam Integer userId){
        List<CartVO> cartOVList=cartService.delete(userId,productId);

        return ApiRestResponse.success(cartOVList);
    }

    @ApiOperation("选择/不选择购物车的某商品")
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId,@RequestParam Integer selected,@RequestParam Integer userId){
        List<CartVO> cartOVList=cartService.selectOrNot(userId,productId,selected);

        return ApiRestResponse.success(cartOVList);
    }

    @ApiOperation("全选择/全不选择购物车的某商品")
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected,@RequestParam Integer userId){
        List<CartVO> cartOVList=cartService.selectAllOrNotAll(userId,selected);

        return ApiRestResponse.success(cartOVList);
    }
}
