package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.MallSystem;
import org.springframework.stereotype.Repository;

@Repository
public interface MallSystemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallSystem record);

    int insertSelective(MallSystem record);

    MallSystem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallSystem record);

    int updateByPrimaryKey(MallSystem record);

    MallSystem topOne();
}