package com.triviaGame.trivia_game.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private final Object lock = new Object();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Player> players = new ArrayList<>();
    private List<Question> questions;
    private int currentQuestionIndex;
    private GameState gameState = GameState.WAITING;
    private QuestionState questionState;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void startGame() {
        currentQuestionIndex = 0;
        gameState = GameState.IN_PROGRESS;
        questionState = QuestionState.SENDING_QUESTION;
    }

    public void startQuestion() {
        synchronized (lock) {
            if (questionState != QuestionState.SENDING_QUESTION) {
                throw new RuntimeException("Question State should be SENDING_QUESTION for startQuestion");
            }

            final Question question = questions.get(currentQuestionIndex);

            final Long currentTime = System.currentTimeMillis();
            question.setStartTimestamp(currentTime);

            for (Player player : players) {
                player.setHasCorrectlyAnswered(false);
            }
            question.setAnswers(new HashMap<>());

            questionState = QuestionState.IN_PROGRESS;
        }
    }

    public void endQuestion() {
        synchronized (lock) {
            if (questionState != QuestionState.IN_PROGRESS) {
                throw new RuntimeException("Question State should be IN_PROGRESS for endQuestion");
            }

            questionState = QuestionState.COUNTING_SCORE;
        }
    }

    public void advanceGame() {
        synchronized (lock) {
            if (questionState != QuestionState.COUNTING_SCORE) {
                throw new RuntimeException("Question State should be COUNTING_SCORE for advanceGame");
            }

            if (currentQuestionIndex >= questions.size() - 1) {
                gameState = GameState.ENDED;
            } else {
                currentQuestionIndex++;
                questionState = QuestionState.SENDING_QUESTION;
            }
        }
    }

    public Boolean submitAnswer(Long playerId, String answer) {
        synchronized (lock) {
            if (gameState != GameState.IN_PROGRESS || questionState != QuestionState.IN_PROGRESS) {
                throw new RuntimeException("State not in progress for submitAnswer");
            }

            final Player currentPlayer = players.stream()
                                                .filter(player -> Objects.equals(playerId, player.getId()))
                                                .findFirst()
                                                .orElse(null);

            if (currentPlayer == null) {
                throw new RuntimeException("Player does not exist");
            }
            if (currentPlayer.getHasCorrectlyAnswered()) {
                throw new RuntimeException("Player has already answered correctly");
            }

            final Question currentQuestion = questions.get(currentQuestionIndex);
            final String correctAnswer = currentQuestion.getCorrectAnswer();

            if (!Objects.equals(answer, correctAnswer)) {
                return false;
            }

            final Long answeredTimestamp = System.currentTimeMillis();
            currentPlayer.setHasCorrectlyAnswered(true);
            currentPlayer.setCorrectlyAnsweredTimestamp(answeredTimestamp);
            currentQuestion.getAnswers().put(playerId, new Question.PlayerResponse(answer, answeredTimestamp));

            return true;
        }
    }

    public void scoreQuestion() {
        synchronized (lock) {
            if (questionState != QuestionState.COUNTING_SCORE) {
                throw new RuntimeException("Question not in COUNTING_SCORE state");
            }

            final Question question = questions.get(currentQuestionIndex);
            if (question == null || question.getStartTimestamp() == null) {
                throw new RuntimeException("Question does not have timestamp");
            }
            if (question.isHasScored()) {
                return;
            }

            final Map<Long, Question.PlayerResponse> answers = question.getAnswers();

            for (Player player : players) {
                final Long playerId = player.getId();
                final Question.PlayerResponse playerResponse = answers.get(playerId);
                if (playerResponse == null || !player.getHasCorrectlyAnswered()) {
                    continue;
                }

                final long timeDifference = playerResponse.getAnsweredTimestamp() - question.getStartTimestamp();
                final long timeDifferenceSeconds = timeDifference / 1000;
                final int scoreToAdd = (int) Math.max(0, 10 - timeDifferenceSeconds);
                player.setScore(player.getScore() + scoreToAdd);
            }

            question.setHasScored(true);
        }
    }

    public void attachSession(Long playerId, WebSocketSession session) {
        sessions.put(playerId, session);
        broadcastPlayerConnect(playerId);
    }

    public boolean detachSession(WebSocketSession session) {
        for (Map.Entry<Long, WebSocketSession> entry : sessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                sessions.remove(entry.getKey());
                broadcastPlayerDisconnect(entry.getKey());
                return true;
            }
        }

        return false;
    }

    public void onPlayerCorrectAnswer(Player player) {
        WSMessage payload = null;
        try {
            payload = new WSMessage("PLAYER_CORRECT", Map.of("playerId", player.getId(), "playerName", player.getName()));
            String payloadJson = objectMapper.writeValueAsString(payload);
            broadcast(payloadJson);
        } catch (Exception e) {
            System.out.println("Could not send message" + payload);
        }
    }

    private void broadcastPlayerConnect(Long playerId) {
        WSMessage payload = null;
        Player player = players.get((int) (playerId - 1));
        try {
            payload = new WSMessage("PLAYER_JOINED", Map.of("playerId", player.getId(), "playerName", player.getName()));
            String payloadJson = objectMapper.writeValueAsString(payload);
            broadcast(payloadJson);
        } catch (Exception e) {
            System.out.println("Could not send message" + payload);
        }
    }

    private void broadcastPlayerDisconnect(Long playerId) {
        WSMessage payload = null;
        Player player = players.get((int) (playerId - 1));
        try {
            payload = new WSMessage("PLAYER_LEFT", Map.of("playerId", player.getId(), "playerName", player.getName()));
            String payloadJson = objectMapper.writeValueAsString(payload);
            broadcast(payloadJson);
        } catch (Exception e) {
            System.out.println("Could not send message" + payload);
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (Exception ignored) {}
        });
    }

    public Optional<Player> getPlayerByToken(String token) {
        return players.stream()
                      .filter(player -> player.getToken().equals(token))
                      .findFirst();
    }

    public GameState getGameState() {
        return gameState;
    }

    public QuestionState getQuestionState() {
        return questionState;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Object getLock() {
        return lock;
    }

    // Remove later
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
