package com.triviaGame.trivia_game.service;

import com.triviaGame.trivia_game.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameService {

    private Game game;
    private long playerIdCounter = 1;

    public GameService(Game game) {
        this.game = game;
    }

    public PlayerJoinedSnapshot joinGame(String playerName) {
        synchronized (game.getLock()) {
            if (game.getGameState() != GameState.WAITING) {
                throw new RuntimeException("Cannot join game once it has started");
            }

            Long playerId = playerIdCounter++;

            Player player = new Player();
            player.setId(playerId);
            player.setName(playerName);
            player.setScore(0);
            player.setHasCorrectlyAnswered(false);

            game.getPlayers().add(player);

            return new PlayerJoinedSnapshot(playerId, playerName);
        }
    }

    public GameSnapshot getGameState() {
        synchronized (game.getLock()) {
            return new GameSnapshot(game.getGameState(), game.getQuestionState(),
                                    game.getCurrentQuestionIndex());
        }
    }

    public QuestionSnapshot getCurrentQuestion() {
        synchronized (game.getLock()) {
            if (game.getQuestionState() != QuestionState.IN_PROGRESS) {
                return null;
            }

            Question currentQuestion = game.getCurrentQuestion();
            long start = currentQuestion.getStartTimestamp();
            long end = start + 10000;

            return new QuestionSnapshot(currentQuestion.getId(), currentQuestion.getQuestionText(),
                                        start, end);
        }
    }

    public Boolean submitAnswer(Long playerId, String answer) {
        return game.submitAnswer(playerId, answer);
    }

    public List<LeaderboardEntry> getLeaderboard() {
        synchronized (game.getLock()) {
            List<LeaderboardEntry> leaderboard = new ArrayList<>();

            for (Player player : game.getPlayers()) {
                leaderboard.add(new LeaderboardEntry(player.getId(), player.getName(),
                                                     player.getScore()));
            }

            leaderboard.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());

            return leaderboard;
        }
    }

    public void startGame() {
        synchronized (game.getLock()) {

            if (game.getGameState() != GameState.WAITING) {
                throw new RuntimeException("Game already started");
            }

            if (game.getPlayers().isEmpty()) {
                throw new RuntimeException("Cannot start game without players");
            }

            game.startGame();
        }
    }
}
