import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        Player p1 = new Player(true); // X
        Player p2 = new Player(false); // O

        Settings.readSettings();

        boolean noExit = true;
        while (noExit) {
            clearScreen();
            System.out.print("""
                                     ------------------------------------------------------------------------------------
                                     ######## ####  ######     ########    ###     ######     ########  #######  ########
                                        ##     ##  ##    ##       ##      ## ##   ##    ##       ##    ##     ## ##
                                        ##     ##  ##             ##     ##   ##  ##             ##    ##     ## ##
                                        ##     ##  ##             ##    ##     ## ##             ##    ##     ## ######
                                        ##     ##  ##             ##    ######### ##             ##    ##     ## ##
                                        ##     ##  ##    ##       ##    ##     ## ##    ##       ##    ##     ## ##
                                        ##    ####  ######        ##    ##     ##  ######        ##     #######  ########
                                     ------------------------------------------------------------------------------------
                                                            ||    Choose Your Opponent    ||
                                          1 - Player

                                          2 - Computer(a dum dum)

                                          3 - Settings

                                          0 - Exit

                                            >>""" + " ");
            int option = testInput();
            if ((Settings.lockedCells >= Settings.boardSize * Settings.boardSize) || Settings.alignInARow <= 1) {
                System.out.println(" CHECK SETTINGS | INVALID SETTINGS ");
                Thread.sleep(1000);
                option = 3;
            }
            switch (option) {
                case 1 -> playerVersusPlayer(p1, p2);
                case 2 -> playerVersusAI(p1, p2);
                case 3 -> settings();
                case 0 -> noExit = false;
                case -25 -> {
                    System.out.println(" !! WRONG INPUT DETECTED !!");
                    Thread.sleep(700);
                }
                default -> {
                    System.out.println(" !! Input is out of context !! ");
                    Thread.sleep(700);
                }
            }
        }
    }

    /**
     * Clears Screen ノ( º _ ºノ)
     */
    public static void clearScreen() {
        // new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Player versus Player mode
     * @param p1 Player 1 [ O ]
     * @param p2 Player 2 [ X ]
     * @throws InterruptedException I have no idea what this does ¯\_(ツ)_/¯
     */
    public static void playerVersusPlayer(Player p1, Player p2) throws InterruptedException {
        Board board = new Board();
        board.createGameBoard();

        boolean isPlayer1Turn = true;
        int remainingTurns = Settings.boardSize * Settings.boardSize - Settings.lockedCells;
        while (remainingTurns != 0) {
            clearScreen();
            board.printBoard();
            if (isPlayer1Turn) {
                if (p1.getInput() && p1.fillCell(board.gameBoard, Settings.boardSize)) {
                    remainingTurns--;
                    isPlayer1Turn = false;
                    if (p1.winLogic(board.gameBoard)) {
                        clearScreen();
                        board.printBoard();
                        System.out.print(" || [ X ] HAS WON THIS MATCH ||");
                        Thread.sleep(3000);
                        break;
                    }
                }
            } else {
                if (p2.getInput() && p2.fillCell(board.gameBoard, Settings.boardSize)) {
                    remainingTurns--;
                    isPlayer1Turn = true;
                    if (p2.winLogic(board.gameBoard)) {
                        clearScreen();
                        board.printBoard();
                        System.out.print(" || [ O ] HAS WON THIS MATCH ||");
                        Thread.sleep(3000);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Player versus AI mode
     *
     * @param p1 Player 1 [ O ]
     * @param p2 Player 2 [ X ]
     * @throws InterruptedException Again; I have no idea what this does ¯\_(ツ)_/¯
     */
    public static void playerVersusAI(Player p1, Player p2) throws InterruptedException {
        Board board = new Board();
        board.createGameBoard();

        boolean isPlayer1Turn = true;
        int remainingTurns = Settings.boardSize * Settings.boardSize - Settings.lockedCells;
        while (remainingTurns != 0) {
            clearScreen();
            board.printBoard();
            if (isPlayer1Turn) {
                if (p1.getInput() && p1.fillCell(board.gameBoard, Settings.boardSize)) {
                    board.squareList.remove((Integer) (p1.yInput * Settings.boardSize + p1.xInput));
                    if (p1.winLogic(board.gameBoard)) {
                        clearScreen();
                        board.printBoard();
                        System.out.print(" || [ X ] HAS WON THIS MATCH ||");
                        Thread.sleep(3000);
                        break;
                    }
                    remainingTurns--;
                    isPlayer1Turn = false;
                }
            } else {
                Random rand = new Random();
                int tempIndex;
                int tempLoc;
                tempLoc = board.squareList.get(tempIndex = rand.nextInt(board.squareList.size()));
                board.squareList.remove(tempIndex);
                tempIndex = 0; // NOW USING AS yAXES HOLDER
                while (tempLoc > Settings.boardSize - 1) {
                    tempLoc -= Settings.boardSize;
                    tempIndex++;
                }
                p2.xInput = tempLoc;
                p2.yInput = tempIndex;
                if (p2.fillCell(board.gameBoard, Settings.boardSize)) {
                    if (p2.winLogic(board.gameBoard)) {
                        clearScreen();
                        board.printBoard();
                        System.out.print(" || [ O ] HAS WON THIS MATCH ||");
                        Thread.sleep(3000);
                        break;
                    }
                    remainingTurns--;
                    isPlayer1Turn = true;
                }
            }
        }
    }

    /**
     * Prevents wrongful Integer Inputs
     */
    public static int testInput() {
        Scanner scan = new Scanner(System.in);
        try {
            return Integer.parseInt(scan.next());
        } catch (NumberFormatException e) {
            return -25;
        }
    }

    /**
     * Changes game-file settings
     */
    public static void settings() throws InterruptedException, IOException {
        boolean exit = false;
        while (!exit) {
            clearScreen();
            System.out.print("""
                      1 - Change size of the board
                      
                      2 - Change number of locked cells
                      
                      3 - Change number of aligned cells to win
                      
                      0 - Back
                      
                        >>""" + ' ');
            int option = testInput();
            clearScreen();
            int temp;
            switch (option) {
                case 1 -> {
                    System.out.printf(" Choose a new board size ( Current: %d ): ", Settings.boardSize);
                    if ((temp = testInput()) != -25) {
                        Settings.boardSize = temp;
                    }
                }
                case 2 -> {
                    System.out.printf(" Update amount of locked cells ( Current: %d ): ", Settings.lockedCells);
                    if ((temp = testInput()) != -25) {
                        Settings.lockedCells = temp;
                    }
                }
                case 3 -> {
                    System.out.printf(" Update amount of aligned cells needed to win ( Current: %d ): ", Settings.alignInARow);
                    if ((temp = testInput()) != -25) {
                        Settings.alignInARow = temp;
                    }
                }
                case 0 -> {
                    Settings.writeSettings();
                    exit = true;
                }
                case -25 -> {
                    System.out.println(" !! WRONG INPUT DETECTED !!");
                    Thread.sleep(700);
                }
                default -> {
                    System.out.println(" !! Input is out of context !! ");
                    Thread.sleep(700);
                }
            }
        }
    }
}
