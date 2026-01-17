package com.triviaGame.trivia_game.config;

import com.triviaGame.trivia_game.model.Game;
import com.triviaGame.trivia_game.model.room.RoomManager;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.triviaGame.trivia_game.service.GameService;

@Configuration
public class GameConfig {

    @Bean
    public Game game() {
        return new Game();
    }

    @Bean
    public GameOrchestrator gameOrchestrator(Game game) {
        return new GameOrchestrator(game);
    }

    @Bean
    public GameService gameService(Game game) {
        return new GameService(game);
    }

    @Bean
    public RoomManager roomManager() {
        return new RoomManager();
    }
}
