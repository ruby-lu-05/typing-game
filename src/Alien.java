/* Ruby Lu
 * 6/23/2022
 * ICS30 Final Project: Typing Game (Alien class)
 */

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Alien extends TypingGame {

    public static final int NUM_WORDS_PER_LENGTH = 100;
    public static final int NUM_WORD_LENGTHS = 7;

    // each alien is given a word length, x and y coordinates, word, and velocity
    int wordLength;
    int x;
    int y;
    String word;
    double velocity;
    JLabel alien;
    boolean exists = false;

    public Alien (int length, double velocity) throws IOException {
        Random r = new Random();

        this.wordLength = length;

        ImageIcon[] aliens = new ImageIcon[7];
        for (int i = 3; i < 10; i++) {
            aliens[i-3] = new ImageIcon("alien" + i + ".png");
        }

        // a 2D array containing arrays storing words for each word length
        String[][] wordList = new String[NUM_WORD_LENGTHS][NUM_WORDS_PER_LENGTH];
        for (int i = 3; i < 10; i++) {
            // reading from each file and adding words to the array
            String[] words = new String[NUM_WORDS_PER_LENGTH];
            ArrayList<String> specificWordList = (ArrayList<String>)Files.readAllLines(Paths.get(i + "LetterWords.txt"));
            for (int j = 0; j < NUM_WORDS_PER_LENGTH; j++) {
                words[j] = specificWordList.get(j);
            }
            wordList[i-3] = words;
        }

        // randomly deciding which edge of the screen the alien will spawn from
        int edge = r.nextInt(4)+1;

        switch (edge) {
            // left side, random y coordinate
            case 1:
                x = -SPRITE_SIZE;
                y = r.nextInt(DISPLAY_SIZE);
                break;
            // top side, random x coordinate
            case 2:
                x = r.nextInt(DISPLAY_SIZE);
                y = -SPRITE_SIZE;
                break;
            // right side, random y coordinate
            case 3:
                x = DISPLAY_SIZE;
                y = r.nextInt(DISPLAY_SIZE);
                break;
            // bottom side, random x coordinate
            case 4:
                x = r.nextInt(DISPLAY_SIZE);
                y = DISPLAY_SIZE;
                break;
        }

        // constructing a random alien with a random word and random coordinates
        this.word = wordList[wordLength-3][r.nextInt(NUM_WORDS_PER_LENGTH)];
        this.velocity = velocity;
        this.alien = new JLabel(aliens[wordLength-3]);
        alien.setText(word);
        alien.setFont(new Font("Century Gothic", Font.PLAIN,14));
        alien.setHorizontalTextPosition(JLabel.CENTER);
        alien.setVerticalTextPosition(JLabel.BOTTOM);
        alien.setForeground(Color.white);
    }

    /**
     * Removes an alien object from the screen
     * pre: the JFrame that the alien was added to
     * post: the alien is removed from the frame
     */
    public void deleteAlien(JFrame frame) {
        frame.getContentPane().remove(alien);
        exists = false;
    }

    /**
     * Animates alien movement
     * pre: the JFrame that the alien is to be added to
     * post: the alien is added to the JFrame with updated coordinates
     */
    public void drawAlien(JFrame frame) {
        // ending coordinates are the center of the screen
        int endX = DISPLAY_SIZE/2-SPRITE_SIZE/2;
        int endY = DISPLAY_SIZE/2-SPRITE_SIZE/2;
        double xMovement;  // horizontal distance travelled each time
        double yMovement;  // vertical distance travelled each time

        if (endX - x != 0 && endY - y != 0) {
            // slope of the alien's current location to the center of the screen
            double m = (double)(endX - x)/(endY - y);
            // using trigonometry to determine the amount distance needed to travel horizontally and vertically
            xMovement = Math.sqrt((m*m*velocity*velocity)/(m*m+1));
            yMovement = Math.sqrt((velocity*velocity)/(m*m+1));
        } else {
            // for when the slope is undefined (cannot divide by 0)
            if (endX - x == 0) {
                xMovement = velocity;
                yMovement = 0;
            } else {
                xMovement = 0;
                yMovement = velocity;
            }
        }

        if (x > endX) {
            // for when the alien spawns to the right of the turtle
            xMovement = -xMovement;
        }
        if (y > endY) {
            // for when the alien spawns beneath the turtle
            yMovement = -yMovement;
        }

        // updating the alien's x and y coordinates
        x += (int) xMovement;
        y += (int) yMovement;
        alien.setBounds(x,y,SPRITE_SIZE,SPRITE_SIZE);

        if (!exists) {
            // drawing the alien on the frame
            int layer = frame.getContentPane().getComponents().length - 2;
            frame.getContentPane().add(alien,layer);
            exists = true;
        }
    }
}
