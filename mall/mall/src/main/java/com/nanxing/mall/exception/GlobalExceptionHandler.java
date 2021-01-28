package com.nanxing.mall.exception;

import com.nanxing.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 处理统一异常的handler
 * @author: Mr.Tang
 * @create: 2021/1/15 10:56
 * ControllerAdvice:拦截异常
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception:",e);
        return ApiRestResponse.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(MallException.class)
    @ResponseBody
    public Object handleMallException(MallException e){
        log.error("Mall Exception:",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

    /**
     * 系统处理的异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.info("MethodArgumentNotValidException:"+e);
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestResponse handleBindingResult(BindingResult result){
        //把异常处理为对外暴露的提示
        List<String> list=new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> arrErrors=result.getAllErrors();
            for (ObjectError objectError:arrErrors) {
                String message=objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if(list.size()==0){
            return ApiRestResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }
}
