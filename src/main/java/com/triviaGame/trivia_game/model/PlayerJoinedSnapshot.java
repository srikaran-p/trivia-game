package com.triviaGame.trivia_game.model;

public class PlayerJoinedSnapshot {
    private final Long playerId;
    private final String playerName;

    public PlayerJoinedSnapshot(Long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
