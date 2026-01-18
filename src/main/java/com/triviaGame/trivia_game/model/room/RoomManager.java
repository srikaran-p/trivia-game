package com.triviaGame.trivia_game.model.room;

import com.triviaGame.trivia_game.model.Game;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import com.triviaGame.trivia_game.service.GameService;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {

    private final Map<String, GameContext> rooms = new ConcurrentHashMap<>();

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
        for (GameContext context : rooms.values()) {
            if (context.removeSession(session)) {
                return;
            }
        }
    }
}
