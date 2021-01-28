package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    Product selectByName(String name);

    Product batchUpdateSellStatus(@Param("ids") Integer[] ids,@Param("status") Integer status);

    List<Product> selectListForAdmin(@Param("keyword") String keyword);

    List<Product> selectList(@Param("query") ProductListQuery query);

    List<String> getCharacterByPyin(String pyin);


}