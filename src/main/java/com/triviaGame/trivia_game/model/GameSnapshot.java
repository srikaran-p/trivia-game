package com.triviaGame.trivia_game.model;

import java.util.List;

public class GameSnapshot {
    private final GameState gameState;
    private final QuestionState questionState;
    private final QuestionSnapshot questionSnapshot;
    private final List<LeaderboardEntry> leaderboard;
    private final int currentQuestionIndex;

    public GameSnapshot(GameState gameState, QuestionState questionState, int currentQuestionIndex,
                        QuestionSnapshot questionSnapshot, List<LeaderboardEntry> leaderboard) {
        this.gameState = gameState;
        this.questionState = questionState;
        this.currentQuestionIndex = currentQuestionIndex;
        this.questionSnapshot = questionSnapshot;
        this.leaderboard = leaderboard;
    }

    public GameState getGameState() {
        return gameState;
    }

    public QuestionState getQuestionState() {
        return questionState;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public QuestionSnapshot getQuestionSnapshot() {
        return questionSnapshot;
    }

    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }
}