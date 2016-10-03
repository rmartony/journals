package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final static Logger log = Logger.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public List<User> getUsersSubscribedInCategory(Long categoryId) {
        List<Subscription> subscriptionList = subscriptionRepository.findByCategoryIdIn(categoryId);
        if (subscriptionList != null) {
            List<User> userList = new ArrayList<>(subscriptionList.size());
            subscriptionList.forEach(s -> userList.add(s.getUser()));
            return userList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

}
