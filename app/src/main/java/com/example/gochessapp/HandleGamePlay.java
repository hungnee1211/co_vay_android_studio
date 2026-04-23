package com.example.gochessapp;

public class HandleGamePlay {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    public static boolean isValidMove(int r, int c, int p, int[][] board) {
        if (r < 0 || r >= 8 || c < 0 || c >= 8 || board[r][c] != EMPTY) return false;
        int opp = (p == BLACK) ? WHITE : BLACK;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1}, dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i], nc = c + dc[i];
            boolean hasOpp = false;
            while (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && board[nr][nc] == opp) {
                nr += dr[i]; nc += dc[i]; hasOpp = true;
            }
            if (hasOpp && nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && board[nr][nc] == p) return true;
        }
        return false;
    }


    public static int[][] simulateMove(int r, int c, int p, int[][] board) {
        int[][] nextState = new int[8][8];
        for (int i = 0; i < 8; i++) nextState[i] = board[i].clone();
        nextState[r][c] = p;
        int opp = (p == BLACK) ? WHITE : BLACK;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1}, dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i], nc = c + dc[i];
            if (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && nextState[nr][nc] == opp) {
                int tr = nr, tc = nc;
                while (tr >= 0 && tr < 8 && tc >= 0 && tc < 8 && nextState[tr][tc] == opp) {
                    tr += dr[i]; tc += dc[i];
                }
                if (tr >= 0 && tr < 8 && tc >= 0 && tc < 8 && nextState[tr][tc] == p) {
                    int fr = nr, fc = nc;
                    while (nextState[fr][fc] == opp) {
                        nextState[fr][fc] = p;
                        fr += dr[i]; fc += dc[i];
                    }
                }
            }
        }
        return nextState;
    }
}