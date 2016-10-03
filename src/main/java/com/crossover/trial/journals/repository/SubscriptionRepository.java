package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCategoryIdIn(Long categoryId);

}
