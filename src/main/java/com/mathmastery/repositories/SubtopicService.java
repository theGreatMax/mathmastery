package com.mathmastery.repositories;

import com.mathmastery.models.Subtopic;

import java.util.Optional;

public interface SubtopicService {
    Subtopic findBySlugAndTopicSlug(String subtopicSlug, String topicSlug);

}

