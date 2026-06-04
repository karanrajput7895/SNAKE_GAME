import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel implements ActionListener, KeyListener {

    static final int WIDTH = 600, HEIGHT = 600;
    static final int UNIT = 25;
    static final int UNITS = (WIDTH * HEIGHT) / (UNIT * UNIT);
    static final int DELAY = 120;

    int[] x = new int[UNITS];
    int[] y = new int[UNITS];
    int bodyParts = 4;
    int foodX, foodY;
    int score = 0;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random = new Random();

    Main() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    void startGame() {
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    void spawnFood() {
        foodX = random.nextInt(WIDTH / UNIT) * UNIT;
        foodY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // Food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT, UNIT);
            // Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT, UNIT);
            }
            // Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25);
        } else {
            gameOver(g);
        }
    }

    void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over!", 150, 300);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, 250, 350);
    }

    void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= UNIT; break;
            case 'D': y[0] += UNIT; break;
            case 'L': x[0] -= UNIT; break;
            case 'R': x[0] += UNIT; break;
        }
    }

    void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            score += 10;
            spawnFood();
        }
    }

    void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) running = false;
        }
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) running = false;
        if (!running) timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) { move(); checkFood(); checkCollision(); }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN:  if (direction != 'U') direction = 'D'; break;
            case KeyEvent.VK_LEFT:  if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        Main game = new Main();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}