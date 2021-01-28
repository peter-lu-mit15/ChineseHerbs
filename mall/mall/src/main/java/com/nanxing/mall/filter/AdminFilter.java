package com.nanxing.mall.filter;

import com.nanxing.mall.common.Constant;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 管理员校验器
 * @author: Mr.Tang
 * @create: 2021/1/15 15:01
 **/
public class AdminFilter implements Filter {
    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpSession session=request.getSession();
        User currentUser=(User)session.getAttribute(Constant.MALL_USER);
        if(currentUser==null){
            PrintWriter out=new HttpServletResponseWrapper((HttpServletResponse)servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        //检验是否是管理员
        boolean adminRole=userService.checkAdminRole(currentUser);
        if(adminRole){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            PrintWriter out=new HttpServletResponseWrapper((HttpServletResponse)servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
        }
    }

    @Override
    public void destroy() {

    }
}
