package com.nanxing.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.ProductMapper;
import com.nanxing.mall.model.dao.VariantMapper;
import com.nanxing.mall.model.pojo.OrderItem;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.query.ProductListQuery;
import com.nanxing.mall.model.request.AddProductReq;
import com.nanxing.mall.model.request.ProductListReq;
import com.nanxing.mall.model.vo.CategoryVO;
import com.nanxing.mall.model.vo.OrderItemVO;
import com.nanxing.mall.model.vo.ProductVO;
import com.nanxing.mall.service.CategoryService;
import com.nanxing.mall.service.ProductService;
import com.nanxing.mall.service.VariantService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 商品服务实现类
 * @author: Mr.Tang
 * @create: 2021/1/15 20:27
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    VariantMapper variantMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product=new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld=productMapper.selectByName(addProductReq.getName());
        if(productOld!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count=productMapper.insertSelective(product);
        if (count==0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct){
        Product productOld=productMapper.selectByName(updateProduct.getName());
        //同名且不同id，不能修改
        if(productOld!=null && !productOld.getId().equals(updateProduct.getId())){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
        int count=productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count==0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Product productOld=productMapper.selectByPrimaryKey(id);
        if(productOld==null ){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count=productMapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer status){
        productMapper.batchUpdateSellStatus(ids,status);

    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize,String keyword){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products=productMapper.selectListForAdmin(keyword);
        PageInfo pageInfo=new PageInfo(products);
        return pageInfo;

    }

    @Override
    public Product detail(Integer id){
        Product product=productMapper.selectByPrimaryKey(id);
        return product;

    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        //构建Query对象
        ProductListQuery productListQuery=new ProductListQuery();
        //搜索处理
//        if(!StringUtils.isEmpty(productListReq.getKeyword())){
//            String keyword=new StringBuilder().append("%").append(productListReq.getKeyword()).toString();
//            productListQuery.setKeyword(keyword);
//        }
        if(!StringUtils.isEmpty(productListReq.getKeyword())){
            productListQuery.setKeyword(productListReq.getKeyword());
        }
        if(!StringUtils.isEmpty(productListReq.getFirstName())){
            productListQuery.setFirstName(productListReq.getFirstName());
        }
        if(!StringUtils.isEmpty(productListReq.getPyName())){
            productListQuery.setPyName(productListReq.getPyName());
        }

        //处理目录,如果查某个目录下的商品，不仅是需要查出该目录下的，还要把所有的子目录的所有商品都查询出来，所以拿到一个目录id的list
        if(productListReq.getCategoryId()!=null){
            List<CategoryVO> categoryVOList=categoryService.listCategoryForCustomer(productListReq.getCategoryId());

            ArrayList<Integer> categoryIds=new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList,categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }
        //排序处理
        String orderBy=productListReq.getOrderBy();
        if(Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
        }else{
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }
        List<Product> productList=productMapper.selectList(productListQuery);

        //获取每种药材的处理类型列表
        List<ProductVO> productVOList=new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Product product=productList.get(i);
            if(product!=null){
                List<Variant> variantList=variantMapper.selectByProductIdList(product.getId());
                ProductVO productVO=new ProductVO();
                BeanUtils.copyProperties(product,productVO);
                productVO.setVariantList(variantList);
                productVOList.add(productVO);
            }
        }

        PageInfo pageInfo=new PageInfo(productVOList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList,ArrayList<Integer> categoryIds){
        for (CategoryVO vo:categoryVOList){
            if(vo!=null){
                categoryIds.add(vo.getId());
                getCategoryIds(vo.getChildCategory(),categoryIds);
            }
        }
    }

    @Override
    public List<String> getCharacterByPyin(String pyin) {
        return productMapper.getCharacterByPyin(pyin);
    }
}
