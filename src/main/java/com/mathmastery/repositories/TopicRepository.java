package com.mathmastery.repositories;

import com.mathmastery.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByGrade(int grade);
    Topic findByTopicSlug(String topicSlug);

}

