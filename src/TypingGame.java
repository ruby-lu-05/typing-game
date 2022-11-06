/* Ruby Lu
 * 6/23/2022
 * ICS3O Final Project: Typing Game
 * Resource used: https://docs.oracle.com/javase/tutorial/uiswing/TOC.html
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TypingGame {

    // instantiating game colours
    public static final Color PURPLE = new Color(23,9,50);
    public static final Color PINK = new Color(186,30,104);
    public static final Color TEAL = new Color(43,220,202);

    // constant integer values
    public static final int MILLI_CONV = 1000;
    public static final int DISPLAY_SIZE = 600;
    public static final int SPRITE_SIZE = 100;
    public static final int BOX_HEIGHT = (int)(DISPLAY_SIZE/11.0);
    public static final int BOX_LENGTH = (int)(DISPLAY_SIZE*(4.0/10));
    public static final int MAX_LIVES = 5;

    // variables that will be updated throughout the game
    public static String currentInput = "";
    public static boolean keyDown = false;
    public static boolean entered = false;
    public static int difficultyLevel = 0;
    public static int numLives = 5;

    // boolean values to control game variables
    public static boolean introScreen = true;
    public static boolean playPressed = false;
    public static boolean gamePlay = false;
    public static boolean gameOver = false;
    public static boolean instructionsPressed = false;
    public static boolean highScoresPressed = false;
    public static boolean obliterate = false;
    public static boolean obliterateTyped = false;
    public static boolean restoreLife = false;

    public static int score = 0;
    public static boolean newHighScore = false;

    // difficulty levels (with a number from 0 to 3 corresponding to each difficulty level)
    public static String[] difficultyLevels = {"   Easy", "   Medium", "   Hard", "   Extreme"};

    // txt files containing the high score for each difficulty level
    public static String[] scoreFiles = {"easy_highscore.txt", "medium_highscore.txt", "hard_highscore.txt", "extreme_highscore.txt"};


    public static void main(String[] args) throws IOException {
        Random r = new Random();

        // creating a JFrame
        JFrame frame = new JFrame();
        frame.setSize(DISPLAY_SIZE, DISPLAY_SIZE);
        frame.setTitle("Space Turtle: Typing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        // detecting keyboard input from the user
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            // detecting key presses
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 8) {           // backspace key
                    if (currentInput.length() > 0) currentInput = currentInput.substring(0, currentInput.length() - 1);
                } else if (e.getKeyChar() == 10) {   // enter key
                    entered = true;
                } else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ') {
                    // the program displays any letter or digit, and spaces
                    currentInput += e.getKeyChar();
                }
                keyDown = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // importing images to be displayed
        ImageIcon turtleIcon = new ImageIcon("turtle.png");
        ImageIcon largeTurtle = new ImageIcon("turtle_big.png");
        ImageIcon backgroundImage = new ImageIcon("space_bkg.png");
        ImageIcon deadTurtle = new ImageIcon("dead_turtle.png");
        ImageIcon heart = new ImageIcon("heart.png");
        ImageIcon emptyHeart = new ImageIcon("empty_heart.png");
        ImageIcon bombAlien = new ImageIcon("bomb_alien.png");
        ImageIcon nurseAlien = new ImageIcon("nurse_alien.png");

        // defining fonts that will be used throughout the game
        Font titleFont = new Font("Cooper Black", Font.PLAIN,60);
        Font defaultFont = new Font("Century Gothic", Font.BOLD,14);
        Font largerFont = new Font ("Century Gothic", Font.BOLD,16);
        Font inputFont = new Font("Century Gothic", Font.PLAIN, 14);
        Font alienFont = new Font("Century Gothic", Font.PLAIN,14);

        frame.setIconImage(turtleIcon.getImage());

        // JLabels for text boxes and images to be displayed for the intro screen and game over screen
        JLabel topSubtitle = new JLabel("welcome to");
        topSubtitle.setFont(largerFont);
        topSubtitle.setBounds(0,DISPLAY_SIZE/6-80,DISPLAY_SIZE,100);
        topSubtitle.setHorizontalAlignment(JLabel.CENTER);
        topSubtitle.setForeground(Color.WHITE);

        JLabel bottomSubtitle = new JLabel("a typing game");
        bottomSubtitle.setFont(largerFont);
        bottomSubtitle.setBounds(0,DISPLAY_SIZE/60+110,DISPLAY_SIZE,100);
        bottomSubtitle.setHorizontalAlignment(JLabel.CENTER);
        bottomSubtitle.setForeground(Color.WHITE);

        JLabel title = new JLabel("SPACE TURTLE!");
        title.setFont(titleFont);
        title.setBounds(0,DISPLAY_SIZE/6-30,DISPLAY_SIZE,100);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(TEAL);

        JLabel displayTurtle = new JLabel("",largeTurtle,JLabel.CENTER);
        displayTurtle.setBounds(0,DISPLAY_SIZE/2-100,DISPLAY_SIZE,250);

        // JButtons for opening the instructions, opening high scores, and starting the game
        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setHorizontalAlignment(JLabel.LEFT);
        instructionsButton.setBounds(DISPLAY_SIZE/12,DISPLAY_SIZE-120,DISPLAY_SIZE/4,50);
        instructionsButton.setFont(largerFont);
        instructionsButton.setBackground(TEAL);
        instructionsButton.setForeground(PURPLE);
        instructionsButton.addActionListener(e -> instructionsPressed = true);

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setHorizontalAlignment(JLabel.LEFT);
        highScoresButton.setBounds(DISPLAY_SIZE/12,DISPLAY_SIZE-120,DISPLAY_SIZE/4,50);
        highScoresButton.setFont(largerFont);
        highScoresButton.setBackground(TEAL);
        highScoresButton.setForeground(PURPLE);
        highScoresButton.addActionListener(e -> highScoresPressed = true);

        JButton playButton = new JButton("Play");
        playButton.setHorizontalAlignment(JLabel.LEFT);
        playButton.setBounds(DISPLAY_SIZE/2+100,DISPLAY_SIZE-120,DISPLAY_SIZE/4,DISPLAY_SIZE/12);
        playButton.setFont(largerFont);
        playButton.setBackground(TEAL);
        playButton.setForeground(PURPLE);
        playButton.addActionListener(e -> playPressed = true);

        // JComboBox to allow the user to select a difficulty level
        JComboBox<String> selectDifficulty = new JComboBox<>(difficultyLevels);
        selectDifficulty.setBackground(TEAL);
        selectDifficulty.setFont(largerFont);
        selectDifficulty.setForeground(PURPLE);
        selectDifficulty.setBounds(DISPLAY_SIZE/2-75,DISPLAY_SIZE-120, DISPLAY_SIZE/4,50);
        selectDifficulty.addActionListener(e -> difficultyLevel = selectDifficulty.getSelectedIndex());

        // the user gets 5 lives (represented with filled-in pink hearts)
        JLabel[] lives = new JLabel[MAX_LIVES];
        for (int i = 0; i < MAX_LIVES; i++) {
            JLabel life = new JLabel("",heart,JLabel.CENTER);
            life.setBounds(DISPLAY_SIZE/2+40*i, 500,40,40);
            lives[i] = life;
        }

        // player's character (space turtle)
        JLabel turtle = new JLabel("", turtleIcon, JLabel.CENTER);
        turtle.setBounds((DISPLAY_SIZE - SPRITE_SIZE)/2, (DISPLAY_SIZE - SPRITE_SIZE)/2, 100, 100);

        // text boxes to be displayed during gameplay
        JLabel textPrompt = new JLabel("Type the incoming words!");
        textPrompt.setBounds(20, DISPLAY_SIZE - BOX_HEIGHT - 91, BOX_LENGTH, 100);
        textPrompt.setForeground(Color.WHITE);
        textPrompt.setFont(defaultFont);

        JLabel input = new JLabel(currentInput);
        input.setBounds(20, DISPLAY_SIZE - BOX_HEIGHT - 30, BOX_LENGTH - 10, BOX_HEIGHT - 25);
        input.setVerticalAlignment(JLabel.CENTER);
        input.setBackground(Color.WHITE);
        input.setForeground(Color.BLACK);
        input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        input.setOpaque(true);
        input.setFont(inputFont);

        JLabel scoreDisplay = new JLabel("score: " + score);
        scoreDisplay.setBounds(15, DISPLAY_SIZE - BOX_HEIGHT - 80, BOX_LENGTH - 10, BOX_HEIGHT - 25);
        scoreDisplay.setVerticalAlignment(JLabel.CENTER);
        scoreDisplay.setForeground(TEAL);
        scoreDisplay.setFont(defaultFont);

        JLabel outerTextBox = new JLabel();
        outerTextBox.setBounds(15, DISPLAY_SIZE - BOX_HEIGHT - 50, BOX_LENGTH, BOX_HEIGHT);
        outerTextBox.setBackground(PINK);
        outerTextBox.setOpaque(true);

        // the "obliterate" alien can eliminate all the current aliens on screen (icon: bomb)
        JLabel obliterateAlien = new JLabel("obliterate",bombAlien,JLabel.CENTER);
        obliterateAlien.setBounds(DISPLAY_SIZE-115,15,100,100);
        obliterateAlien.setFont(alienFont);
        obliterateAlien.setHorizontalTextPosition(JLabel.CENTER);
        obliterateAlien.setVerticalTextPosition(JLabel.BOTTOM);
        obliterateAlien.setForeground(Color.white);

        // the "restore life" alien can restore one life (icon: "nurse" alien)
        JLabel restoreLifeAlien = new JLabel("restore life",nurseAlien,JLabel.CENTER);
        restoreLifeAlien.setBounds(DISPLAY_SIZE-115,DISPLAY_SIZE-200,100,100);
        restoreLifeAlien.setFont(alienFont);
        restoreLifeAlien.setHorizontalTextPosition(JLabel.CENTER);
        restoreLifeAlien.setVerticalTextPosition(JLabel.BOTTOM);
        restoreLifeAlien.setForeground(Color.white);

        // space background
        JLabel background = new JLabel("", backgroundImage, JLabel.CENTER);
        background.setBounds(0, 0, DISPLAY_SIZE, DISPLAY_SIZE);

        frame.setVisible(true);

        // variables to keep track of how many milliseconds have passed
        long lastTick = 0;
        long currTick;
        long lastSpawn = 0;

        // ArrayList of all the current aliens on screen
        ArrayList<Alien> alienList = new ArrayList<>();

        // variables for controlling the difficulty of the game
        double enterRate = 5;
        int[] wordLengths = {3,4,5};
        double alienVelocity = 1.5;

        while (true) {

            // screen to welcome players to the game
            if (introScreen) {
                frame.getContentPane().add(title);
                frame.getContentPane().add(bottomSubtitle);
                frame.getContentPane().add(topSubtitle);
                frame.getContentPane().add(displayTurtle);
                frame.getContentPane().add(playButton);
                frame.getContentPane().add(instructionsButton);
                frame.getContentPane().add(selectDifficulty);
                frame.getContentPane().add(background);
                introScreen = false;
            }
            
            if (playPressed) {
                // the game starts when the play button is pressed
                gamePlay = true;
                frame.getContentPane().removeAll();
                frame.requestFocus();
                playPressed = false;
            } else if (instructionsPressed) {
                // a new window with instructions on it pops up when the instructions button is pressed
                JOptionPane.showMessageDialog(frame, """
                        Type the words shown underneath each alien
                        travelling towards the turtle at the center,
                        and press enter to kill them before they reach the
                        turtle. The "obliterate" alien can kill all the current
                        aliens on screen, while the "restore life" alien can
                        restore one life. When an alien touches the turtle, a
                        life is lost, and the game is over when all 5 lives
                        have been lost.""", "Instructions",JOptionPane.PLAIN_MESSAGE);
                instructionsPressed = false;
            } else if (highScoresPressed) {
                // a new window displaying high scores pops up when the high scores button is pressed
                StringBuilder displayedScores = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    // displaying the high score for each difficulty level
                    displayedScores.append(difficultyLevels[i].trim()).append(": ").append(getHighScore(i)).append("\n");
                }
                JOptionPane.showMessageDialog(frame,displayedScores,"High Scores",JOptionPane.PLAIN_MESSAGE);
                highScoresPressed = false;
            }

            frame.setVisible(true);

            // playing the game
            if (gamePlay) {
                score = 0;
                scoreDisplay.setText("score: " + score);
                frame.getContentPane().removeAll();

                for (int i = 0; i < 5; i++) {
                    // displaying the lives using filled-in hearts
                    lives[i].setIcon(heart);
                    frame.getContentPane().add(lives[i]);
                }

                // adding the turtle and text boxes to the screen
                frame.getContentPane().add(turtle);
                frame.getContentPane().add(textPrompt, 0);
                frame.getContentPane().add(input, 1);
                frame.getContentPane().add(scoreDisplay, 2);
                frame.getContentPane().add(outerTextBox, 2);
                frame.getContentPane().add(background, -1);

                // changing the difficulty of the game based on the difficulty level
                for (int i = 0; i < 4; i++) {
                    if (difficultyLevel == i) {
                        enterRate -= i;
                        alienVelocity += i;
                        for (int j = 0; j < 3; j++) {
                            wordLengths[j] += i;
                        }
                    }
                }

                while (true) {
                    currTick = System.currentTimeMillis();
                    // the game is over when the user runs out of lives
                    if (numLives <= 0) {
                        // resetting game variables so the player can replay
                        numLives = 5;
                        alienVelocity = 1.5;
                        enterRate = 5;
                        for (int i = 0; i < 3; i++) {
                            wordLengths[i] = 3+i;
                        }

                        // checking whether they got a new high score
                        if (score > getHighScore(difficultyLevel)) {
                            newHighScore = true;
                            setHighScore(difficultyLevel, score);
                        }

                        frame.getContentPane().removeAll();

                        gamePlay = false;
                        gameOver = true;
                        break;
                    }

                    // new aliens are periodically generated (added to the alienList ArrayList)
                    if (currTick - lastSpawn >= enterRate * MILLI_CONV) {
                        alienList.add(new Alien(wordLengths[r.nextInt(3)], alienVelocity));
                        lastSpawn = currTick;
                    }

                    // currentInput is updated each time a key is pressed
                    if (keyDown) {
                        input.setText(currentInput);
                        keyDown = false;
                    }

                    // tasks that are executed every 25 milliseconds (e.g animation)
                    if (currTick - lastTick >= 25) {

                        if (r.nextInt(1000) == 1) {
                            // for each iteration, there's a 1/1000 chance of the "obliterate" alien appearing
                            if (!obliterate) {
                                frame.add(obliterateAlien, 1);
                                obliterate = true;
                            }
                        } else if (r.nextInt(500) == 2) {
                            // for each iteration, theres a 1/500 chance of the "restore life" alien appearing
                            // it only appears after at least 1 life has been lost
                            if (!restoreLife && numLives < 5) {
                                restoreLife = true;
                                frame.add(restoreLifeAlien, 1);
                            }
                        }

                        // when a word is entered, the text box clears
                        // and the entered word is stored in a string
                        String enteredWord = "";

                        if (entered) {
                            enteredWord = currentInput;
                            currentInput = "";
                            entered = false;
                            input.setText(currentInput);
                        }

                        // checking if they got the "obliterate" alien
                        if (obliterate && enteredWord.equalsIgnoreCase("obliterate")) {
                            obliterateTyped = true;
                        }

                        // looping over each alien object in the alienList ArrayList
                        for (int i = 0; i < alienList.size(); i++) {
                            Alien alien = alienList.get(i);

                            // checking whether the alien has reached the turtle
                            if (alien.x >= 200 && alien.x <= 300 && alien.y >= 200 && alien.y <= 300) {
                                // when an alien reaches the turtle, it's removed and a life is lost
                                alien.deleteAlien(frame);
                                alienList.remove(alien);
                                numLives -= 1;
                                // a lost life is represented with an empty heart
                                lives[numLives].setIcon(emptyHeart);
                            } else if (enteredWord.equalsIgnoreCase(alien.word)) {
                                // checking whether the user has killed an alien
                                // when an alien is killed, it is removed and the score increases by 500
                                alien.deleteAlien(frame);
                                alienList.remove(alien);

                                // increasing the difficulty after every 10 aliens killed
                                if (score > 0 && score % 5000 == 0) {
                                    enterRate *= 0.85;         // aliens start entering more frequently
                                    alienVelocity *= 1.1;      // aliens start travelling faster
                                    if (wordLengths[2] < 9) {  // words start getting longer
                                        for (int j = 0; j < 3; j++) {
                                            wordLengths[j]++;
                                        }
                                    }
                                }
                                score += 500;
                                scoreDisplay.setText("score: " + score);
                            } else {
                                alien.drawAlien(frame);
                            }
                        }

                        // removing every alien when the "obliterate" alien is used
                        if (obliterateTyped) {
                            for (int i = 0; i < alienList.size(); i++) {
                                Alien alien = alienList.get(i);
                                score += 500;
                                scoreDisplay.setText("score: " + score);
                                alien.deleteAlien(frame);
                                alienList.remove(alien);
                            }

                            if (alienList.size() == 0) {
                                frame.remove(obliterateAlien);
                                obliterate = false;
                                obliterateTyped = false;
                            }
                        }

                        // adding a life when the "restore life" alien is used
                        if (restoreLife && enteredWord.equals("restore life")) {
                            numLives += 1;
                            lives[numLives - 1].setIcon(heart);
                            frame.remove(restoreLifeAlien);
                            restoreLife = false;
                        }

                        lastTick = currTick;
                        frame.repaint();
                    }
                }
            }

            // game over screen
            if (gameOver) {
                alienList.clear();

                // displaying the score of the game and the high score
                if (newHighScore) {
                    topSubtitle.setText("NEW HIGH SCORE");
                    newHighScore = false;
                } else {
                    topSubtitle.setText("HIGH SCORE: " + getHighScore(difficultyLevel));
                }
                frame.getContentPane().add(topSubtitle);

                title.setText("GAME OVER!");
                frame.getContentPane().add(title);

                bottomSubtitle.setText("SCORE: " + score);
                frame.getContentPane().add(bottomSubtitle);

                // displaying an image of a dead turtle
                displayTurtle.setIcon(deadTurtle);
                frame.getContentPane().add(displayTurtle);

                // buttons to play again and see high scores, and a JComboBox to select difficulty again
                playButton.setText("Play Again");
                frame.getContentPane().add(playButton);
                frame.getContentPane().add(selectDifficulty);
                frame.getContentPane().add(highScoresButton);

                frame.getContentPane().add(background);
                frame.setVisible(true);

                // resetting variables to prepare for a new game
                input.setText("");
                currentInput = "";
                gameOver = false;
                frame.repaint();
            }


        }
    }

    /**
     * Gets the current high score based on the difficulty level
     * pre: difficulty >= 0 && difficulty <= 3
     * post: returns an integer representing the high score of that difficulty level
     */
    public static int getHighScore(int difficulty) {
        File highScoreFile = new File (scoreFiles[difficulty]);
        if (highScoreFile.exists()) {
            try {
                Scanner reader = new Scanner(highScoreFile);
                int high = Integer.parseInt(reader.next());
                reader.close();
                return high;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Changes the high score of a certain difficulty level
     * pre: difficulty >= 0 && difficulty <= 3 && newScore > 0
     * post: the new high score is written to the txt file
     */
    public static void setHighScore(int difficulty, int newScore) {
        try {
            FileWriter writer = new FileWriter(scoreFiles[difficulty]);
            writer.write(Integer.toString(newScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}