public class Board {
    public static int[][] start() {
        int[][] board = new int[4][4];
        int i = 0;
        while (i < 2) {
            int x = (int) (Math.random() * 4);
            int y = (int) (Math.random() * 4);
            if (board[x][y] == 0) {
                board[x][y] = 2;
                i++;
            }
        }
        return board;
    }

    public static int[][] move(int[][] board, int direction) {
        int[][] n = new int[4][4];

        if (direction == 0) {
            for (int row = 0; row < 4;row++) {
                int a = 0;
                int last = -1;
                for (int i = 0; i < 4; i++) {
                    if (last == board[row][i]) {
                        n[row][a - 1] *= 2;
                        last = -1;
                    }else if (board[row][i] != 0) {
                        n[row][a++] = board[row][i];
                        last = board[row][i];
                    }
                }
            }
        } else if (direction == 1) {
            for (int row = 0; row < 4;row++) {
                int a = 0;
                int last = -1;
                for (int i = 0; i < 4; i++) {
                    if (last == board[i][row]) {
                        n[a - 1][row] *= 2;
                        last = -1;
                    }else if (board[i][row] != 0) {
                        n[a++][row] = board[i][row];
                        last = board[i][row];
                    }
                }
            }
        } else if (direction == 2) {
            for (int row = 0; row < 4;row++) {
                int a = 0;
                int last = -1;
                for (int i = 0; i < 4; i++) {
                    if (last == board[row][3 - i]) {
                        n[row][4 - a] *= 2;
                        last = -1;
                    }else if (board[row][3 - i] != 0) {
                        n[row][3 - a++] = board[row][3 - i];
                        last = board[row][3 - i];
                    }
                }
            }
        } else if (direction == 3) {
            for (int row = 0; row < 4;row++) {
                int a = 0;
                int last = -1;
                for (int i = 0; i < 4; i++) {
                    if (last == board[3 - i][row]) {
                        n[4 - a][row] *= 2;
                        last = -1;
                    }else if (board[3 - i][row] != 0) {
                        n[3 - a++][row] = board[3 - i][row];
                        last = board[3 - i][row];
                    }
                }
            }
        }


        return n;
    }
}
