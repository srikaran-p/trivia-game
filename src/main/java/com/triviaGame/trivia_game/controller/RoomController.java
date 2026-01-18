package com.triviaGame.trivia_game.controller;

import com.triviaGame.trivia_game.model.GameSnapshot;
import com.triviaGame.trivia_game.model.LeaderboardEntry;
import com.triviaGame.trivia_game.model.PlayerJoinedSnapshot;
import com.triviaGame.trivia_game.model.QuestionSnapshot;
import com.triviaGame.trivia_game.model.room.GameContext;
import com.triviaGame.trivia_game.model.room.RoomManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    private RoomManager roomManager;

    public RoomController(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @PostMapping("/rooms")
    public String createRoom() {
        return roomManager.createRoom();
    }

    @PostMapping("/rooms/{roomId}/join")
    public PlayerJoinedSnapshot joinRoom(@PathVariable String roomId, @RequestParam String name) {
        GameContext context = roomManager.getRoom(roomId);
        return context.getGameService().joinGame(name);
    }

    @GetMapping("/rooms/{roomId}/start")
    public void start(@PathVariable String roomId) {
        GameContext context = roomManager.getRoom(roomId);
        context.getGameService().startGame();
        context.getOrchestrator().startGame();
    }

    @GetMapping("/rooms/{roomId}/state")
    public GameSnapshot getState(@PathVariable String roomId) {
        GameContext context = roomManager.getRoom(roomId);
        return context.buildSnapshot();
    }

    @GetMapping("/rooms/{roomId}/question")
    public QuestionSnapshot getQuestion(@PathVariable String roomId) {
        GameContext context = roomManager.getRoom(roomId);
        return context.getGameService().getCurrentQuestion();
    }

    @PostMapping("/rooms/{roomId}/answer")
    public boolean submitAnswer(@PathVariable String roomId, @RequestParam Long playerId,
                                @RequestParam String answer) {
        GameContext context = roomManager.getRoom(roomId);
        return context.getGameService().submitAnswer(playerId, answer);
    }

    @GetMapping("/rooms/{roomId}/leaderboard")
    public List<LeaderboardEntry> getLeaderboard(@PathVariable String roomId) {
        GameContext context = roomManager.getRoom(roomId);
        return context.getGameService().getLeaderboard();
    }
}
