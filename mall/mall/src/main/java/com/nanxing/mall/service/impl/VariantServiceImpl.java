package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.VariantMapper;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.request.AddVariant;
import com.nanxing.mall.model.request.UpdateVariant;
import com.nanxing.mall.service.VariantService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantServiceImpl implements VariantService {
    @Autowired
    VariantMapper variantMapper;

    @Override
    public List<Variant> add(AddVariant addVariant) {
        Variant variant=new Variant();
        BeanUtils.copyProperties(addVariant,variant);
        Variant variantOld=variantMapper.selectByNameAndProductId(addVariant.getName(),addVariant.getProductId());
        if(variantOld!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count=variantMapper.insertSelective(variant);
        if(count==0){
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
        List<Variant> variantList=variantMapper.selectListByProductId(addVariant.getProductId());
        return variantList;
    }

    @Override
    public List<Variant> update(Variant updateVariant) {
        if(updateVariant.getName()!=null){
            Variant variantOld=variantMapper.selectByNameAndProductId(updateVariant.getName(),updateVariant.getProductId());
            if(variantOld!=null && variantOld.getId().equals(updateVariant.getId())){
                throw new MallException(MallExceptionEnum.NAME_EXISTED);
            }
        }
        int count=variantMapper.updateByPrimaryKeySelective(updateVariant);
        if(count==0){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
        List<Variant> variantList=variantMapper.selectListByProductId(updateVariant.getProductId());
        return variantList;
    }

    @Override
    public List<Variant> delete(Integer id) {
        Variant variantOld=variantMapper.selectByPrimaryKey(id);
        int productId=variantOld.getProductId();
        //查不到记录，无法删除，删除失败
        if(variantOld==null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=variantMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        List<Variant> variantList=variantMapper.selectListByProductId(productId);
        return variantList;
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Variant> variantList=variantMapper.selectList();
        PageInfo pageInfo=new PageInfo(variantList);
        return pageInfo;

    }

    @Override
    public PageInfo listByProduct(Integer pageNum, Integer pageSize,Integer productId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Variant> variantList=variantMapper.selectListByProductId(productId);
        PageInfo pageInfo=new PageInfo(variantList);
        return pageInfo;

    }

}
