import java.util.Arrays;

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

        while (true) {
            int x = (int) (Math.random() * 4);
            int y = (int) (Math.random() * 4);
            if (n[x][y] == 0) {
                n[x][y] = 2;
                break;
            }
        }

        return n;
    }


    public static boolean[] canPlay(int[][] board) {
        boolean[] can = new boolean[]{false, false, false, false};

        for (int row = 0; row < 4;row++) {
            int a = 0;
            int last = -1;
            for (int i = 0; i < 4; i++) {
                if (last == board[row][i]) {
                    can[0] = true;
                } else if (board[row][i] != 0) {
                    if (a++ != i) {
                        can[0] = true;
                    }
                    last = board[row][i];
                }
            }
        }


        for (int row = 0; row < 4;row++) {
            int a = 0;
            int last = -1;
            for (int i = 0; i < 4; i++) {
                if (last == board[i][row]) {
                    can[1] = true;
                }else if (board[i][row] != 0) {
                    if (a++ != i) {
                        can[1] = true;

                    }
                    last = board[i][row];
                }
            }
        }


        for (int row = 0; row < 4;row++) {
            int a = 0;
            int last = -1;
            for (int i = 0; i < 4; i++) {
                if (last == board[row][3 - i]) {
                    can[2] = true;
                }else if (board[row][3 - i] != 0) {
                    if (a++ != i) {
                        can[2] = true;
                    }
                    last = board[row][3 - i];
                }
            }
        }


        for (int row = 0; row < 4;row++) {
            int a = 0;
            int last = -1;
            for (int i = 0; i < 4; i++) {
                if (last == board[3 - i][row]) {
                    can[3] = true;
                }else if (board[3 - i][row] != 0) {
                    if (a++ != i) {
                        can[3] = true;
                    }
                    last = board[3 - i][row];
                }
            }
        }


        return can;
    }

    public static double[][] toTrainable(int[][] b) {
        double[][] out = new double[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (b[x][y] != 0) {
                    out[x][y] = Math.log(b[x][y]) / 4;
                }
            }
        }
        return out;
    }
}
