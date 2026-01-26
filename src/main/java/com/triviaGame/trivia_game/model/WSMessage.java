package com.triviaGame.trivia_game.model;

import java.util.Map;

public class WSMessage {

    private String type;
    private Map<String, Object> payload;

    public WSMessage(String type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
