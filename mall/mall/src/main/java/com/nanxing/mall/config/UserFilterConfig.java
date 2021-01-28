package com.nanxing.mall.config;

import com.nanxing.mall.filter.AdminFilter;
import com.nanxing.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: User过滤器的配置
 * @author: Mr.Tang
 * @create: 2021/1/15 15:10
 **/
@Configuration
public class UserFilterConfig {
//    @Bean
//    public UserFilter userFilter(){
//        return new UserFilter();
//    }
//
//    @Bean(name = "userFilterConf")
//    public FilterRegistrationBean adminFilterConfig(){
//        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(userFilter());
//
//        filterRegistrationBean.addUrlPatterns("/productByLogin/*");
//        filterRegistrationBean.addUrlPatterns("/mail/*");
//        filterRegistrationBean.addUrlPatterns("/cart/*");
//        filterRegistrationBean.addUrlPatterns("/order/*");
//        filterRegistrationBean.setName("userFilterConf");
//        return filterRegistrationBean;
//    }
}
