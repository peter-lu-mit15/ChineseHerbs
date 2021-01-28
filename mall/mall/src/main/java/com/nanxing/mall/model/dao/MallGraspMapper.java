package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.MallGrasp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallGraspMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallGrasp record);

    int insertSelective(MallGrasp record);

    MallGrasp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallGrasp record);

    int updateByPrimaryKey(MallGrasp record);

    MallGrasp selectByName(String email);

    List<MallGrasp> selectList();
}