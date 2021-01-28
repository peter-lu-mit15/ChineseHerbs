package com.nanxing.mall.service.impl;

import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallSystemMapper;
import com.nanxing.mall.model.pojo.MallSystem;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.service.MallSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MallSystemServiceImpl implements MallSystemService {
    @Autowired
    MallSystemMapper mallSystemMapper;

    @Override
    public void update(MallSystem mallSystem) {
        int count=mallSystemMapper.updateByPrimaryKeySelective(mallSystem);
        if(count==0){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public MallSystem detail() {
        MallSystem mallSystem=mallSystemMapper.topOne();
        return mallSystem;
    }
}
