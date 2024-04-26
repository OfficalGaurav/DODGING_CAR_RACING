import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CarRacingGame extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int DELAY = 10;
    private final int CAR_WIDTH = 40;
    private final int CAR_HEIGHT = 60;
    private final int OBSTACLE_WIDTH = 50;
    private final int OBSTACLE_HEIGHT = 50;

    private int carX = WIDTH / 2;
    private int carY = HEIGHT - 100;
    private int carSpeed = 0;
    private int obstacleX = WIDTH / 2;
    private int obstacleY = 0;
    private int obstacleSpeed = 2;
    private Timer timer;
    private boolean gameOver;

    public CarRacingGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawRoad(g);
        drawCar(g);
        drawObstacle(g);
        if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawRoad(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        for (int i = 0; i < HEIGHT; i += 40) {
            g.fillRect(WIDTH / 2 - 5, i, 10, 30);
        }
    }

    private void drawCar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(carX - CAR_WIDTH / 2, carY - CAR_HEIGHT / 2, CAR_WIDTH, CAR_HEIGHT);
    }

    private void drawObstacle(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(obstacleX - OBSTACLE_WIDTH / 2, obstacleY - OBSTACLE_HEIGHT / 2, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics fm = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (WIDTH - fm.stringWidth(msg)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveCar();
            moveObstacle();
            checkCollisions();
            repaint();
        }
    }

    private void moveCar() {
        carX += carSpeed;
        if (carX < CAR_WIDTH / 2) {
            carX = CAR_WIDTH / 2;
        }
        if (carX > WIDTH - CAR_WIDTH / 2) {
            carX = WIDTH - CAR_WIDTH / 2;
        }
    }

    private void moveObstacle() {
        obstacleY += obstacleSpeed;
        if (obstacleY > HEIGHT) {
            obstacleY = -OBSTACLE_HEIGHT;
            obstacleX = (int) (Math.random() * (WIDTH - OBSTACLE_WIDTH)) + OBSTACLE_WIDTH / 2;
        }
    }

    private void checkCollisions() {
        Rectangle carRect = new Rectangle(carX - CAR_WIDTH / 2, carY - CAR_HEIGHT / 2, CAR_WIDTH, CAR_HEIGHT);
        Rectangle obstacleRect = new Rectangle(obstacleX - OBSTACLE_WIDTH / 2, obstacleY - OBSTACLE_HEIGHT / 2, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

        if (carRect.intersects(obstacleRect)) {
            gameOver = true;
            timer.stop();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                carSpeed = -5;
            }
            if (key == KeyEvent.VK_RIGHT) {
                carSpeed = 5;
            }
            if (key == KeyEvent.VK_UP) {
                carSpeed++;
            }
            if (key == KeyEvent.VK_DOWN) {
                carSpeed--;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                carSpeed = 0;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Car Racing Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CarRacingGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}