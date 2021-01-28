package com.nanxing.mall.service.impl;

import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.CartMapper;
import com.nanxing.mall.model.dao.ProductMapper;
import com.nanxing.mall.model.dao.VariantMapper;
import com.nanxing.mall.model.pojo.Cart;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.vo.CartVO;
import com.nanxing.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 购物车实现类
 * @author: Mr.Tang
 * @create: 2021/1/16 9:27
 **/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    VariantMapper variantMapper;

    @Override
    public List<CartVO> list(Integer userId){
        List<CartVO> cartVOS=cartMapper.selectList(userId);
        for(CartVO vo:cartVOS){

            int price=1;
            Variant variant=variantMapper.selectByPrimaryKey(vo.getHandle());
            if(variant!=null){
                price=variant.getPrice();
            }
            int totalPrice=(vo.getPrice()*vo.getQuantity())+(vo.getQuantity()*price);
            vo.setTotalPrice(totalPrice);
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count,Integer handle,Integer formula){
        validProduct(productId,count);

        Cart cart=cartMapper.selectCartByadd(userId,productId,formula,handle);
        if(cart==null){
            //商品不在购物车，添加新的记录
            cart=new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setHandle(handle);
            cart.setFormula(formula);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        }else{
            //商品在购物车列表中，添加数量
            count=cart.getQuantity()+count;
            Cart cartNew=new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setHandle(handle);
            cartNew.setFormula(formula);
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    private void validProduct(Integer productId, Integer count) {
        Product product=productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在，商品是否上架
        if(product==null ||  product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
            throw new MallException(MallExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if(count>product.getStock()){
            throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);

        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart==null){
            //商品不在购物车，无法更新
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }else{
            //商品在购物车列表中，更新数量
            Cart cartNew=new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId){

        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart==null){
            //商品不在购物车，无法删除
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }else{
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId,Integer productId,Integer selected){
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart==null){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }else{
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectAllOrNotAll(Integer userId,Integer selected){
        cartMapper.selectOrNot(userId,null,selected);

        return this.list(userId);
    }


}
