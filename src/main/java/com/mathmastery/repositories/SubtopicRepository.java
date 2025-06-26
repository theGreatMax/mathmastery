package com.mathmastery.repositories;

import com.mathmastery.models.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, Long> {
    List<Subtopic> findById(long subtopicID);
    Optional<Subtopic> findSubtopicByTopic_TopicSlugAndSlug(String topicSlug, String Slug );
    List<Subtopic> findByTopic_id(long topicID);


    Subtopic findSubtopicsBySlug(String slug);
}
