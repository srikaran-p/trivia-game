package com.triviaGame.trivia_game.model.room;

import com.triviaGame.trivia_game.model.*;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import com.triviaGame.trivia_game.service.GameService;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

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

    public void attachSession(Long playerId, WebSocketSession session) {
        game.attachSession(playerId, session);
    }

    public boolean removeSession(WebSocketSession session) {
        return game.detachSession(session);
    }

    public GameSnapshot buildSnapshot() {
        synchronized (game.getLock()) {
            List<LeaderboardEntry> leaderboard = gameService.getLeaderboard();
            QuestionSnapshot questionSnapshot = gameService.getCurrentQuestion();
            GameState gameState = game.getGameState();
            QuestionState questionState = game.getQuestionState();
            int currentQuestionIndex = game.getCurrentQuestionIndex();

            return new GameSnapshot(gameState, questionState, currentQuestionIndex, questionSnapshot, leaderboard);
        }
    }

    public void onPlayerCorrectAnswer(Player player) {
        game.onPlayerCorrectAnswer(player);
    }

    public Optional<Player> getPlayerByToken(String token) {
        return game.getPlayerByToken(token);
    }
}
