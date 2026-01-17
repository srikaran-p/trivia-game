package com.triviaGame.trivia_game.model.room;

import com.triviaGame.trivia_game.model.Game;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import com.triviaGame.trivia_game.service.GameService;

public class GameContext {

    private final Game game;
    private final GameService gameService;
    private final GameOrchestrator orchestrator;

    public GameContext(Game game, GameService gameService, GameOrchestrator orchestrator) {
        this.game = game;
        this.gameService = gameService;
        this.orchestrator = orchestrator;
    }

    public GameService getGameService() {
        return gameService;
    }

    public GameOrchestrator getOrchestrator() {
        return orchestrator;
    }
}
