package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallBoiledMapper;
import com.nanxing.mall.model.dao.MallGraspMapper;
import com.nanxing.mall.model.pojo.MallBoiled;
import com.nanxing.mall.model.pojo.MallGrasp;
import com.nanxing.mall.model.request.AddMallBoiledReq;
import com.nanxing.mall.service.BoiledService;
import com.nanxing.mall.service.GraspService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 抓药师邮箱服务实现类
 * @author: Mr.Tang
 * @create: 2021/1/15 20:27
 **/
@Service
public class GraspServiceImpl implements GraspService {

    @Autowired
    MallGraspMapper mallGraspMapper;

    @Override
    public void add(MallGrasp mallGrasp){
        MallGrasp mallGraspNew=new MallGrasp();
        BeanUtils.copyProperties(mallGrasp,mallGraspNew);
        MallGrasp mallGraspOld=mallGraspMapper.selectByName(mallGrasp.getEmail());
        if(mallGraspOld!=null){
            throw new MallException(MallExceptionEnum.NAMES_EXISTED);
        }
        int count=mallGraspMapper.insertSelective(mallGraspNew);
        if (count==0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(MallGrasp mallGrasp){
        MallGrasp mallGraspOld=mallGraspMapper.selectByName(mallGrasp.getEmail());
        //同名且不同id，不能修改
        if(mallGraspOld!=null && !mallGraspOld.getId().equals(mallGrasp.getId())){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
        int count=mallGraspMapper.updateByPrimaryKeySelective(mallGrasp);
        if (count==0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        MallGrasp mallGrasp=mallGraspMapper.selectByPrimaryKey(id);
        if(mallGrasp==null ){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=mallGraspMapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MallGrasp> mallGraspList=mallGraspMapper.selectList();
        PageInfo pageInfo=new PageInfo(mallGraspList);
        return pageInfo;
    }


}
