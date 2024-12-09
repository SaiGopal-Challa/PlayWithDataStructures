package TicTacToe;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    // Function to print the current board
    public static void printBoard(char[][] board) {
        System.out.println("\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print(" | ");
            }
            System.out.println();
            if (i < 2) System.out.println("---------");
        }
        System.out.println();
    }

    // Function to check if the current player has won
    public static boolean checkWinner(char[][] board, char player) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // Row win
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // Column win
            }
        }

        // Diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // Diagonal win
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // Diagonal win
        }

        return false;
    }

    // Function to check if the board is full (no empty spaces)
    public static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Minimax Algorithm to calculate the best move for the computer
    public static int[] minimax(char[][] board, int depth, boolean isMaximizing) {
        // Base cases: Check for terminal states (win/loss/draw)
        if (checkWinner(board, 'O')) {
            return new int[]{1, -1};  // Maximizing player (computer) wins
        }
        if (checkWinner(board, 'X')) {
            return new int[]{-1, -1}; // Minimizing player (player) wins
        }
        if (isBoardFull(board)) {
            return new int[]{0, -1};  // Draw
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[]{-1, -1};

        // Loop through all cells to simulate all possible moves
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {  // Check if the cell is empty
                    board[i][j] = isMaximizing ? 'O' : 'X';  // Make the move

                    // Recursively call minimax and choose the best score
                    int score = minimax(board, depth + 1, !isMaximizing)[0];
                    if (isMaximizing) {
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                    } else {
                        if (score < bestScore) {
                            bestScore = score;
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                    }

                    board[i][j] = ' ';  // Undo the move (backtrack)
                }
            }
        }

        return new int[]{bestScore, bestMove[0], bestMove[1]};
    }

    // Function for the computer to make the best move
    public static void computerMove(char[][] board) {
        int[] bestMove = minimax(board, 0, true);  // Start Minimax with maximizing player (computer)
        int row = bestMove[1];
        int col = bestMove[2];

        board[row][col] = 'O';  // Place the computer's mark
        System.out.println("Computer places 'O' at row " + (row + 1) + " and column " + (col + 1));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        char[][] board = new char[3][3];

        // Initialize the board with empty spaces
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        // Randomly decide who plays first: even => player (X) first, odd => computer (X) first
        boolean isPlayerTurn = rand.nextInt(2) == 0;  // Even means player first
        if (isPlayerTurn) {
            System.out.println("Player goes first as 'X'.");
        } else {
            System.out.println("Computer goes first as 'X'.");
        }

        printBoard(board);

        boolean gameWon = false;
        while (!gameWon && !isBoardFull(board)) {
            if (isPlayerTurn) {
                // Player's turn
                System.out.println("Your turn (Player X):");
                System.out.print("Enter row (1-3): ");
                int row = scanner.nextInt() - 1;
                System.out.print("Enter column (1-3): ");
                int col = scanner.nextInt() - 1;

                // Check if the move is valid (within range and cell is empty)
                if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ') {
                    board[row][col] = 'X';  // Place the player's mark
                    printBoard(board);
                } else {
                    System.out.println("Invalid move. Try again.");
                    continue;  // Skip the rest of the loop and prompt the player again
                }

                // Check if the player wins
                if (checkWinner(board, 'X')) {
                    printBoard(board);
                    System.out.println("Congratulations! You win!");
                    gameWon = true;
                }
            } else {
                // Computer's turn (uses Minimax to make the best move)
                computerMove(board);
                printBoard(board);

                // Check if the computer wins
                if (checkWinner(board, 'O')) {
                    System.out.println("Computer wins! Better luck next time.");
                    gameWon = true;
                }
            }

            // Switch turn
            isPlayerTurn = !isPlayerTurn;
        }

        // If no winner and board is full, it's a draw
        if (!gameWon && isBoardFull(board)) {
            printBoard(board);
            System.out.println("It's a draw!");
        }

        scanner.close();
    }
}

