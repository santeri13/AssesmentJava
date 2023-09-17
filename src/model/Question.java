package model;

import java.util.List;
import java.util.ArrayList;

public class Question {
    private int id;
    private int topicId;
    private int difficulty;
    private String content;
    private List<Response> responses;

    public Question() {
        responses = new ArrayList<>();
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public void addResponse(Response response) {
        this.responses.add(response);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", difficulty=" + difficulty +
                ", content='" + content + '\'' +
                ", responses=" + responses +
                '}';
    }
}
