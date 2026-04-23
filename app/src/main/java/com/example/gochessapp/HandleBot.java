package com.example.gochessapp;

public class HandleBot {
    private final int[][] weights = {
            {100, -20, 10,  5,  5, 10, -20, 100},
            {-20, -50, -2, -2, -2, -2, -50, -20},
            { 10,  -2,  5,  1,  1,  5,  -2,  10},
            {  5,  -2,  1,  0,  0,  1,  -2,   5},
            {  5,  -2,  1,  0,  0,  1,  -2,   5},
            { 10,  -2,  5,  1,  1,  5,  -2,  10},
            {-20, -50, -2, -2, -2, -2, -50, -20},
            {100, -20, 10,  5,  5, 10, -20, 100}
    };

    public int[] getBestMove(int[][] board, String mode) {
        if ("HARD".equals(mode)) return getHardMove(board);
        return getEasyMove(board);
    }

    private int[] getEasyMove(int[][] board) {
        int bestR = -1, bestC = -1, maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (HandleGamePlay.isValidMove(i, j, HandleGamePlay.WHITE, board)) {
                    if (weights[i][j] > maxScore) {
                        maxScore = weights[i][j]; bestR = i; bestC = j;
                    }
                }
            }
        }

        return new int[]{bestR, bestC};

    }

    private int[] getHardMove(int[][] board) {
        int bestR = -1, bestC = -1, bestVal = Integer.MIN_VALUE;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (HandleGamePlay.isValidMove(i, j, HandleGamePlay.WHITE, board)) {
                    int[][] nextState = HandleGamePlay.simulateMove(i, j, HandleGamePlay.WHITE, board);
                    int moveVal = minimax(nextState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    if (moveVal > bestVal) {
                        bestVal = moveVal; bestR = i; bestC = j;
                    }
                }
            }
        }
        return new int[]{bestR, bestC};
    }

    private int minimax(int[][] state, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0) return evaluate(state);
        if (isMax) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (HandleGamePlay.isValidMove(i, j, HandleGamePlay.WHITE, state)) {
                        int eval = minimax(HandleGamePlay.simulateMove(i, j, HandleGamePlay.WHITE, state), depth - 1, alpha, beta, false);
                        maxEval = Math.max(maxEval, eval); alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break;
                    }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (HandleGamePlay.isValidMove(i, j, HandleGamePlay.BLACK, state)) {
                        int eval = minimax(HandleGamePlay.simulateMove(i, j, HandleGamePlay.BLACK, state), depth - 1, alpha, beta, true);
                        minEval = Math.min(minEval, eval); beta = Math.min(beta, eval);
                        if (beta <= alpha) break;
                    }
            return minEval;
        }
    }

    private int evaluate(int[][] state) {
        int score = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (state[i][j] == HandleGamePlay.WHITE) score += weights[i][j];
                else if (state[i][j] == HandleGamePlay.BLACK) score -= weights[i][j];
            }
        return score;
    }
}