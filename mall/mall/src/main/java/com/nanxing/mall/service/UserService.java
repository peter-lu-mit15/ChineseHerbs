package com.nanxing.mall.service;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.User;

/**
 * @description: UserService
 * @author: Mr.Tang
 * @create: 2021/1/14 20:05
 **/

public interface UserService {

    User getUser(Integer userId);

    void register(String username,String password) throws MallException;

    User login(String username, String password) throws MallException;

    void updateInfomation(User user) throws MallException;

    boolean checkAdminRole(User user);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize, String keyword);

    void delete(Integer id);

    void update(User user);

}
