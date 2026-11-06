import javax.swing.*;
import java.awt.*;

public class GameUI extends JFrame {
    private CardLayout card;
    private JPanel root;

    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    public GameUI() {
        setTitle("Escape From Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        card = new CardLayout();
        root = new JPanel(card);

        menuPanel = new MenuPanel(this);

        root.add(menuPanel, "MENU");
        add(root);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        showMenu();
    }

    public void showMenu() {
        card.show(root, "MENU");
        menuPanel.requestFocusInWindow();
    }

    public void startGame(MazeMap.Difficulty diff) {
        // 난이도별 제한시간
        int timeLimitSec = (diff == MazeMap.Difficulty.EASY) ? 90 :
                           (diff == MazeMap.Difficulty.NORMAL) ? 60 : 45;

        MazeMap maze = new MazeMap(diff);
        Player player = new Player(1,1);
        GameManager manager = new GameManager(maze, player, timeLimitSec);

        // 기존 게임 패널이 있으면 제거하고 새로 붙임
        if (gamePanel != null) root.remove(gamePanel);

        gamePanel = new GamePanel(manager, timeLimitSec, this);
        root.add(gamePanel, "GAME");

        pack(); // 크기 갱신
        card.show(root, "GAME");

        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameUI::new);
    }
}
