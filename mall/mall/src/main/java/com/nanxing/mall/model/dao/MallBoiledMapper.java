package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.MallBoiled;
import com.nanxing.mall.model.pojo.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallBoiledMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallBoiled record);

    int insertSelective(MallBoiled record);

    MallBoiled selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallBoiled record);

    int updateByPrimaryKey(MallBoiled record);

    MallBoiled selectByName(Integer count);

    List<MallBoiled> selectList();
}