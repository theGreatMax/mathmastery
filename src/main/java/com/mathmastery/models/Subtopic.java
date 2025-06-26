package com.mathmastery.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="subtopics")
@Setter
@Getter
public class Subtopic {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name ="title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name ="slug")
    private String slug;

    @OneToMany(mappedBy = "subtopic", cascade = CascadeType.ALL)
    private List<Question> questions;



}
