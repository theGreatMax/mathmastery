package com.mathmastery.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="topics")
public class Topic {
    @Id
    @GeneratedValue
    private Long id;
    @Column()
    private int grade;
    @Column()
    private String title;
    @Column()
    private String description;
    @Column()
    private String iconClass;
    @Column()
    private String colorClass;
    @Column()
    private String topicSlug;


    @OneToMany(mappedBy = "topic")
    private List<Subtopic> subtopics;

}


