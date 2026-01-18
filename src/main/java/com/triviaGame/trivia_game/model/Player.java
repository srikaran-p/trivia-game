package com.triviaGame.trivia_game.model;

public class Player {

    private Long id;
    private String name;
    private String token;
    private boolean hasCorrectlyAnswered;
    private Long correctlyAnsweredTimestamp;
    private int score = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getHasCorrectlyAnswered() {
        return hasCorrectlyAnswered;
    }

    public void setHasCorrectlyAnswered(boolean hasCorrectlyAnswered) {
        this.hasCorrectlyAnswered = hasCorrectlyAnswered;
    }

    public Long getCorrectlyAnsweredTimestamp() {
        return correctlyAnsweredTimestamp;
    }

    public void setCorrectlyAnsweredTimestamp(Long correctlyAnsweredTimestamp) {
        this.correctlyAnsweredTimestamp = correctlyAnsweredTimestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
