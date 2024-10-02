package com.lld;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * Functionalities Implemented:
 *
 * <li>Game start with multiple players support</li>
 * <li>Multiple symbol support</li>
 * <li>Winner announce</li>
 * <li>Get next player turn</li>
 * <li>Variable board size support</li>
 * <li>Update player stats - i.e. Win/Loss/Matches Played</li>
 *
 * <br>
 * Functionalities that could be added:
 * <li>Undo support by storing moves of player</li>
 * <li>Refactoring methods into user-player management services & game management services</li>
 * <li>Player ratings</li>
 * <li>Game Tournaments</li>
 */
public class TicTacToe {

    public Game startGame(List<Player> players, int boardSize) {
        GameBoard gameBoard = new GameBoard(boardSize);
        return new Game(gameBoard, players);
    }

    class Game {

        private final String id;

        private final LinkedList<Player> players = new LinkedList<>();

        private final GameBoard board;

        private Player winner;

        public Game(GameBoard board, List<Player> players) {
            board.setStatus(GameStatus.ONGOING);
            this.board = board;
            players.forEach(player -> {
                player.setMatchesPlayed(player.getMatchesPlayed() + 1);
                this.players.addLast(player);
            });
            this.id = Utils.generateId();
            board.setGameId(this.id);
        }

        public Player makeMove(int row, int col) {
            if (!GameStatus.ONGOING.equals(this.board.getStatus())) {
                throw new RuntimeException("Game is already finished with " + this.board.getStatus().name());
            }
            if (this.players.isEmpty()) {
                return null;
            }
            Player currentPlayer = this.players.peek();

            if (isInvalidMove(row, col, currentPlayer)) {
                throw new RuntimeException("Invalid move");
            }

            GameStatus gameStatus = this.board.markCellAndUpdateStatus(currentPlayer.getSign(), row, col);

            this.players.removeFirst();
            this.players.addLast(currentPlayer);

            if (GameStatus.WIN.equals(gameStatus)) {
                this.winner = currentPlayer;
                markPlayersWinLoss(currentPlayer, this.players);
            }

            return this.winner;
        }

        /* Can be managed more appropriately by using a proper user-player management service */
        private void markPlayersWinLoss(Player winner, Deque<Player> players) {
            winner.setWin(winner.getWin() + 1);
            players
                    .stream()
                    .filter(player -> !player.getId().equals(winner.getId()))
                    .forEach(player -> player.setLoss(player.getLoss() + 1));
        }

        private boolean isInvalidMove(int row, int col, Player currentPlayer) {
            return row < 0 || col < 0 || row >= this.board.getSize() || col >= this.board.getSize() || currentPlayer.getSign() == null;
        }

        public Player getNextPlayer() {
            return this.players.peek();
        }
    }


    @Getter
    @Setter
    class Player {

        private final String id;

        private String aliasName;

        private MarkingSymbol sign;

        private Integer loss = 0;

        private Integer win = 0;

        private Integer matchesPlayed = 0;


        public Player(String aliasName, MarkingSymbol sign) {
            this.aliasName = aliasName;
            this.sign = sign;
            this.id = Utils.generateId();
        }
    }

    @Getter
    @Setter
    class GameBoard {

        /**
         * Stores value at each board cell
         */
        private final MarkingSymbol[][] board;

        private Map<Integer, SymbolCounterDTO> rowAndSymbolCount;

        private Map<Integer, SymbolCounterDTO> colAndSymbolCount;

        private SymbolCounterDTO diagonalSymbolCount;

        private SymbolCounterDTO reverseDiagonalCount;

        private GameStatus status;

        private final int size;

        private int moves;

        private String gameId;

        private final String id;

        public GameBoard(int size) {
            this.size = size;
            this.board = new MarkingSymbol[size][size];
            this.moves = 0;
            this.rowAndSymbolCount = new HashMap<>();
            this.colAndSymbolCount = new HashMap<>();
            this.diagonalSymbolCount = null;
            this.reverseDiagonalCount = null;
            this.id = Utils.generateId();
        }

        public GameStatus markCellAndUpdateStatus(MarkingSymbol symbol, int row, int col) {
            this.board[row][col] = symbol;
            this.moves++;

            adjustRowOrColSymbolCounts(this.rowAndSymbolCount, row, symbol);
            adjustRowOrColSymbolCounts(this.colAndSymbolCount, col, symbol);

            if (row == col) {
                if (this.diagonalSymbolCount == null) {
                    this.diagonalSymbolCount = new SymbolCounterDTO(symbol);
                } else {
                    this.diagonalSymbolCount.setCount(symbol);
                }
            }

            if (row == this.size - 1 - col) {
                if (this.reverseDiagonalCount == null) {
                    this.reverseDiagonalCount = new SymbolCounterDTO(symbol);
                } else {
                    this.reverseDiagonalCount.setCount(symbol);
                }
            }
            if (isWinner(symbol, row, col)) {
                this.status = GameStatus.WIN;
            } else if (this.moves == this.size * this.size) {
                this.status = GameStatus.DRAW;
            }
            return this.status;
        }

        public boolean isWinner(MarkingSymbol symbol, int row, int col) {
            return isRowOrColMatch(this.colAndSymbolCount, symbol, col)
                    || isRowOrColMatch(this.rowAndSymbolCount, symbol, row)
                    || isDiagonalMatch(this.diagonalSymbolCount, symbol)
                    || isDiagonalMatch(this.reverseDiagonalCount, symbol);
        }

        private boolean isRowOrColMatch(Map<Integer, SymbolCounterDTO> indexAndCounterMap, MarkingSymbol symbol, int index) {
            return indexAndCounterMap.get(index) != null
                    && indexAndCounterMap.get(index).getSymbol().equals(symbol)
                    && indexAndCounterMap.get(index).getCount() == this.size;
        }

        private boolean isDiagonalMatch(SymbolCounterDTO diagonalSymbolCount, MarkingSymbol symbol) {
            return diagonalSymbolCount != null
                    && diagonalSymbolCount.getSymbol().equals(symbol)
                    && diagonalSymbolCount.getCount() == this.size;
        }

        private void adjustRowOrColSymbolCounts(Map<Integer, SymbolCounterDTO> indexAndCounterMap, int idx, MarkingSymbol symbol) {
            if (indexAndCounterMap.get(idx) != null) {
                /* If any other symbol marked on the row, then no one can be winner so marking -1 */
                indexAndCounterMap.get(idx).setCount(symbol);
            } else {
                indexAndCounterMap.put(idx, new SymbolCounterDTO(symbol));
            }
        }
    }

    @Getter
    class SymbolCounterDTO {
        private final MarkingSymbol symbol;
        private Integer count;

        public SymbolCounterDTO(MarkingSymbol symbol) {
            this.symbol = symbol;
            this.count = 1;
        }

        public void setCount(MarkingSymbol symbol) {
            if (this.count == -1) {
                return;
            }
            if (!this.symbol.equals(symbol)) {
                this.count = -1;
            } else {
                this.count++;
            }
        }
    }

    enum GameStatus {
        ONGOING,
        WIN,
        DRAW
    }

    enum MarkingSymbol {
        CROSS,
        ZERO
    }

    static class Utils {

        public static String generateId() {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

}

