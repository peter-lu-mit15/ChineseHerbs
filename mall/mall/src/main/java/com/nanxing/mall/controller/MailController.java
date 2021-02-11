package com.nanxing.mall.controller;

import com.github.pagehelper.PageInfo;
import com.nanxing.mall.common.ApiRestResponse;
import com.nanxing.mall.common.Constant;
import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallGraspMapper;
import com.nanxing.mall.model.dao.OrderMapper;
import com.nanxing.mall.model.pojo.*;
import com.nanxing.mall.model.vo.OrderItemVO;
import com.nanxing.mall.model.vo.OrderVO;
import com.nanxing.mall.service.GraspService;
import com.nanxing.mall.service.MailService;
import com.nanxing.mall.service.OrderService;
import com.nanxing.mall.service.UserService;
import com.nanxing.mall.service.impl.MailServiceImpl;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @description: 邮件发送
 * @author: Mr.Tang
 * @create: 2021/1/21 10:01
 **/

@Api(tags = "邮件服务")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/mail/")
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    MailService mailService;
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    GraspService graspService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    MallGraspMapper mallGraspMapper;

    @ApiOperation("配置文件参数发送邮件")
    @PostMapping(value = "send")
    public ApiRestResponse send(@RequestBody MailSendDto dto) {
        mailService.sendSimpleMailMessge(dto);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }

    @ApiOperation("系统参数发送邮件")
    @PostMapping(value = "/system_send")
    public ApiRestResponse sendSys(@RequestBody MailSendDto dto) {
        mailService.sendMailMessge(dto);
        logger.info("MailController send success.............");
        return ApiRestResponse.success();
    }

    @ApiOperation("通知用户和抓药师")
    @GetMapping("sendEmail")
    public ApiRestResponse SendEmail(@RequestParam String orderNo){
        OrderVO orderVO=orderService.detail(orderNo);
        //生成html模板
        User user=userService.getUser(orderVO.getUserId());
        String sendhtml=HtmlForConfirm(orderVO);
        //通知用户
        mailService.sendMimeMessgeForHtml(user.getEmail(),"订单确认通知",sendhtml);
        //通知抓药师
        List<MallGrasp> graspList=mallGraspMapper.selectList();
        for (MallGrasp item : graspList) {
            if(!StringUtil.isNullOrEmpty(item.getEmail()))
            mailService.sendMimeMessgeForHtml(item.getEmail(),"客户订单确认通知",sendhtml);
        }
        return ApiRestResponse.success();
    }

    @ApiOperation("通知用户取药")
    @GetMapping("SendEmailForOrder")
    public ApiRestResponse SendEmailForOrder(@RequestParam String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        if (order==null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }


//        OrderVO orderVO=orderService.detailForEmail(orderNo);
        //生成html模板
        User user=userService.getUser(order.getUserId());
        //String sendhtml=HtmlForConfirm(orderVO);
        MailSendDto mailSendDto=new MailSendDto();
        mailSendDto.setContent("您好；你的订单："+orderNo+"已处理好，请及时去取药;");
        mailSendDto.setSubject("取药通知");
        mailSendDto.setTo(user.getEmail());
        mailService.sendMailMessge(mailSendDto);
        //改变订单状态
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAYED.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CUSTOMER.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MallException(MallExceptionEnum.WORNG_ORDER_STATUS);
        }


        return ApiRestResponse.success();
    }

    /**
     * 订单确认html模板
     * @param orderVo
     * @return
     */
    private String HtmlForConfirm(OrderVO orderVo){
        String html="";
        html+="<p>"+orderVo.getReceiverName()+"您好：</p>";
        html+="<p>本次你一共点的药材如下：</p><div>";
        String str="";
        for(int i=0;i<orderVo.getOrderItemVOList().size();i++){
            OrderItemVO item=orderVo.getOrderItemVOList().get(i);
            html+="<span>formula#"+item.getFormula()+"</span>-";
            html+="<span>药材名称："+item.getProductName()+"</span>;";
            html+="<span>处理类型："+item.getHandleName()+"</span>;";
            html+="<span>数量："+item.getQuantity()+"</span>;";
            html+="<span>单价："+item.getUnitPrice()+"</span>;";
            html+="<span>小合计："+item.getTotalPrice()+"</span>";
            html+="<br><hr>";
        }
        html+="</div><p>备注信息："+orderVo.getRemarks()+"</p><br>";
        html+="<p>总金额："+orderVo.getTotalPrice()+"</p>";

        return html;
    }

    @ApiOperation("添加抓药师邮箱")
    @PostMapping("add")
    public ApiRestResponse add(@RequestParam String email){
        MallGrasp mallGrasp=new MallGrasp();
        mallGrasp.setEmail(email);
        graspService.add(mallGrasp);
        return ApiRestResponse.success();
    }

    @ApiOperation("修改抓药师邮箱")
    @PostMapping("update")
    public ApiRestResponse update(@RequestParam String email,@RequestParam Integer id){
        MallGrasp mallGrasp=new MallGrasp();
        mallGrasp.setEmail(email);
        mallGrasp.setId(id);
        graspService.update(mallGrasp);
        return ApiRestResponse.success();
    }

    @ApiOperation("删除抓药师邮箱")
    @GetMapping("delete")
    public ApiRestResponse delete(@RequestParam Integer id){
        graspService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("抓药师邮箱列表")
    @GetMapping("list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo=graspService.list(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }


}
