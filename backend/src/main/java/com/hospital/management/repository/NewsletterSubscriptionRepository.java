package com.hospital.management.repository;

import com.hospital.management.domain.entity.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, UUID> {
    Optional<NewsletterSubscription> findByEmailIgnoreCase(String email);
}
