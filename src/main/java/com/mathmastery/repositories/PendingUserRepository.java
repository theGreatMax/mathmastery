package com.mathmastery.repositories;

import com.mathmastery.models.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    PendingUser findByVerificationToken(String token);
}
