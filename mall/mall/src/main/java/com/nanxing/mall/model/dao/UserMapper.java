package com.nanxing.mall.model.dao;

import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String username);

    User selectLogin(@Param("username") String userName,@Param("password") String password);

    List<User> selectListForAdmin(@Param("keyword") String keyword);

}