package com.triviaGame.trivia_game.orchestrator;

import com.triviaGame.trivia_game.model.Game;
import com.triviaGame.trivia_game.model.GameState;
import com.triviaGame.trivia_game.model.Question;
import com.triviaGame.trivia_game.model.QuestionState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameOrchestrator {

    private final Game game;
    private final ScheduledExecutorService scheduler;

    public GameOrchestrator(Game game) {
        this.game = game;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startGame() {
        final List<Question> questions = new ArrayList<>();
        questions.add(new Question(1L, "What is first letter?", "A"));
        questions.add(new Question(2L, "What is second letter?", "B"));
        questions.add(new Question(3L, "What is third letter?", "C"));
        questions.add(new Question(4L, "What is fourth letter?", "D"));
        game.setQuestions(questions);

        startNextQuestion();
    }

    private void startNextQuestion() {
        if (game.getGameState() == GameState.ENDED) {
            scheduler.shutdown();
            return;
        }

        game.startQuestion();

        scheduler.schedule(this::onQuestionTimeUp, 10, TimeUnit.SECONDS);
    }

    private void onQuestionTimeUp() {
        if (game.getQuestionState() != QuestionState.IN_PROGRESS) {
            return;
        }

        game.endQuestion();
        game.scoreQuestion();
        game.advanceGame();

        startNextQuestion();
    }
}
