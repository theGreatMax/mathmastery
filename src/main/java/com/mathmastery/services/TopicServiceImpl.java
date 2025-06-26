package com.mathmastery.services;

import com.mathmastery.models.Topic;
import com.mathmastery.repositories.TopicRepository;
import com.mathmastery.repositories.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;


    public List<Topic> getTopicsWithProgress(int grade) {
        return topicRepository.findByGrade(grade);
    }
}

