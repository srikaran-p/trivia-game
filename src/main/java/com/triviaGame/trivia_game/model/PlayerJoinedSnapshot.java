package com.triviaGame.trivia_game.model;

public class PlayerJoinedSnapshot {
    private final Long playerId;
    private final String playerName;
    private final String token;

    public PlayerJoinedSnapshot(Long playerId, String playerName, String token) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.token = token;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getToken() {
        return token;
    }
}
