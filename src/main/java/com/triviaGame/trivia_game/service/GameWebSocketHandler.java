package com.triviaGame.trivia_game.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triviaGame.trivia_game.config.WebSocketConfig;
import com.triviaGame.trivia_game.model.GameSnapshot;
import com.triviaGame.trivia_game.model.Player;
import com.triviaGame.trivia_game.model.WSMessage;
import com.triviaGame.trivia_game.model.room.GameContext;
import com.triviaGame.trivia_game.model.room.RoomManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final RoomManager roomManager;
    private final ObjectMapper objectMapper;

    public GameWebSocketHandler(RoomManager roomManager) {
        this.roomManager = roomManager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        Map<String, String> params = parseQueryParams(uri.getQuery());

        String roomId = params.get("roomId");
        String token = params.get("token");

        if (roomId == null || roomId.isBlank() || token == null || token.isBlank()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        authenticate(session, roomId, token);

        GameContext gameContext = roomManager.getRoom(roomId);
        GameSnapshot gameSnapshot = gameContext.buildSnapshot();

        send(session, new WSMessage("SNAPSHOT", gameSnapshot));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("Received: " + message.getPayload());

        session.sendMessage(new TextMessage("ACK: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        roomManager.removeSession(session);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }

        return params;
    }

    private void authenticate(WebSocketSession session, String roomId, String token) throws Exception {
        GameContext gameContext = roomManager.getRoom(roomId);

        if (gameContext == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Room not found"));
            return;
        }

        Optional<Player> playerOpt = gameContext.getPlayerByToken(token);
        if (playerOpt.isEmpty()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Player not found"));
            return;
        }

        Player player = playerOpt.get();

        gameContext.attachSession(player.getId(), session);

        System.out.println("Authenticated WS: player = " + player.getName() + ", room = " + roomId);
        send(session, "CONNECTED");
    }

    private void send(WebSocketSession session, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            System.out.println("Could not send message" + payload);
        }
    }
}
