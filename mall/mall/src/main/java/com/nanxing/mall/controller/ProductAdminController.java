package com.nanxing.mall.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.pojo.Product;
import com.nanxing.mall.model.pojo.Variant;
import com.nanxing.mall.model.query.ProductEntity;
import com.nanxing.mall.model.query.UserEntity;
import com.nanxing.mall.model.request.AddProductReq;
import com.nanxing.mall.model.request.ProductListReq;
import com.nanxing.mall.model.request.UpdateProductReq;
import com.nanxing.mall.model.vo.ProductVO;
import com.nanxing.mall.service.ProductService;
import com.nanxing.mall.service.VariantService;
import com.nanxing.mall.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    VariantService variantService;
    @Value("${file.upload.excel}")
    String excel_dir;


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

    public String getFileName(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /**
     * Excel导出
     */
    @ApiOperation("药材信息导出")
    @GetMapping("exportExcel")
    public ApiRestResponse exportExcel(HttpServletResponse response) throws IOException {
        ProductListReq params=new ProductListReq();
        params.setPageNum(1);
        params.setPageSize(10000);
        PageInfo pageInfo=productService.list(params);
        List<ProductVO> list=pageInfo.getList();
        List<ProductEntity> resultList=new ArrayList<ProductEntity>();
        for(int i=0;i<list.size();i++){
            ProductVO item=list.get(i);
            ProductEntity p=new ProductEntity();
            BeanUtils.copyProperties(item,p);
            PageInfo variantPageInfo=variantService.listByProduct(1,5,item.getId());
            if(variantPageInfo.getList().size()>0){
                for(int j=0;j<variantPageInfo.getList().size();j++){
                    Variant  variantItem=(Variant)variantPageInfo.getList().get(j);
                    switch (j){
                        case 0:
                                p.setVariant1(variantItem.getName());
                                p.setVariantPrice1(variantItem.getPrice());
                                break;
                        case 1:
                            p.setVariant2(variantItem.getName());
                            p.setVariantPrice2(variantItem.getPrice());
                            break;
                        case 2:
                            p.setVariant3(variantItem.getName());
                            p.setVariantPrice3(variantItem.getPrice());
                            break;
                        case 3:
                            p.setVariant4(variantItem.getName());
                            p.setVariantPrice4(variantItem.getPrice());
                            break;
                        default:
                            p.setVariant5(variantItem.getName());
                            p.setVariantPrice5(variantItem.getPrice());
                            break;
                    }
                }
            }

            resultList.add(p);
        }

        String fileName=getFileName()+"Medicine.xls";
        //导出操作
        FileOutputStream fos = null;
        try (
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("MedicineInfo","medicine"), ProductEntity.class, resultList)
        ) {
            fos = new FileOutputStream(excel_dir+""+fileName);
            workbook.write(fos);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fos.close();
        }
        return ApiRestResponse.success(fileName);

    }

    /**
     * Excel导入
     */
    @ApiOperation("药材信息导入")
    @PostMapping("importExcel")
    public ApiRestResponse importExcel(HttpServletRequest request,HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        //解析excel
        List<ProductEntity> productList = ExcelUtil.importExcel(file, 1, 1, ProductEntity.class);
        System.out.println("导入数据一共【" + productList.size() + "】行");
        if(productList.size()>0){
            productService.importProduct(request,productList);
            return ApiRestResponse.success();
        }else{
            throw new MallException(MallExceptionEnum.FILE_IS_NULL);
        }
    }

    @ApiOperation("导入模板下载")
    @GetMapping("productExcel")
    public ApiRestResponse getProductExcel(){
        String url=excel_dir+"medicine_import.xls";
        return ApiRestResponse.success(url);
    }



}
