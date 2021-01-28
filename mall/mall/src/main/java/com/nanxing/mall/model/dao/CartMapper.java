package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.Cart;
import com.nanxing.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByadd(@Param("userId") Integer userId, @Param("productId") Integer productId,@Param("formula") Integer formula,@Param("handle") Integer handle);

    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<CartVO> selectList(@Param("userId") Integer userId);

    Integer selectOrNot(@Param("userId") Integer userId,@Param("productId")Integer productId,@Param("selected")Integer selected);
}