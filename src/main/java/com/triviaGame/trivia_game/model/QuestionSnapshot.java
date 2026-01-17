package com.triviaGame.trivia_game.model;

public class QuestionSnapshot {
    private final Long questionId;
    private final String questionText;
    private final long startTimestamp;
    private final long endTimestamp;

    public QuestionSnapshot(Long questionId,
                            String questionText,
                            long startTimestamp,
                            long endTimestamp) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }
}

