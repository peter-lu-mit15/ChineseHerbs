package com.nanxing.mall.config;

import com.nanxing.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 配置地址映射
 * @author: Mr.Tang
 * @create: 2021/1/15 14:11
 **/
@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    /**
     * 这里是映射文件路径的方法
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //前端界面
//        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");

        registry.addResourceHandler("/imgs/**").addResourceLocations("file:"+ Constant.FILE_UPLOAD_DIR);

        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
