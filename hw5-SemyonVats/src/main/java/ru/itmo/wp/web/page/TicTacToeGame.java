package ru.itmo.wp.web.page;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame {

    public static GamePlayer changePlayer(GamePlayer player) {
        if (player.equals(GamePlayer.X)) {
            return GamePlayer.O;
        } else {
            return GamePlayer.X;
        }
    }

    static boolean check(String playerSymbol, String[][] cells, int size) {
        boolean[] rowWin = new boolean[size];
        boolean[] colWin = new boolean[size];
        boolean diag1Win = true, diag2Win = true;
        for (int i = 0; i < size; i++) {
            rowWin[i] = true;
            colWin[i] = true;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String cell = cells[i][j];
                if (!playerSymbol.equals(cell)) {
                    rowWin[i] = false;
                    colWin[j] = false;
                    if (i == j) diag1Win = false;
                    if (i + j == size - 1) diag2Win = false;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            if (rowWin[i] || colWin[i]) return true;
        }
        return diag1Win || diag2Win;
    }

    static GamePhase verdict(String[][] cells, int size) {
        if (check("X", cells, size)) {
            return GamePhase.WON_X;
        }
        if (check("O", cells, size)) {
            return GamePhase.WON_O;
        } else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (cells[i][j] == null) {
                        return GamePhase.RUNNING;
                    }
                }
            }
            return GamePhase.DRAW;
        }
    }

    private static boolean isLineBlocked(String[] line) {
        boolean hasX = false;
        boolean hasO = false;
        for (String cell : line) {
            if ("X".equals(cell)) {
                hasX = true;
            } else if ("O".equals(cell)) {
                hasO = true;
            }
        }
        return hasX && hasO;
    }

    public static boolean DrawTest(int size, String[][] cells) {
        for (int i = 0; i < size; i++) {
            if (!isLineBlocked(cells[i])) {
                return false;
            }
        }

        for (int j = 0; j < size; j++) {
            String[] column = new String[size];
            for (int i = 0; i < size; i++) {
                column[i] = cells[i][j];
            }
            if (!isLineBlocked(column)) {
                return false;
            }
        }

        String[] diag1 = new String[size];
        for (int i = 0; i < size; i++) {
            diag1[i] = cells[i][i];
        }
        if (!isLineBlocked(diag1)) {
            return false;
        }

        String[] diag2 = new String[size];
        for (int i = 0; i < size; i++) {
            diag2[i] = cells[i][size - 1 - i];
        }
        return isLineBlocked(diag2);
    }


    public static GamePhase isDetermine(int size, String[][] cells, GamePlayer player, int depth) {
        if (DrawTest(size, cells)) {
            return GamePhase.DRAW;
        }

        int filledCounter = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] != null) {
                    filledCounter++;
                }

            }
        }
        if (filledCounter < size * size - 4 * size) {
            depth = 0;
        }

        GamePhase curPhase = verdict(cells, size);
        if (curPhase != GamePhase.RUNNING || depth == 0) {
            return curPhase;
        }

        List<GamePhase> phase = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] == null) {
                    cells[i][j] = (player == GamePlayer.X ? "X" : "O");
                    phase.add(isDetermine(size, cells, changePlayer(player), depth - 1));
                    cells[i][j] = null;
                    if (phase.get(0) != phase.get(phase.size() - 1)) { //оптимизация на случайных позициях
                        return GamePhase.RUNNING;
                    }
                }
            }
        }
        return phase.get(0);
    }

    public static void makeMove(State state, int row, int col) {
        if (state.getPhase() != GamePhase.RUNNING || state.getCells()[row][col] != null) {
            return;
        }

        state.getCells()[row][col] = (state.getCurrentPlayer() == GamePlayer.X ? "X" : "O");

        GamePhase immediateVerdict = verdict(state.getCells(), state.getSize());
        if (immediateVerdict != GamePhase.RUNNING) {
            state.setPhase(immediateVerdict);
            return;
        }

        GamePhase phase = isDetermine(state.getSize(), state.getCells(), changePlayer(state.getCurrentPlayer()), state.getDepth());

        if (phase == GamePhase.RUNNING) {
            state.setCurrentPlayer(changePlayer(state.getCurrentPlayer()));
        } else {
            state.setPhase(phase);
        }
    }


    /*
    public static void makeMove(State state, int row, int col) {
        if (state.getPhase() != GamePhase.RUNNING || state.getCells()[row][col] != null) {
            return;
        }

        GamePlayer player = state.getCurrentPlayer();
        String symbol = (player == GamePlayer.X ? "X" : "O");
        state.getCells()[row][col] = symbol;

        if (checkWin(state, symbol)) {
            state.setPhase(player == GamePlayer.X ? GamePhase.WON_X : GamePhase.WON_O);
        } else if (isDraw(state)) {
            state.setPhase(GamePhase.DRAW);
        } else {
            state.setCurrentPlayer(player == GamePlayer.X ? GamePlayer.O : GamePlayer.X);
        }
    }



    public static boolean checkWin(State state, String playerSymbol) {
        final int size = state.getSize();
        String[][] cells = state.getCells();


        for (int i = 0; i < size; i++) {
            boolean rowWin = true, colWin = true;
            for (int j = 0; j < size; j++) {
                if (!playerSymbol.equals(cells[i][j])) rowWin = false;
                if (!playerSymbol.equals(cells[j][i])) colWin = false;
            }
            if (rowWin || colWin) return true;
        }

        boolean diag1Win = true, diag2Win = true;
        for (int i = 0; i < size; i++) {
            if (!playerSymbol.equals(cells[i][i])) diag1Win = false;
            if (!playerSymbol.equals(cells[i][size - 1 - i])) diag2Win = false;
        }
        return diag1Win || diag2Win;
    }

    public static boolean isDraw(State state) {
        String[][] cells = state.getCells();
        for (String[] row : cells) {
            for (String cell : row) {
                if (cell == null) return false;
            }
        }
        return true;
    }
    */
}