package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private static final String SESSION_KEY = "ticTacToeState";
    private static final String STATE = "state";

    private State getState(HttpServletRequest request) {
        State state = (State) request.getSession().getAttribute(SESSION_KEY);
        if (state == null) {
            state = new State();
            request.getSession().setAttribute(SESSION_KEY, state);
        }
        return state;
    }

    public void action(HttpServletRequest request, Map<String, Object> view) {
        view.put(STATE, getState(request));
    }

    public void onMove(HttpServletRequest request, Map<String, Object> view) {
        State state = getState(request);

        if (state.getPhase() == GamePhase.RUNNING) {
            final int size = state.getSize();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (request.getParameter("cell_" + i + "_" + j) != null) {
                        TicTacToeGame.makeMove(state, i, j);
                        view.put(STATE, state);
                        return;
                    }
                }
            }
        }

        view.put(STATE, state);
    }

    public void newGame(HttpServletRequest request, Map<String, Object> view) {
        State state = new State();
        request.getSession().setAttribute(SESSION_KEY, state);
        view.put(STATE, state);
    }
}