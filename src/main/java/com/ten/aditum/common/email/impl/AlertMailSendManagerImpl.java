package com.ten.aditum.common.email.impl;

import com.ten.email.AlertMailSendManager;
import com.ten.email.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class AlertMailSendManagerImpl implements AlertMailSendManager {

    @Value("email@163.com")
    private String sender;

//    private final JavaMailSender mailSender;
//
//    @Autowired
//    public AlertMailSendManagerImpl(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    @Override
    public void sendEmail(SimpleEmail simpleEmail) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, simpleEmail.isAttachment());
//        /*
//         * 添加发送者
//         */
//        helper.setFrom(sender);
//        /*
//         * 添加接收者
//         */
//        Set<String> toSet = simpleEmail.getAccepter();
//        helper.setTo(toSet.toArray(new String[0]));
//        /*
//         * 添加主题 设置编码
//         */
//        BASE64Encoder base64 = new BASE64Encoder();
//        try {
//            helper.setSubject(base64.encode((simpleEmail.getSubject()).getBytes("UTF-8")));
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException();
//        }
//        /*
//         * 添加正文
//         */
//        try {
//            helper.setText(base64.encode((simpleEmail.getContent()).getBytes("UTF-8")), simpleEmail.isHtml());
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException();
//        }
//        /*
//         * 添加附件
//         */
//        if (simpleEmail.isAttachment()) {
//            Map<String, File> attachments = simpleEmail.getAttachments();
//
//            if (attachments != null) {
//                for (Map.Entry<String, File> attach : attachments.entrySet()) {
//                    helper.addAttachment(attach.getKey(), attach.getValue());
//                }
//            }
//
//        }
//        mailSender.send(message);
    }
}

