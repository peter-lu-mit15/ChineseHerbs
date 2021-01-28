package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.CategoryMapper;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.request.AddCategoryReq;
import com.nanxing.mall.model.vo.CategoryVO;
import com.nanxing.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 目录分类Service实现类
 * @author: Mr.Tang
 * @create: 2021/1/15 13:18
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category=new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        Category oldCategory=categoryMapper.selectByName((addCategoryReq.getName()));
        if(oldCategory!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count=categoryMapper.insertSelective(category);
        if(count==0){
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Category updateCategory){
        if(updateCategory.getName()!=null){
            Category categoryOld=categoryMapper.selectByName(updateCategory.getName());
            if(categoryOld!=null && categoryOld.getId().equals(updateCategory.getId())){
                throw new MallException(MallExceptionEnum.NAME_EXISTED);
            }
        }
        int count=categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if(count==0){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Category categoryOld=categoryMapper.selectByPrimaryKey(id);
        //查不到记录，无法删除，删除失败
        if(categoryOld==null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=categoryMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categoryList=categoryMapper.selectList();
        PageInfo pageInfo=new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value="listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer parentId){
        ArrayList<CategoryVO> categoryVOArrayList=new ArrayList<>();
        recursivelyFindCategoryies(categoryVOArrayList,parentId);
        return categoryVOArrayList;
    }

    private void recursivelyFindCategoryies(List<CategoryVO> categoryVOList,Integer parentId){
        //递归获取所有子类别，并组合成为一个"树目录"
        List<Category> categoryList=categoryMapper.selectCategoriesByParentId(parentId);
        if(!CollectionUtils.isEmpty(categoryList)){
            for (Category cate:categoryList){
                CategoryVO categoryVO=new CategoryVO();
                BeanUtils.copyProperties(cate,categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategoryies(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }


}
