package com.triviaGame.trivia_game.model.room;

import com.triviaGame.trivia_game.model.Game;
import com.triviaGame.trivia_game.model.Player;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import com.triviaGame.trivia_game.service.GameService;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {

    private final Map<String, GameContext> rooms = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Player> sessionByPlayer = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, GameContext> sessionByRoom = new ConcurrentHashMap<>();

    public String createRoom() {
        String roomId = UUID.randomUUID().toString();

        Game game = new Game();
        GameService gameService = new GameService(game);
        GameOrchestrator orchestrator = new GameOrchestrator(game);

        rooms.put(roomId, new GameContext(game, gameService, orchestrator));
        return roomId;
    }

    public GameContext getRoom(String roomId) {
        GameContext context = rooms.get(roomId);
        if (context == null) {
            throw new RuntimeException("Room not found!");
        }

        return context;
    }

    public void removeSession(WebSocketSession session) {
        GameContext context = sessionByRoom.get(session);
        if (context.removeSession(session)) {
            sessionByPlayer.remove(session);
            sessionByRoom.remove(session);
        }
    }

    public void populateSessionMap(WebSocketSession session, GameContext context, Player player) {
        sessionByPlayer.put(session, player);
        sessionByRoom.put(session, context);
    }

    public Player getPlayerBySession(WebSocketSession session) {
        return sessionByPlayer.get(session);
    }

    public GameContext getRoomBySession(WebSocketSession session) {
        return sessionByRoom.get(session);
    }
}
