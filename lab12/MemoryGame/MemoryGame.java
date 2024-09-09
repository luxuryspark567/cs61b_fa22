package MemoryGame;

import byowTools.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //Generate random string of letters of length n
        StringBuilder retStr = new StringBuilder();

        // Generate one char at a time
        for (int i = 0; i < n; i++) {
            int indexForChar = RandomUtils.uniform(rand, 0, CHARACTERS.length); //26 chars?
            char c = CHARACTERS[indexForChar];
            retStr.append(c);
        }

        return retStr.toString();
    }

    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
        * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.show();
    }

    public void drawFrame(String s, String roundStatus) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig;
        // 1, Draw Header
        // 1.1 draw line
        StdDraw.line(0, this.height - 2, this.width, this.height - 2);

        // 1.2 draw header_round
        String headerRound = "Round: " + round;
        fontBig = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(fontBig);
        StdDraw.text(0, this.height - 1, headerRound + headerRound);
        // 1.3 draw roundStatus
        fontBig = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height - 1, roundStatus);

        // 1.4 draw encourage
        int encourageIndex = RandomUtils.uniform(rand, 0, ENCOURAGEMENT.length);;
        fontBig = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(fontBig);
        // each character is x16 in height, but not sure the width pixel
        // then used a trick to repeat the string to make it edge aligned.
        StdDraw.text(this.width, this.height - 1, ENCOURAGEMENT[encourageIndex] + ENCOURAGEMENT[encourageIndex]);

        // 2 show main MSG in the center
        fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        StdDraw.show();
    }

    public void drawBlankFrame() {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        //Font fontBig = new Font("Monaco", Font.BOLD, 30);
        //StdDraw.setFont(fontBig);
        //StdDraw.text(this.width / 2, this.height / 2, s);

        //TODO: If the game is not over, display encouragement, and let the user know if they
        // should be typing their answer or watching for the next round.


        StdDraw.show();
    }
    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        // Each character should be visible on the screen for 1 second and there should be a
        // brief 0.5 second break between characters where the screen is blank.
        char[] cArray = letters.toCharArray();
        for (char c:cArray) {
            drawFrame(Character.toString(c), "Watch!");
            StdDraw.pause(1000); // 1000 ms

            // blank the screen
            drawFrame("", "Watch!");
            StdDraw.pause(500); // 500 ms
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        StringBuilder retStr = new StringBuilder();
        int counter = n;
        while (counter > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                // get the char
                char c = StdDraw.nextKeyTyped();
                // add the char to result
                retStr.append(c);
                drawFrame(retStr.toString(), "Type!");
                counter--;
            }
        }
        return retStr.toString();
    }

    boolean judgeResult(String testStr, String userStr) {
        return !testStr.equals(userStr);
    }

    void displayMsgRoundWelcome() {
        String welcomeMsg = "Round: " + round;

        drawFrame(welcomeMsg, "Watch!");
        StdDraw.pause(1000); // 1000 ms
    }

    void displayMsgForUserAnswer() {
        drawFrame("", "Type!");
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        this.gameOver = false;
        this.round = 0;

        // Establish Engine loop
        while (!gameOver) {

            round = round + 1;

            displayMsgRoundWelcome();

            // generate random string for current round
            String testStr = generateRandomString(round);

            // display test
            flashSequence(testStr);

            // show welcome
            displayMsgForUserAnswer();

            // get result
            String userStr = solicitNCharsInput(round);

            // judge result
            gameOver = judgeResult(testStr, userStr);

            StdDraw.pause(1000);
        }

        this.drawFrame("Game Over! You made it to round: " + this.round);
    }

}
