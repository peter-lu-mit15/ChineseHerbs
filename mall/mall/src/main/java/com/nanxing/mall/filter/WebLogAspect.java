package com.nanxing.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @description: 打印请求和响应信息
 * @author: Mr.Tang
 * @create: 2021/1/15 9:41
 **/
@Aspect
@Component
public class WebLogAspect {
    private final Logger log=LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.nanxing.mall.controller.*.*(..))")
    public void webLog(){

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        //收到请求，记录请求内容
        ServletRequestAttributes attributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();

        log.info("URL : "+request.getRequestURI().toString());
        log.info("HTTP_METHOD : "+request.getMethod());
        log.info("IP : "+request.getRemoteAddr());
        log.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        log.info("ARGS : "+ Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "res",pointcut = "webLog()")
    public void DoAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容
        log.info("RESPONSE :"+new ObjectMapper().writeValueAsString(res));
    }
}
