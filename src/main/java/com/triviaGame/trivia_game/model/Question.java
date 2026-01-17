package com.triviaGame.trivia_game.model;

import java.util.HashMap;
import java.util.Map;

public class Question {

    private Long id;
    private String questionText;
    private String correctAnswer;
    private Long startTimestamp;
    private Map<Long, PlayerResponse> answers = new HashMap<>();
    private boolean hasScored = false;

    public Question(Long id, String questionText, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Map<Long, PlayerResponse> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, PlayerResponse> answers) {
        this.answers = answers;
    }

    public boolean isHasScored() {
        return hasScored;
    }

    public void setHasScored(boolean hasScored) {
        this.hasScored = hasScored;
    }

    public static class PlayerResponse {

        private String answerSubmitted;
        private Long answeredTimestamp;

        PlayerResponse(String answerSubmitted, Long answeredTimestamp) {
            this.answerSubmitted = answerSubmitted;
            this.answeredTimestamp = answeredTimestamp;
        }

        public String getAnswerSubmitted() {
            return answerSubmitted;
        }

        public void setAnswerSubmitted(String answerSubmitted) {
            this.answerSubmitted = answerSubmitted;
        }

        public Long getAnsweredTimestamp() {
            return answeredTimestamp;
        }

        public void setAnsweredTimestamp(Long answeredTimestamp) {
            this.answeredTimestamp = answeredTimestamp;
        }
    }
}
