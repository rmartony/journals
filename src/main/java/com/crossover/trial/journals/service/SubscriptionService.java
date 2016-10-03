package com.crossover.trial.journals.service;

import com.crossover.trial.journals.model.User;

import java.util.List;

public interface SubscriptionService {

    List<User> getUsersSubscribedInCategory(Long categoryId);

}
