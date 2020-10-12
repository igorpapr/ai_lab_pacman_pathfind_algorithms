package com.zetcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotColor = new Color(212, 30, 169);
    private Color mazeColor;

    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24;

    public int getN_BLOCKS() {
        return N_BLOCKS;
    }

    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int PACMAN_SPEED = 6;

    private int pacAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int pacmanAnimPos = 0;
    private int pacsLeft, score;

    private Image ghost;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy, view_dx, view_dy;

    /*
        1st bit: wall on left
        2nd bit: wall on top
        3rd bit: wall on right
        4th bit: wall on bottom
        5th bit: CANDY
        6th bit: fill cell
     */

    // shitty plus sign before one-sign numbers to have beauty formatting
    private final short levelData[] = {
            +3, 10, 10, 10, +2, +2, +2, +2, +2, +2, +2, +2, +2, +2, +6,
            +5, 32, 32, 32, +1, +0, +0, +0, +0, +0, +0, +0, +0, +0, +4,
            +5, 32, 32, 32, +1, +0, +0, +0, +0, +0, +0, +0, +0, +0, +4,
            +5, 32, 32, 32, +1, +0, +0, +8, +0, +0, +0, +0, +0, +0, +4,
            +1, +2, +2, +2, +0, +0, +4, 32, +1, +0, +0, +0, +0, +0, +4,
            +1, +0, +0, +0, +0, +0, +4, 32, +1, +0, +0, +0, +0, +8, +4,
            +9, +0, +0, +0, +8, +8, 12, 32, +9, +8, +8, +0, +4, 32, +5,
            33, +1, +0, +4, 32, 32, 32, 32, 32, 32, 32, +1, +4, 32, +5,
            33, +1, +0, +0, +2, +2, +6, 32, +3, +2, +2, +0, +4, 32, +5,
            33, +1, +0, +0, +0, +0, +4, 32, +1, +0, +0, +0, +4, 32, +5,
            33, +1, +0, +0, +0, +0, +4, 32, +1, +0, +0, +0, +4, 32, +5,
            33, +1, +0, +0, +0, +0, +0, +2, +0, +0, +0, +0, +4, 32, +5,
            33, +1, +0, +0, +0, +0, +0, +0, +0, +0, +0, +0, +4, 32, +5,
            33, +9, +8, +8, +8, +8, +8, +8, +8, +8, +0, +0, +0, +2, +4,
            41, 40, 40, 40, 40, 40, 40, 40, 40, 40, +9, +8, +8, +8, 12
    };

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    private int candyPos;
    public final HashMap<Integer, LinkedList<Integer>> path = new HashMap<>();

    public Board() {
        this(false);
    }

    public Board(boolean inGame) {
        this.inGame = inGame;
        loadImages();
        initVariables();
        initBoard();

        if (inGame) {
            initGame();
        }
    }

    private void placeRandomCandy() {
        int pos = ThreadLocalRandom.current().nextInt(N_BLOCKS * N_BLOCKS);

        while ((screenData[pos] & 32) != 0) {
            pos = ThreadLocalRandom.current().nextInt(N_BLOCKS * N_BLOCKS);
        }
        screenData[pos] = (short) (screenData[pos] | 16);
        candyPos = pos;
    }

    public int getCandyPos() {
        return candyPos;
    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        mazeColor = new Color(5, 100, 5);
        d = new Dimension(400, 400);

        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        pacAnimCount--;

        if (pacAnimCount <= 0) {
            pacAnimCount = PAC_ANIM_DELAY;
            pacmanAnimPos = pacmanAnimPos + pacAnimDir;

            if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
                pacAnimDir = -pacAnimDir;
            }
        }
    }

    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);

            //checkMaze(); // maybe will use later
        }
    }

    private void showIntroScreen(Graphics2D g2d) {

        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

        String s = "Press s to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }

    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (i = 0; i < pacsLeft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void death() {

        pacsLeft--;

        if (pacsLeft == 0) {
            inGame = false;
        }

        continueLevel();
    }

    private void movePacman() {

        int pos;
        short ch;

        if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
            pacmand_x = req_dx;
            pacmand_y = req_dy;
            view_dx = pacmand_x;
            view_dy = pacmand_y;
        }

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
                inGame = false;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                    view_dx = pacmand_x;
                    view_dy = pacmand_y;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {

        if (view_dx == -1) {
            drawPacnanLeft(g2d);
        } else if (view_dx == 1) {
            drawPacmanRight(g2d);
        } else if (view_dy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacnanLeft(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    public Optional<Integer> canMoveUp(int pos) {
//        return (pos / N_BLOCKS >= 1) && (screenData[pos - N_BLOCKS] & 32) == 0;
        if ((pos / N_BLOCKS >= 1) && (screenData[pos - N_BLOCKS] & 32) == 0) {
            return Optional.of(pos - N_BLOCKS);
        }
        return Optional.empty();
    }

    public Optional<Integer> canMoveDown(int pos) {
        if ((pos / N_BLOCKS <= N_BLOCKS - 2) && (screenData[pos + N_BLOCKS] & 32) == 0) {
            return Optional.of(pos + N_BLOCKS);
        }
        return Optional.empty();
    }

    public Optional<Integer> canMoveLeft(int pos) {
        if ((pos % N_BLOCKS >= 1) && (screenData[pos - 1] & 32) == 0) {
            return Optional.of(pos - 1);
        }
        return Optional.empty();
    }

    public Optional<Integer> canMoveRight(int pos) {
        if ((pos % N_BLOCKS <= N_BLOCKS - 2) && (screenData[pos + 1] & 32) == 0) {
            return Optional.of(pos + 1);
        }
        return Optional.empty();
    }

    public boolean candyIsUnderneath(int pos) {
        return (screenData[pos] & 16) != 0;
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 32) != 0) {
                    g2d.fillRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                }


                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    //g2d.fillRect(x + 11, y + 11, 8, 8);
                    g2d.fillOval(x + 5, y + 5, 12, 12);
                }

                i++;
            }
        }
    }

    private void initGame() {

        pacsLeft = 3;
        score = 0;
        initLevel();
        currentSpeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        placeRandomCandy();

        continueLevel();
    }

    private void continueLevel() {

//        short i;
//        int dx = 1;
//        int random;

//        for (i = 0; i < N_GHOSTS; i++) {
//
//            ghost_y[i] = 4 * BLOCK_SIZE;
//            ghost_x[i] = 4 * BLOCK_SIZE;
//            ghost_dy[i] = 0;
//            ghost_dx[i] = dx;
//            dx = -dx;
//            random = (int) (Math.random() * (currentSpeed + 1));
//
//            if (random > currentSpeed) {
//                random = currentSpeed;
//            }
//
//            ghostSpeed[i] = validSpeeds[random];
//        }

        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;
    }

    private void loadImages() {
        pacman1 = new ImageIcon("src/resources/images/pacman.png").getImage();
        pacman2up = new ImageIcon("src/resources/images/up1.png").getImage();
        pacman3up = new ImageIcon("src/resources/images/up2.png").getImage();
        pacman4up = new ImageIcon("src/resources/images/up3.png").getImage();
        pacman2down = new ImageIcon("src/resources/images/down1.png").getImage();
        pacman3down = new ImageIcon("src/resources/images/down2.png").getImage();
        pacman4down = new ImageIcon("src/resources/images/down3.png").getImage();
        pacman2left = new ImageIcon("src/resources/images/left1.png").getImage();
        pacman3left = new ImageIcon("src/resources/images/left2.png").getImage();
        pacman4left = new ImageIcon("src/resources/images/left3.png").getImage();
        pacman2right = new ImageIcon("src/resources/images/right1.png").getImage();
        pacman3right = new ImageIcon("src/resources/images/right2.png").getImage();
        pacman4right = new ImageIcon("src/resources/images/right3.png").getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        drawPath(g2d);

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void drawPath(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        drawPath(g2d, 172);
    }

    private void drawPath(Graphics2D g2d, int p1) {
        var points = path.getOrDefault(p1, new LinkedList<>());
        for (int p2 : points) {
            var x1 = p1 % N_BLOCKS * BLOCK_SIZE + BLOCK_SIZE / 2;
            var y1 = p1 / N_BLOCKS * BLOCK_SIZE + BLOCK_SIZE / 2;
            var x2 = p2 % N_BLOCKS * BLOCK_SIZE + BLOCK_SIZE / 2;
            var y2 = p2 / N_BLOCKS * BLOCK_SIZE + BLOCK_SIZE / 2;
            g2d.drawLine(x1, y1, x2, y2);
            drawPath(g2d, p2);
        }
    }

    public void addPathAfter(int prev, int next) {
        var list = path.getOrDefault(prev, new LinkedList<>());
        list.add(next);
        path.put(prev, list);
    }

    public void removePathAfter(int pos) {
        path.remove(pos);
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
                if (key == 's' || key == 'S') {
                    inGame = true;
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                req_dx = 0;
                req_dy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
