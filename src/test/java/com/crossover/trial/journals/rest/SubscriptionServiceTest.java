package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.User;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    public void getUsersSubscribedInCategory() {
        List<User> users = subscriptionService.getUsersSubscribedInCategory(3L);
        assertNotNull(users);
        assertEquals(1, users.size());
    }

}
