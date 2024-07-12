package com.lld;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class SnakeAndLadder {


    public class Game {
        private Board board;

        private Dice dice;

        private Map<Player, Integer> playersPositionMap;

        private LinkedList<Player> playerTurns;

        public void makeMove() {}

    }

    public class Board {

        private Integer totalCells;

        private Map<Integer, Integer> snakeStartEndMap;

        private Map<Integer, Integer> ladderStartEndMap;

    }

    public class Dice {

        private final Random random = new Random();

        private final Integer DICE_SIZE = 6;

        public Integer rollDice() {
            return random.nextInt(DICE_SIZE + 1);
        }

    }

    public class Player {

        private String aliasName;

    }

    public enum CellType {
        REGULAR,
        SNAKE,
        LADDER
    }

}

