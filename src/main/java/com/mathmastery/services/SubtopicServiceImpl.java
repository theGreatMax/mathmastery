package com.mathmastery.services;

import com.mathmastery.models.Subtopic;
import com.mathmastery.repositories.SubtopicRepository;
import com.mathmastery.repositories.SubtopicService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubtopicServiceImpl implements SubtopicService {


    private final SubtopicRepository subtopicRepository;

    protected SubtopicServiceImpl(SubtopicRepository subtopicRepository) {
        this.subtopicRepository = subtopicRepository;
    }

    @Override
    public Subtopic findBySlugAndTopicSlug(String topicSlug , String subtopicSlug) {
        return subtopicRepository.findSubtopicByTopic_TopicSlugAndSlug(topicSlug , subtopicSlug)
                .orElseThrow(() -> new RuntimeException("Subtopic not found"));
    }

}
