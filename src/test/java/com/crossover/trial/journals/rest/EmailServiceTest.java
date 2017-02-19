package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.EmailService;
import com.crossover.trial.journals.service.SubscriptionService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    public void sendSingleEmailMessageTest() {
        String[] to = new String[]{"yyy@mailinator.com"};
        emailService.sendMessage(to, "Test subject", "This is a test email");
    }

    @Test
    public void sendEmailMessage4UserCategoryTest() {
        final Long categoryId = 3L;

        List<User> userList = subscriptionService.getUsersSubscribedInCategory(categoryId);

        emailService.sendMessage(userList, "Test subject", "This is a test email");
    }

}
