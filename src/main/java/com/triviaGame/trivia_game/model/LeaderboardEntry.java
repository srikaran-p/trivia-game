package com.triviaGame.trivia_game.model;

public class LeaderboardEntry {
    private final Long playerId;
    private final String playerName;
    private final int score;

    public LeaderboardEntry(Long playerId, String playerName, int score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
