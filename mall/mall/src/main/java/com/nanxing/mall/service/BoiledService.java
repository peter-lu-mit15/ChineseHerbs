package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.pojo.MallBoiled;
import com.nanxing.mall.model.request.AddMallBoiledReq;

public interface BoiledService {
    void add(AddMallBoiledReq addMallBoiledReq);

    void update(MallBoiled boiled);

    void delete(Integer id);

    PageInfo list(Integer pageNum, Integer pageSize);
}
