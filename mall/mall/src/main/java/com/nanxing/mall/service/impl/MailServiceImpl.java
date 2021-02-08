package com.nanxing.mall.service.impl;

import com.nanxing.mall.exception.MallException;
import com.nanxing.mall.exception.MallExceptionEnum;
import com.nanxing.mall.model.dao.MallSystemMapper;
import com.nanxing.mall.model.pojo.MailSendDto;
import com.nanxing.mall.model.pojo.MallSystem;
import com.nanxing.mall.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    MallSystemMapper systemMapper;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendSimpleMailMessge(MailSendDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(dto.getContent());
        try {
            mailSender.send(message);
            logger.info("send success.................");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
        }
    }



    /**
     * @description:通知用户取药
     * @author: Mr.Tang
     * @create: 2021/1/26 14:30
     **/
    @Override
    public void sendMailMessge(MailSendDto dto) {
        MallSystem mallSystem=systemMapper.topOne();

        
        String from=mallSystem.getSysEmail();                           //发件邮箱
        String to=dto.getTo();                             //收件邮箱
        String subject=dto.getSubject();                        //邮件标题
        String text=dto.getContent();                           //邮件内容
        String host="smtp.163.com";                           //smtp服务器
        int port=465;                              //smtp服务端口
        String password=mallSystem.getSysAuthorization();                       //授权码
        String sender="XX药店";                         //发件人
        



        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(host);
        jms.setPort(port);
        jms.setUsername(from);
        jms.setPassword(password);
        jms.setDefaultEncoding("Utf-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.connectiontimeout", "5000");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.writetimeout", "5000");
        //properties.setProperty("mail.smtp.starttls.enable", "true");
        //properties.setProperty("mail.smtp.starttls.required", "true");
        //properties.setProperty("mail.smtp.ssl.enable", "true");
        jms.setJavaMailProperties(properties);

        try {
            MimeMessage mimeMessage = jms.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from,sender);                //sender为自定义显示发件人名称
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            jms.send(mimeMessage);
            logger.info("send success.................");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }



//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(sender);
//        message.setTo(dto.getTo());
//        message.setSubject(dto.getSubject());
//        message.setText(dto.getContent());
//        try {
//            mailSender.send(message);
//            logger.info("send success.................");
//        } catch (Exception e) {
//            logger.error("发送简单邮件时发生异常!", e);
//        }
    }

    @Override
    public void sendSimpleMailMessge(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("send success.................");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }
    }

    @Override
    public void sendMimeMessgeForHtml(String to, String subject, String content) {
        
        MallSystem mallSystem=systemMapper.topOne();

        String from=mallSystem.getSysEmail();  ;                    //发件邮箱
        String host="smtp.gmail.com";                          //smtp服务器
        int port=465;                                          //smtp服务端口
        String password="SGR123456";                           //授权码
        String sender="药店";                                 //发件人

        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(host);
        jms.setPort(port);
        jms.setUsername(from);
        jms.setPassword(password);
        jms.setDefaultEncoding("Utf-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.connectiontimeout", "5000");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.writetimeout", "5000");
        jms.setJavaMailProperties(properties);

        try {
            MimeMessage mimeMessage = jms.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from,sender);                //sender为自定义显示发件人名称
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            jms.send(mimeMessage);
            logger.info("send success.................");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }
        

    }

    @Override
    public void sendMimeMessge(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            logger.info("send success.................");
        } catch (MessagingException e) {
            logger.error("发送MimeMessge时发生异常！", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }
    }

    @Override
    public void sendMimeMessge(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            logger.info("send success.................");
        } catch (MessagingException e) {
            logger.error("发送带附件的MimeMessge时发生异常！", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }
    }

    @Override
    public void sendMimeMessge(String to, String subject, String content, Map<String, String> rscIdMap) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            for (Map.Entry<String, String> entry : rscIdMap.entrySet()) {
                FileSystemResource file = new FileSystemResource(new File(entry.getValue()));
                helper.addInline(entry.getKey(), file);
            }
            mailSender.send(message);
            logger.info("send success.................");
        } catch (MessagingException e) {
            logger.error("发送带静态文件的MimeMessge时发生异常！", e);
            throw new MallException(MallExceptionEnum.SEND_EMAIL_ERROR);
        }
    }
}
