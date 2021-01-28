package com.nanxing.mall.filter;

import com.nanxing.mall.common.Constant;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 用户校验器
 * @author: Mr.Tang
 * @create: 2021/1/15 15:01
 **/
public class UserFilter implements Filter {
    @Autowired
    UserService userService;

    public static User currentUser;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpSession session=request.getSession();
        currentUser=(User)session.getAttribute(Constant.MALL_USER);
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

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
}
