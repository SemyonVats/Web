package ru.itmo.wp.web.page;

public class State {
    private final int size = 9;
    private final String[][] cells = new String[size][size];
    private GamePlayer currentPlayer = GamePlayer.X;
    private GamePhase phase = GamePhase.RUNNING;

    public int getSize() {
        return size;
    }

    public int getDepth() {
        return (int) (Math.log(5_000_000) / Math.log(4 * size));
    }


    public String[][] getCells() {
        return cells;
    }

    public GamePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(GamePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

}