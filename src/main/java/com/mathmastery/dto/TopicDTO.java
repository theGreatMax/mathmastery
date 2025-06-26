package com.mathmastery.dto;

import com.mathmastery.models.Subtopic;
import com.mathmastery.models.Topic;

import java.sql.Time;
import java.util.List;

public class TopicDTO {
    public String title;
    public String description;
    public String iconClass;
    public String colorClass;
    public String topicSlug;
    public List<Subtopic> subtopics;
    public int grade;

    public String timeSpentFormatted;

    public TopicDTO(Topic topic) {
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.iconClass = topic.getIconClass();
        this.colorClass = topic.getColorClass();
        this.topicSlug = topic.getTopicSlug();
        this.subtopics = topic.getSubtopics();
        this.grade = topic.getGrade();
    }
}
