package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.pojo.Category;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.request.AddCategoryReq;
import com.nanxing.mall.model.request.UpdateCategoryReq;
import com.nanxing.mall.model.vo.CategoryVO;
import com.nanxing.mall.service.CategoryService;
import com.nanxing.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @description: 商品目录Controller
 * @author: Mr.Tang
 * @create: 2021/1/15 12:38
 **/
@Api(tags = "商品类型目录")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    /**
     * 管理员添加分类目录
     * @param session
     * @param addCategoryReq 分类请求对象
     * @return
     */
    @ApiOperation("后台添加目录")
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,@Valid @RequestBody AddCategoryReq addCategoryReq){
//        if(addCategoryReq.getName()==null ||addCategoryReq.getType()==null ||addCategoryReq.getOrderNum()==null ||addCategoryReq.getParentId()==null){
//            return ApiRestResponse.error(MallExceptionEnum.NAME_NOT_NULL);
//        }
//        User currentUser=(User)session.getAttribute(Constant.MALL_USER);
//        if(currentUser==null){
//            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
//        }
        categoryService.add(addCategoryReq);
        return ApiRestResponse.success();

        //检验是否是管理员
//        boolean adminRole=userService.checkAdminRole(currentUser);
//        if(adminRole){
//
//        }else{
//            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
//        }
    }

    @ApiOperation("后台更新目录")
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(HttpSession session,@Valid @RequestBody UpdateCategoryReq updateCategoryReq){
//        User currentUser=(User)session.getAttribute(Constant.MALL_USER);
//        if(currentUser==null){
//            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
//        }

        //检验是否是管理员
//        boolean adminRole=userService.checkAdminRole(currentUser);
//        if(adminRole){
            Category category=new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            categoryService.update(category);
            return ApiRestResponse.success();
//        }else{
//            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
//        }
    }

    @ApiOperation("后台删除目录")
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台目录列表")
    @GetMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo=categoryService.listForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 递归查询子目录
     * @return
     */
    @ApiOperation("前台目录列表")
    @GetMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer(){
        List<CategoryVO> categoryVOS=categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
