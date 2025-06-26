package com.mathmastery.repositories;

import com.mathmastery.dto.TopicDTO;
import com.mathmastery.models.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    List<TopicDTO> getTopicsWithProgress(int grade, Long userId);
    List<Topic> findByTopicSlug(String slug);

}
