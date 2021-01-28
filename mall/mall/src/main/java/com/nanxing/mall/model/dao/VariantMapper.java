package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.Variant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Variant record);

    int insertSelective(Variant record);

    Variant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Variant record);

    int updateByPrimaryKey(Variant record);

    List<Variant> selectList();

    List<Variant> selectListByProductId(Integer productId);

    Variant selectByNameAndProductId(@Param("name") String name,@Param("productId") Integer productId);

    List<Variant> selectByProductIdList(Integer productId);
}