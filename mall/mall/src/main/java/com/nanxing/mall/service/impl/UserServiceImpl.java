package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.UserMapper;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.service.UserService;
import com.nanxing.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @description: UserService实现类
 * @author: Mr.Tang
 * @create: 2021/1/14 20:08
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void register(String username, String password) throws MallException {
        //查询用户名是否存在
        User result=userMapper.selectByName(username);
        if(result!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }

        //写入数据库
        User user=new User();
        user.setUsername(username);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        int count=userMapper.insertSelective(user);
        if(count==0){
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String username, String password) throws MallException {
        String md5Password=null;
        try {
            md5Password=MD5Utils.getMD5Str(password);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        User user=userMapper.selectLogin(username,md5Password);
        if(user==null){
            throw new MallException(MallExceptionEnum.WONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInfomation(User user) throws MallException {
        //更新个性签名
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>1){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user){
        //1.普通用户 2.管理员
        return user.getRole().equals(2);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize, String keyword){
        PageHelper.startPage(pageNum,pageSize);
        List<User>  userList=userMapper.selectListForAdmin(keyword);
        PageInfo pageInfo=new PageInfo(userList);
        return pageInfo;

    }

    @Override
    public void delete(Integer id){
        User user=userMapper.selectByPrimaryKey(id);
        if(user==null ){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=userMapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void update(User user){
        User userOld=userMapper.selectByName(user.getUsername());
        //同名且不同id，不能修改
        if(userOld!=null && !userOld.getId().equals(user.getId())){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
        int count=userMapper.updateByPrimaryKeySelective(user);
        if (count==0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }

    }

}
