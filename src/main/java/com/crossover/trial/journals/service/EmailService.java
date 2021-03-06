package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rmartony on 2/18/2017.
 */
@Service
public class EmailService {

    private final static Logger log = Logger.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendMessage(String[] to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        try {
            if (log.isDebugEnabled()) {
                log.debug("About to send email to: ");
                if (to != null && to.length > 0) {
                    log.debug(Arrays.toString(msg.getTo()));
                }
                log.debug("with subject '" + subject + "' and body '" + text + "'");
            }
            mailSender.send(msg);
        } catch (MailException e) {
            throw new ServiceException("Error ocurred while sending email message to " + Arrays.toString(msg.getTo()), e);
        }
    }

    public void sendMessage(List<User> userList, String subject, String text) {
        List<String> emailList = new ArrayList<>(userList.size());
        userList.forEach(u -> emailList.add(u.getEmail()));
        sendMessage(emailList.toArray(new String[]{}), subject, text);
    }

}
