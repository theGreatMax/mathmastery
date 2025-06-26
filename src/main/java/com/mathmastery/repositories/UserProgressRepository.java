package com.mathmastery.repositories;

import com.mathmastery.models.User;
import com.mathmastery.models.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress  , Long> {
}
