import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final GameManager manager;
    private final GameUI ui;

    private final int CELL = 48;
    private final int HUD_H = 46;
    private final int TIME_LIMIT_SEC;

    private int bumpFlashFrames = 0;
    private int wallTextFrames = 0;

    private final Color C_BG = new Color(10, 12, 18);
    private final Color C_FLOOR = new Color(20, 24, 33);
    private final Color C_TEXT = new Color(235, 245, 255);
    private final Color C_UI = new Color(0, 0, 0, 170);

    private final Color C_NEON_WALL = new Color(120, 170, 255);
    private final Color C_GOAL = new Color(0, 255, 170);
    private final Color C_PLAYER = new Color(255, 70, 90);

    private Timer gameTimer;

    public GamePanel(GameManager manager, int timeLimitSec, GameUI ui) {
        this.manager = manager;
        this.TIME_LIMIT_SEC = timeLimitSec;
        this.ui = ui;

        int w = manager.getMaze().getWidth() * CELL;
        int h = manager.getMaze().getHeight() * CELL;
        setPreferredSize(new Dimension(w, h));
        setFocusable(true);

        gameTimer = new Timer(1000, e -> {
            manager.tick();
            repaint();
        });
        gameTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ESCAPE) System.exit(0);

                // M: 메뉴로 돌아가기 
                if (code == KeyEvent.VK_M) {
                    gameTimer.stop();
                    ui.showMenu();
                    return;
                }

                // R: 재시작
                if (code == KeyEvent.VK_R) {
                    manager.restart(TIME_LIMIT_SEC);
                    bumpFlashFrames = 0;
                    wallTextFrames = 0;
                    repaint();
                    return;
                }

                if (manager.getState() != GameManager.State.PLAYING) return;

                if (code == KeyEvent.VK_W) manager.move(0, -1);
                else if (code == KeyEvent.VK_S) manager.move(0, 1);
                else if (code == KeyEvent.VK_A) manager.move(-1, 0);
                else if (code == KeyEvent.VK_D) manager.move(1, 0);

                if (manager.consumeBumped()) {
                    Toolkit.getDefaultToolkit().beep();
                    bumpFlashFrames = 6;
                    wallTextFrames = 18;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[][] map = manager.getMaze().getMap();

        g2.setColor(C_BG);
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawMazeFloor(g2, map);
        drawGoal(g2, map);
        drawMazeLines(g2, map);
        drawPlayer(g2);
        drawHud(g2);

        if (bumpFlashFrames > 0) {
            g2.setColor(new Color(255, 60, 60, 70));
            g2.fillRect(0, 0, getWidth(), getHeight());
            bumpFlashFrames--;
        }

        if (wallTextFrames > 0) {
            drawCenteredBadge(g2, "WALL!", new Color(255, 70, 90), new Color(0, 0, 0, 180));
            wallTextFrames--;
        }

        if (manager.isCleared() || manager.isTimeOver()) {
            drawEndOverlay(g2);
        }
    }

    private void drawMazeFloor(Graphics2D g2, int[][] map) {
        g2.setColor(C_FLOOR);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] != 1) {
                    int px = x * CELL;
                    int py = y * CELL;
                    g2.fillRect(px + 2, py + 2, CELL - 4, CELL - 4);
                }
            }
        }
    }

    private void drawMazeLines(Graphics2D g2, int[][] map) {
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(C_NEON_WALL.getRed(), C_NEON_WALL.getGreen(), C_NEON_WALL.getBlue(), 220));

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 1) continue;

                int x0 = x * CELL;
                int y0 = y * CELL;
                int x1 = x0 + CELL;
                int y1 = y0 + CELL;

                if (isWall(map, x, y - 1)) g2.drawLine(x0, y0, x1, y0);
                if (isWall(map, x, y + 1)) g2.drawLine(x0, y1, x1, y1);
                if (isWall(map, x - 1, y)) g2.drawLine(x0, y0, x0, y1);
                if (isWall(map, x + 1, y)) g2.drawLine(x1, y0, x1, y1);
            }
        }
        g2.setStroke(new BasicStroke(1));
    }

    private void drawGoal(Graphics2D g2, int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 2) {
                    int px = x * CELL;
                    int py = y * CELL;

                    g2.setColor(new Color(C_GOAL.getRed(), C_GOAL.getGreen(), C_GOAL.getBlue(), 60));
                    g2.fillOval(px + 6, py + 6, CELL - 12, CELL - 12);

                    int pad = CELL / 4;
                    g2.setColor(new Color(C_GOAL.getRed(), C_GOAL.getGreen(), C_GOAL.getBlue(), 220));
                    g2.fillRoundRect(px + pad, py + pad, CELL - pad * 2, CELL - pad * 2, 14, 14);

                    g2.setColor(new Color(C_GOAL.getRed(), C_GOAL.getGreen(), C_GOAL.getBlue(), 255));
                    g2.drawRoundRect(px + pad, py + pad, CELL - pad * 2, CELL - pad * 2, 14, 14);
                }
            }
        }
    }

    private void drawPlayer(Graphics2D g2) {
        Player p = manager.getPlayer();
        int px = p.getX() * CELL;
        int py = p.getY() * CELL;

        g2.setColor(new Color(C_PLAYER.getRed(), C_PLAYER.getGreen(), C_PLAYER.getBlue(), 90));
        g2.fillOval(px + 5, py + 5, CELL - 10, CELL - 10);

        g2.setColor(C_PLAYER);
        g2.fillOval(px + 9, py + 9, CELL - 18, CELL - 18);
    }

    private void drawHud(Graphics2D g2) {
        g2.setColor(C_UI);
        g2.fillRect(0, 0, getWidth(), HUD_H);

        g2.setColor(new Color(C_NEON_WALL.getRed(), C_NEON_WALL.getGreen(), C_NEON_WALL.getBlue(), 120));
        g2.drawLine(0, HUD_H - 1, getWidth(), HUD_H - 1);

        g2.setFont(new Font("Arial", Font.BOLD, 16));

        if (manager.getTimeLeftSec() <= 10 && manager.getState() == GameManager.State.PLAYING) {
            g2.setColor(new Color(255, 120, 120));
        } else {
            g2.setColor(C_TEXT);
        }
        g2.drawString("TIME: " + manager.getTimeLeftSec() + "s", 12, 28);

        g2.setColor(C_TEXT);
        g2.drawString("WASD Move | R Restart | M Menu | ESC Exit", 150, 28);
    }

    private void drawEndOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, getWidth(), getHeight());

        String title = manager.isCleared() ? "CLEAR!" : "TIME OVER";
        String sub = "R: Restart   M: Menu   ESC: Exit";

        Color neon = manager.isCleared() ? C_GOAL : new Color(255, 120, 120);

        int cardW = Math.min(520, getWidth() - 60);
        int cardH = 180;
        int cx = (getWidth() - cardW) / 2;
        int cy = (getHeight() - cardH) / 2;

        g2.setColor(new Color(15, 18, 28, 235));
        g2.fillRoundRect(cx, cy, cardW, cardH, 24, 24);

        g2.setColor(new Color(neon.getRed(), neon.getGreen(), neon.getBlue(), 210));
        g2.drawRoundRect(cx, cy, cardW, cardH, 24, 24);

        g2.setFont(new Font("Arial", Font.BOLD, 46));
        g2.setColor(neon);
        drawCenteredString(g2, title, getWidth() / 2, cy + 80);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(C_TEXT);
        drawCenteredString(g2, sub, getWidth() / 2, cy + 125);
    }

    private void drawCenteredBadge(Graphics2D g2, String msg, Color neon, Color bg) {
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(msg);

        int padX = 18, padY = 10;
        int boxW = textW + padX * 2;
        int boxH = fm.getHeight() + padY * 2;

        int x = (getWidth() - boxW) / 2;
        int y = (getHeight() - boxH) / 2 - 60;

        g2.setColor(bg);
        g2.fillRoundRect(x, y, boxW, boxH, 18, 18);

        g2.setColor(new Color(neon.getRed(), neon.getGreen(), neon.getBlue(), 220));
        g2.drawRoundRect(x, y, boxW, boxH, 18, 18);

        g2.setColor(new Color(neon.getRed(), neon.getGreen(), neon.getBlue(), 120));
        g2.drawString(msg, x + padX + 1, y + padY + fm.getAscent() + 1);

        g2.setColor(neon);
        g2.drawString(msg, x + padX, y + padY + fm.getAscent());
    }

    private void drawCenteredString(Graphics2D g2, String text, int centerX, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = centerX - fm.stringWidth(text) / 2;
        g2.drawString(text, x, y);
    }

    private boolean isWall(int[][] map, int x, int y) {
        if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) return true;
        return map[y][x] == 1;
    }
}
