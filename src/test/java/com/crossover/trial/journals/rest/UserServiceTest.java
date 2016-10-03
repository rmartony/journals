package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.ServiceException;
import com.crossover.trial.journals.service.UserService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserByLoginNameSuccess() {
        Optional<User> user = userService.getUserByLoginName("user1");
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals("user1", user.get().getLoginName());
    }

    @Test
    public void getUserByLoginNameFail() {
        Optional<User> user = userService.getUserByLoginName("unknown");
        assertFalse(user.isPresent());
    }

    @Test
    public void getUserByIdSuccess() {
        User user = userService.findById(1L);
        assertNotNull(user);
        assertEquals(new Long(1), user.getId());
    }

    @Test
    public void getUserByIdFail() {
        User user = userService.findById(12L);
        assertNull(user);
    }

    @Test
    public void subscribeUserSuccess() {
        Optional<User> user = userService.getUserByLoginName("publisher1");
        assertTrue(user.isPresent());

        // subscribe publisher1 to category id 2
        final Long categoryId = 2L;
        try {
            userService.subscribe(user.get(), categoryId);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // verify publisher1 is subscribed to category id 2
        Optional<User> user2 = userService.getUserByLoginName("publisher1");
        assertTrue(user2.isPresent());
        List<Subscription> subscriptions = user2.get().getSubscriptions();
        subscriptions.forEach(s -> assertTrue(s.getCategory().getId().equals(categoryId)));

    }

    @Test(expected = ServiceException.class)
    public void subscribeUserFail() throws ServiceException {
        Optional<User> user = userService.getUserByLoginName("publisher2");
        assertTrue(user.isPresent());
        final Long categoryId = 12L;

        userService.subscribe(user.get(), categoryId);
    }


}
