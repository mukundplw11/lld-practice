package com.lld;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Functionalities Implemented:
 *
 * <li>Game start with multiple players support</li>
 * <li>Rankings announce</li>
 * <li>Get next player turn</li>
 * <li>Variable board size support</li>
 * <li>Two types of cell support - Snake & Ladder</li>
 *
 * <br>
 * Functionalities that could be added:
 * <li>Undo support by storing moves of player</li>
 * <li>Could extend Cell to interfaces to make it extensible for different types of cells</li>
 * <li>Player ratings</li>
 * <li>Game Tournaments</li>
 */
public class SnakeAndLadder {


    class Game {
        private final Board board;

        private final Dice dice;

        private GameStatus gameStatus;

        private final LinkedList<Player> playerRankingsList = new LinkedList<>();

        private final Deque<Player> playerTurns;

        public Game(Board board, Dice dice, List<Player> players) {
            this.board = board;
            this.dice = dice;
            this.playerTurns = new ArrayDeque<>(players);
            this.gameStatus = GameStatus.ONGOING;
        }

        public void makeMove() {
            if (GameStatus.FINISHED.equals(this.gameStatus)) {
                throw new RuntimeException("Game already finished");
            }
            Player currentPlayer = this.playerTurns.poll();
            if (currentPlayer.getCurrentPosition() >= this.board.getTotalCells()) {
                throw new RuntimeException("Player already reached the end");
            }
            int score = this.dice.rollDice();
            int nextPosition = this.board.getNextPosition(score, currentPlayer.getCurrentPosition());
            updatePlayersAndCheckGameStatus(nextPosition, currentPlayer);
        }

        private void updatePlayersAndCheckGameStatus(int nextPosition, Player currentPlayer) {
            currentPlayer.setCurrentPosition(nextPosition);
            if (nextPosition == this.board.getTotalCells()) {
                this.playerRankingsList.addLast(currentPlayer);
            } else {
                this.playerTurns.addLast(currentPlayer);
            }
            if (this.playerTurns.size() == 1) {
                this.playerRankingsList.addLast(this.playerTurns.poll());
            }
            if (this.playerTurns.isEmpty()) {
                this.gameStatus = GameStatus.FINISHED;
            }
        }

        public Player getNextPlayerTurn() {
            if (GameStatus.FINISHED.equals(this.gameStatus)) {
                throw new RuntimeException("Game already finished");
            }
            return this.playerTurns.peek();
        }

    }

    @Getter
    @Setter
    class Board {

        /* Could add List of Cells -> Cells will have 3 implementation -> Normal, Snake & Ladder Cell */
        private Integer totalCells;

        private Map<Integer, Integer> snakeStartEndMap;

        private Map<Integer, Integer> ladderStartEndMap;

        public Board(Integer totalCells, Map<Integer, Integer> snakeStartEndMap, Map<Integer, Integer> ladderStartEndMap) {
            this.totalCells = totalCells;
            this.snakeStartEndMap = snakeStartEndMap;
            this.ladderStartEndMap = ladderStartEndMap;
        }

        public Board(Integer totalCells) {
            this(totalCells, Collections.emptyMap(), Collections.emptyMap());
        }

        public int getNextPosition(int score, int currentPosition) {
            int nextPosition = score + currentPosition;
            if (snakeStartEndMap.get(nextPosition) != null) {
                nextPosition = snakeStartEndMap.get(nextPosition);
            } else if (ladderStartEndMap.get(nextPosition) != null) {
                nextPosition = ladderStartEndMap.get(nextPosition);
            }
            return nextPosition;
        }

    }

    class Dice {

        private final Random random = new Random();

        private final Integer DICE_SIZE = 6;

        public Integer rollDice() {
            return 1 + random.nextInt(DICE_SIZE);
        }

    }

    @Getter
    @Setter
    class Player {

        private String aliasName;

        private Integer currentPosition;

    }

    enum GameStatus {
        ONGOING,
        FINISHED
    }

}

