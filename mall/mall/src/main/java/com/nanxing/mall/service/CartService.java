package com.nanxing.mall.service;

import com.nanxing.mall.model.vo.CartVO;

import java.util.List;

/**
 * @description: 购物车Service
 * @author: Mr.Tang
 * @create: 2021/1/16 9:26
 **/
public interface CartService {


    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count,Integer handle,Integer formula);

    List<CartVO> update(Integer userId, Integer productId, Integer count);

    List<CartVO> delete(Integer userId, Integer productId);

    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected);

    List<CartVO> selectAllOrNotAll(Integer userId, Integer selected);
}
