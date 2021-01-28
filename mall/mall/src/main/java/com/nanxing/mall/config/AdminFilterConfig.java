package com.nanxing.mall.config;

import com.nanxing.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

/**
 * @description: Admin过滤器的配置
 * @author: Mr.Tang
 * @create: 2021/1/15 15:10
 **/
@Configuration
public class AdminFilterConfig {
//    @Bean
//    public AdminFilter adminFilter(){
//        return new AdminFilter();
//    }
//
//    @Bean(name = "adminFilterConf")
//    public FilterRegistrationBean adminFilterConfig(){
//        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(adminFilter());
//        filterRegistrationBean.addUrlPatterns("/admin/category/*");
//        filterRegistrationBean.addUrlPatterns("/admin/product/*");
//        filterRegistrationBean.addUrlPatterns("/admin/order/*");
//        filterRegistrationBean.setName("adminFilterConf");
//        return filterRegistrationBean;
//    }
}
