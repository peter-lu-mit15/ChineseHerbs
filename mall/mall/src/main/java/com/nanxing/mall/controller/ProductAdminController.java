package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.request.AddProductReq;
import com.nanxing.mall.model.request.UpdateProductReq;
import com.nanxing.mall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @description: 后台商品管理Controller
 * @author: Mr.Tang
 * @create: 2021/1/15 19:44
 * RestController: 代表每一个接口都是已ResponseBody的形式返回
 **/

@Api(tags = "后台商品管理")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("admin/")
public class ProductAdminController {
    @Autowired
    ProductService productService;


    /**
     * 后台商品添加
     * @param addProductReq
     * @return
     */
    @ApiOperation("后台商品添加")
    @PostMapping("/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 图片上传
     * @param request
     * @param file
     * @return
     */
    @ApiOperation("商品图片上传")
    @PostMapping("upload/file")
    public ApiRestResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        String fileName=file.getOriginalFilename();
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid=UUID.randomUUID();
        String newFileName=uuid.toString()+suffixName;
        //创建文件
        File fileDirectory=new File(Constant.FILE_UPLOAD_DIR);
        File destFile=new File(Constant.FILE_UPLOAD_DIR+newFileName);
        if(!fileDirectory.exists()){
            if(!fileDirectory.mkdir()){
                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse.success(getHost(new URI(request.getRequestURL()+""))+"/api/imgs/"+newFileName
            );
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 通过请求获取当前服务器地址
     * @param uri
     * @return
     */
    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI=new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
        } catch (URISyntaxException e) {
            effectiveURI=null;
        }
        return effectiveURI;
    }

    @ApiOperation("后台更新商品")
    @PostMapping("product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product=new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品删除")
    @PostMapping("product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架")
    @PostMapping("product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer status){
        productService.batchUpdateSellStatus(ids,status);
        return ApiRestResponse.success();
    }

    @CrossOrigin
    @ApiOperation("后台商品列表")
    @PostMapping("product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String keyword){
        PageInfo pageInfo=productService.listForAdmin(pageNum,pageSize,keyword);
        return ApiRestResponse.success(pageInfo);
    }
}
