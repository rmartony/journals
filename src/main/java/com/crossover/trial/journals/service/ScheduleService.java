package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by rmartony on 2/18/2017.
 */

public class ScheduleService {
    private final static Logger log = Logger.getLogger(ScheduleService.class);

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 22 * * *") // every day at 10PM
    public void sendDailyDigest() {
        List<Journal> journalList = journalService.findInPublishedDate(LocalDate.now());
        if (journalList != null && !journalList.isEmpty()) {
            List<User> userList = userRepository.findAll();

            StringBuilder message = new StringBuilder();
            for (Journal journal : journalList) {
                message.append(journal.getJournalInfo());
            }
            emailService.sendMessage(userList, "Daily digest", message.toString());
        }
    }
}
