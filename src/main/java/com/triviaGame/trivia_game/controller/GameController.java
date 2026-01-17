package com.triviaGame.trivia_game.controller;

import com.triviaGame.trivia_game.model.GameSnapshot;
import com.triviaGame.trivia_game.model.LeaderboardEntry;
import com.triviaGame.trivia_game.model.PlayerJoinedSnapshot;
import com.triviaGame.trivia_game.model.QuestionSnapshot;
import com.triviaGame.trivia_game.orchestrator.GameOrchestrator;
import org.springframework.web.bind.annotation.*;
import com.triviaGame.trivia_game.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final GameOrchestrator orchestrator;

    public GameController(GameService gameService, GameOrchestrator orchestrator) {
        this.gameService = gameService;
        this.orchestrator = orchestrator;
    }

    @PostMapping("/start")
    public void startGame() {
        gameService.startGame();
        orchestrator.startGame();
    }

    @PostMapping("/join")
    public PlayerJoinedSnapshot join(@RequestParam String name) {
        return gameService.joinGame(name);
    }

    @GetMapping("/state")
    public GameSnapshot getState() {
        return gameService.getGameState();
    }

    @GetMapping("/question")
    public QuestionSnapshot getQuestion() {
        return gameService.getCurrentQuestion();
    }

    @PostMapping("/answer")
    public boolean submitAnswer(@RequestParam Long playerId, @RequestParam String answer) {
        return gameService.submitAnswer(playerId, answer);
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard() {
        return gameService.getLeaderboard();
    }
}
