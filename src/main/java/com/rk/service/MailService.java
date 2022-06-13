package com.rk.service;

import com.rk.Exception.SpringStoreException;
import com.rk.model.NotificationMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private JavaMailSender javaMailSender;
//    @Async
//    public void sendMail( String recipient,String msg){
//        MimeMessagePreparator mimeMessagePreparator= mimeMessage -> {
//            MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage);
//            messageHelper.setFrom("amazon@email.com");
//            messageHelper.setTo(recipient);
//            messageHelper.setSubject("Account activation");
//            messageHelper.setText(mailContentBuilder.build(msg));
//        };
//
//        try{
//            javaMailSender.send(mimeMessagePreparator);
//        }catch(MailException ex){
//            throw new SpringStoreException("Error while sending mail");
//        }
//    }

       void sendMail(NotificationMail notificationMail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationMail.getTo());
            messageHelper.setSubject(notificationMail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationMail.getContent()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            throw new SpringStoreException("Exception occurred when sending mail to " + notificationMail.getTo());
        }
    }
}

