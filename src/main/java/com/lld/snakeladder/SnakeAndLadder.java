package com.lld.snakeladder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
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
 * <li>Player ratings</li>
 * <li>Game Tournaments</li>
 */
public class SnakeAndLadder {

    public static void main(String[] args) {
    }

}

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
        System.out.println("Player " + currentPlayer.getAliasName() + " dice value " + score);
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
class Board {

    final Integer totalCells;

    final Map<Integer, Cell> ladderAndSnakeCells;

    public Board(Integer totalCells, Map<Integer, Integer> snakeStartEndMap, Map<Integer, Integer> ladderStartEndMap) {
        this.totalCells = totalCells;
        this.ladderAndSnakeCells = new HashMap<>();
        snakeStartEndMap.forEach((start, end) -> ladderAndSnakeCells.put(start, new SnakeCell(start, end)));
        ladderStartEndMap.forEach((start, end) -> ladderAndSnakeCells.put(start, new LadderCell(start, end)));
    }

    public Board(Integer totalCells) {
        this(totalCells, new HashMap<>(), new HashMap<>());
    }

    public int getNextPosition(int score, int currentPosition) {
        int nextPosition = score + currentPosition;
        if (nextPosition > this.totalCells) {
            System.out.println("Score not valid to finish game");
            return currentPosition;
        }
        if (ladderAndSnakeCells.containsKey(nextPosition)) {
            System.out.println(ladderAndSnakeCells.get(nextPosition).getCellType());
            nextPosition = ladderAndSnakeCells.get(nextPosition).getEnd();
        }
        return nextPosition;
    }

}

@Getter
abstract class Cell {
    private final int start;
    private final int end;

    public Cell(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public abstract String getCellType();

    public int getEnd() {
        return this.end;
    }
}

class SnakeCell extends Cell {

    public SnakeCell(int start, int end) {
        super(start, end);
    }

    public String getCellType() {
        return "---------Snake";
    }

}

class LadderCell extends Cell {

    public LadderCell(int start, int end) {
        super(start, end);
    }

    public String getCellType() {
        return "Ladder--------";
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

    private int currentPosition;

    public Player(String aliasName) {
        this.aliasName = aliasName;
        this.currentPosition = 0;
    }

}

enum GameStatus {
    ONGOING,
    FINISHED
}

