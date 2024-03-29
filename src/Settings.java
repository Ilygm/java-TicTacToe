import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Settings {
    /**
     * boardSize : Size of the game-board
     * lockedCells : Amount of locked squares in board
     * alignNtoWin : Amount of squares needed to be aligned to win
     */
    public static int boardSize = 4;
    public static int lockedCells = 3;
    public static int alignInARow = 3;

    /**     Loading pre-set settings OR Creates a new file with default value of settings       */
    public static void readSettings() throws IOException {
        File settingFile = new File("./src/AppData.txt");
        if (System.getProperty("user.dir").contains("\\src")) {
            settingFile = new File("./AppData.txt");
        }

        if (settingFile.exists()) {
            Scanner scan = new Scanner(settingFile);
            scan.useDelimiter("[a-z|A-Z]*\\s*#\\s");
            scan.nextLine(); // SKIPS THE COMMENT
            for (int i = 1; i < 4; i++) {
                String buffer = "";
                try {
                    buffer = scan.next().strip();
                } catch (NoSuchElementException err) {
                    if (settingFile.delete()) {
                        System.err.println(" AppData.txt HAS BEEN WRONGFULLY MODIFIED, RESTART THE PROGRAM ");
                        System.exit(85);
                    } else {
                        System.out.println(" AppData.txt HAS BEEN WRONGFULLY MODIFIED, DELETE AppData.txt ");
                        System.exit(85);
                    }
                }
                try {
                    switch (i) {
                        case 1 -> boardSize = Integer.parseInt(buffer);
                        case 2 -> lockedCells = Integer.parseInt(buffer);
                        case 3 -> alignInARow = Integer.parseInt(buffer);
                    }
                } catch (NumberFormatException err) {
                    if (settingFile.delete()) {
                        System.err.println(" AppData.txt HAS BEEN WRONGFULLY MODIFIED, RESTART THE PROGRAM ");
                        System.exit(85);
                    }
                }
            }
            scan.reset();
            scan.close();
        } else {
            if (settingFile.createNewFile()) {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(settingFile));
                fileWriter.write("// DO NOT CHANGE ANYTHING BEFORE HASHTAGS\n");
                fileWriter.write("BoardSize   # " + boardSize + '\n');
                fileWriter.write("LockedCells # " + lockedCells + '\n');
                fileWriter.write("AlignInARow # " + alignInARow + '\n');
                fileWriter.close();
            } else {
                System.err.println(" PERMISSION WAS DENIED ");
                System.exit(85);
            }
        }
    }

    /**     Updates AppData.txt file        */
    public static void writeSettings() throws IOException {
        File settingFile = new File("./src/AppData.txt");
        if (System.getProperty("user.dir").contains("\\src")) settingFile = new File("./AppData.txt");
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(settingFile));
        fileWriter.write("// DO NOT CHANGE ANYTHING BEFORE HASHTAGS\n");
        fileWriter.write("BoardSize   # " + boardSize + '\n');
        fileWriter.write("LockedCells # " + lockedCells + '\n');
        fileWriter.write("AlignInARow # " + alignInARow + '\n');
        fileWriter.close();
    }
}
