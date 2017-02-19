package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.ScheduleService;
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

import java.util.Optional;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleServiceTest {

    private final static String NEW_JOURNAL_NAME = "New Journal";
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;
    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void sendDailyDigestTest() {
        publishJournal();
        scheduleService.sendDailyDigest();
    }

    private void publishJournal() {
        User user = getUser("publisher2");
        Optional<Publisher> p = publisherRepository.findByUser(user);

        Journal journal = new Journal();
        journal.setName(NEW_JOURNAL_NAME);
        journal.setUuid("SOME_EXTERNAL_ID");
        try {
            journalService.publish(p.get(), journal, 3L);
        } catch (ServiceException e) {
            fail(e.getMessage());
        }
    }

    private User getUser(String name) {
        Optional<User> user = userService.getUserByLoginName(name);
        if (!user.isPresent()) {
            fail("user1 doesn't exist");
        }
        return user.get();
    }

}
