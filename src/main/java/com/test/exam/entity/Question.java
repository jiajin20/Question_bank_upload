package com.test.exam_test.entity;


import javax.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private String answer;

    public Question() {}

    public Question(String questionText, String answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    public Long getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getAnswer() { return answer; }
}
