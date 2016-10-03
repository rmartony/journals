package com.crossover.trial.journals.service;

import com.crossover.trial.journals.controller.PublisherController;
import com.crossover.trial.journals.model.*;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JournalServiceImpl implements JournalService {

    private final static Logger log = Logger.getLogger(JournalServiceImpl.class);

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public List<Journal> listAll(User user) {
        User persistentUser = userRepository.findOne(user.getId());
        List<Subscription> subscriptions = persistentUser.getSubscriptions();
        if (subscriptions != null) {
            List<Long> ids = new ArrayList<>(subscriptions.size());
            subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
            return journalRepository.findByCategoryIdIn(ids);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Journal> publisherList(Publisher publisher) {
        Iterable<Journal> journals = journalRepository.findByPublisher(publisher);
        return StreamSupport.stream(journals.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Journal publish(Publisher publisher, Journal journal, Long categoryId) throws ServiceException {
        Journal result = null;
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            throw new ServiceException("Category not found");
        }
        journal.setPublisher(publisher);
        journal.setCategory(category);
        try {
            result = journalRepository.save(journal);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        // send emails
        List<User> userList = subscriptionService.getUsersSubscribedInCategory(categoryId);
        emailService.sendMessage(userList, "New Journal published", "Dear user, there is a new Journal published in the category (" + category.getName() + ") you are subscribed.");

        return result;
    }

    @Override
    public void unPublish(Publisher publisher, Long id) throws ServiceException {
        Journal journal = journalRepository.findOne(id);
        if (journal == null) {
            throw new ServiceException("Journal doesn't exist");
        }
        String filePath = PublisherController.getFileName(publisher.getId(), journal.getUuid());
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.error("File " + filePath + " cannot be deleted");
            }
        }
        if (!journal.getPublisher().getId().equals(publisher.getId())) {
            throw new ServiceException("Journal cannot be removed");
        }
        journalRepository.delete(journal);
    }

    @Override
    public List<Journal> findInPublishedDate(LocalDate date) {
        Date publishDate = java.sql.Date.valueOf(date);
        Date publishDateEnd = java.sql.Date.valueOf(date.plusDays(1));

        return journalRepository.findBetweenPublishDates(publishDate, publishDateEnd);
    }
}
