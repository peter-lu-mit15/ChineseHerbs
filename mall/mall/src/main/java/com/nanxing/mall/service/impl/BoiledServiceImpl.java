package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallBoiledMapper;
import com.nanxing.mall.model.pojo.MallBoiled;
import com.nanxing.mall.model.request.AddMallBoiledReq;
import com.nanxing.mall.service.BoiledService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 煮药价格服务实现类
 * @author: Mr.Tang
 * @create: 2021/1/15 20:27
 **/
@Service
public class BoiledServiceImpl implements BoiledService {

    @Autowired
    MallBoiledMapper mallBoiledMapper;

    @Override
    public void add(AddMallBoiledReq addMallBoiledReq){
        MallBoiled mallBoiled=new MallBoiled();
        BeanUtils.copyProperties(addMallBoiledReq,mallBoiled);
        MallBoiled mallBoiledOld=mallBoiledMapper.selectByName(addMallBoiledReq.getCount());
        if(mallBoiledOld!=null){
            throw new MallException(MallExceptionEnum.NAMES_EXISTED);
        }
        int count=mallBoiledMapper.insertSelective(mallBoiled);
        if (count==0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(MallBoiled boiled){
        MallBoiled mallBoiledOld=mallBoiledMapper.selectByName(boiled.getCount());
        //同名且不同id，不能修改
        if(mallBoiledOld!=null && !mallBoiledOld.getId().equals(boiled.getId())){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
        int count=mallBoiledMapper.updateByPrimaryKeySelective(boiled);
        if (count==0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        MallBoiled boiled=mallBoiledMapper.selectByPrimaryKey(id);
        if(boiled==null ){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=mallBoiledMapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MallBoiled> mallBoiledList=mallBoiledMapper.selectList();
        PageInfo pageInfo=new PageInfo(mallBoiledList);
        return pageInfo;
    }


}
