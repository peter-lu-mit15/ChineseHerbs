package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.filter.UserFilter;
import com.nanxing.mall.model.dao.*;
import com.nanxing.mall.model.pojo.*;
import com.nanxing.mall.model.request.CreateOrderReq;
import com.nanxing.mall.model.vo.CartVO;
import com.nanxing.mall.model.vo.OrderItemVO;
import com.nanxing.mall.model.vo.OrderVO;
import com.nanxing.mall.service.CartService;
import com.nanxing.mall.service.OrderService;
import com.nanxing.mall.util.OrderCodeFactory;
import com.nanxing.mall.util.QRCodeGenerator;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单Serverce 实现类
 * @author: Mr.Tang
 * @create: 2021/1/16 12:34
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VariantMapper variantMapper;
    @Autowired
    MallBoiledMapper mallBoiledMapper;

    @Value("${file.upload.ip}")
    String ip;


    //添加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){
        //拿到用户ID
        Integer userId= createOrderReq.getUserId();

        //从购物车查找已选勾的商品
        List<CartVO> cartVOList=cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp=new ArrayList<>();
        for(CartVO vo:cartVOList){
            if(vo.getSelected().equals(Constant.Cart.CHECKED)){
                cartVOListTemp.add(vo);
            }
        }
        cartVOList=cartVOListTemp;

        //如果购物车已勾选的为空，报错
        if(CollectionUtils.isEmpty(cartVOList)){
            throw new MallException(MallExceptionEnum.CART_ENOUGH);
        }

        //判断商品是否存在，上下架状态，库存
        validSaleStatusAndStock(cartVOList);

        //把购物车对象转换为订单ITEM对象
        List<OrderItem> orderItemList=cartVOListToOrderItemList(cartVOList);

        //扣除库存
        for (OrderItem item:orderItemList){
            Product product=productMapper.selectByPrimaryKey(item.getProductId());
            int stock=product.getStock()-item.getQuantity();
            if(stock<0){
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }

        ArrayList<Integer> boiledArr=new ArrayList();
        //获取配方数量
        for (OrderItem item:orderItemList){
            if(boiledArr.size()==0){
                boiledArr.add(item.getFormula());
            }else{
                boolean flag=false;
                for(int i=0;i<boiledArr.size();i++){
                    if(item.getFormula()==boiledArr.get(i)){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    boiledArr.add(item.getFormula());
                }
            }
        }
        int boiledTotalPrice=0;
        MallBoiled mallBoiled=mallBoiledMapper.selectByName(boiledArr.size());
        if(mallBoiled!=null){
            boiledTotalPrice=mallBoiled.getPrice()*mallBoiled.getCount();
        }

        //把购物车中的已勾选的商品删除
        cleanCart(cartVOList);

        //生成订单号，有独立的规则
        Order order=new Order();
        String orderNo=OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList,boiledTotalPrice));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setRemarks(createOrderReq.getRemarks());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAYED.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);

        //循环保存每个商品到order_item表
        for (OrderItem item:orderItemList){
            item.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(item);
        }
        //返回结果
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItemList,Integer boiledPrice) {
        Integer totalPrice=boiledPrice;
        for (OrderItem item:orderItemList){
            totalPrice+=item.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (CartVO vo:cartVOList){
            cartMapper.deleteByPrimaryKey(vo.getId());
        }
    }


    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartVO vo:cartVOList){
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(vo.getProductId());
            //记录商品快照信息
            orderItem.setProductName(vo.getProductName());
            orderItem.setProductImg(vo.getProductImage());
            orderItem.setUnitPrice(vo.getPrice());
            orderItem.setQuantity(vo.getQuantity());
            orderItem.setTotalPrice(vo.getTotalPrice());
            orderItem.setHandle(vo.getHandle());
            orderItem.setFormula(vo.getFormula());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /**
     * 判断商品是否存在，上下架状态，库存
     * @param cartVOList
     */
    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i <cartVOList.size() ; i++) {
            CartVO cartVO=cartVOList.get(i);
            Product product=productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if(product==null ||  product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
                throw new MallException(MallExceptionEnum.NOT_SALE);
            }
            //判断商品库存
            if(cartVO.getQuantity()>product.getStock()){
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    @Override
    public OrderVO detail(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        //订单不存在
        if (order==null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //校验订单所属
//        if(!order.getUserId().equals(userId)){
//            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
//        }
        OrderVO orderVO=getOrderVo(order);
        return orderVO;

    }

    @Override
    public OrderVO detailForEmail(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        //订单不存在
        if (order==null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        OrderVO orderVO=getOrderVo(order);
        return orderVO;

    }

    public OrderVO getOrderVo(Order order) {
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对象的orderVOList
        List<OrderItem> orderItemList=orderItemMapper.selectByOrderItemList(order.getOrderNo());
        List<OrderItemVO> orderItemVOList=new ArrayList<>();
        for(OrderItem item:orderItemList){
            OrderItemVO orderItemVO=new OrderItemVO();
            BeanUtils.copyProperties(item,orderItemVO);
            Variant variant=variantMapper.selectByPrimaryKey(item.getHandle());
            if(variant!=null){
                orderItemVO.setHandleName(variant.getName());
            }else{
                orderItemVO.setHandleName("暂无");
            }

            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        //获取中文状态码
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize,Integer userId){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList=orderListOrderVOList(orderList);
        PageInfo orderPageInfo=new PageInfo(orderList);
        orderPageInfo.setList(orderVOList);
        return orderPageInfo;
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize,String orderNo){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtil.isNullOrEmpty(orderNo)) orderNo=null;
        List<Order> orderList=orderMapper.selectForAdmin(orderNo);
        List<OrderVO> orderVOList=orderListOrderVOList(orderList);
        PageInfo orderPageInfo=new PageInfo(orderList);
        orderPageInfo.setList(orderVOList);
        return orderPageInfo;
    }

    private List<OrderVO> orderListOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList=new ArrayList<>();
        for(Order order:orderList){
            OrderVO orderVO=getOrderVo(order);
            //不加已取消的订单
            if(orderVO.getOrderStatus()!=0)
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public void cancel(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        if (order==null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //校验订单所属
//        Integer userId=UserFilter.currentUser.getId();
//        if(!order.getUserId().equals(userId)){
//            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
//        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAYED.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MallException(MallExceptionEnum.WORNG_ORDER_STATUS);
        }
    }

    @Override
    public String qrcode(String orderNo){
        //获取主机地址
        ServletRequestAttributes attributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String address=ip+":"+request.getLocalPort();

        String payUrl="http://"+address+"/pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350,Constant.FILE_UPLOAD_DIR+orderNo+".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress="http://"+address+"/api/imgs/"+orderNo+".png";
        return pngAddress;
    }


}
