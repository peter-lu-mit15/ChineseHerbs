package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallGraspMapper;
import com.nanxing.mall.model.dao.OrderMapper;
import com.nanxing.mall.model.pojo.MailSendDto;
import com.nanxing.mall.model.pojo.MallGrasp;
import com.nanxing.mall.model.pojo.Order;
import com.nanxing.mall.model.pojo.User;
import com.nanxing.mall.model.vo.OrderItemVO;
import com.nanxing.mall.model.vo.OrderVO;
import com.nanxing.mall.service.GraspService;
import com.nanxing.mall.service.MailService;
import com.nanxing.mall.service.OrderService;
import com.nanxing.mall.service.UserService;
import com.nanxing.mall.service.impl.MailServiceImpl;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 邮件发送
 * @author: Mr.Tang
 * @create: 2021/1/21 10:01
 **/

@Api(tags = "测试邮件服务")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/mailTest/")
public class MailAllController {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    MailService mailService;

    @ApiOperation("发送普通邮件")
    @PostMapping(value = "send")
    public ApiRestResponse send(@RequestBody MailSendDto dto) {
        mailService.sendSimpleMailMessge(dto);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }

    @ApiOperation("发送HTML邮件")
    @PostMapping(value = "sendMimeMessge")
    public ApiRestResponse sendMimeMessge(@RequestParam String to,@RequestParam String subject,@RequestParam String content) {
        mailService.sendMimeMessge(to,subject,content);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }

    @ApiOperation("发送带附件的邮件")
    @PostMapping(value = "sendMimeAndFileMessge")
    public ApiRestResponse sendMimeAndFileMessge(@RequestParam String to,@RequestParam String subject,@RequestParam String content,@RequestParam String filepath) {
        mailService.sendMimeMessge(to,subject,content,filepath);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }

    @ApiOperation("发送带静态文件的邮件")
    @PostMapping(value = "sendMimeAndStaticFileMessge")
    public ApiRestResponse sendMimeAndStaticFileMessge(@RequestParam String to,@RequestParam String subject,@RequestParam String content,@RequestParam Map<String, String> rscIdMap) {
        mailService.sendMimeMessge(to,subject,content,rscIdMap);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }







}
