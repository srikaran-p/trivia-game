package com.triviaGame.trivia_game.model;

public class GameSnapshot {
    private final GameState gameState;
    private final QuestionState questionState;
    private final int currentQuestionIndex;

    public GameSnapshot(GameState gameState,
                        QuestionState questionState,
                        int currentQuestionIndex) {
        this.gameState = gameState;
        this.questionState = questionState;
        this.currentQuestionIndex = currentQuestionIndex;
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
}