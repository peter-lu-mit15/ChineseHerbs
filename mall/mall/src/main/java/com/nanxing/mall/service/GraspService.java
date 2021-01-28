package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.model.pojo.MallGrasp;

public interface GraspService {
    void add(MallGrasp mallGrasp);

    void update(MallGrasp mallGrasp);

    void delete(Integer id);

    PageInfo list(Integer pageNum, Integer pageSize);
}
