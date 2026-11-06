import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final GameUI ui;

    public MenuPanel(GameUI ui) {
        this.ui = ui;

        setPreferredSize(new Dimension(9 * 48, 7 * 48)); // 게임 패널과 비슷한 크기
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 12, 18));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 20, 10, 20);

        JLabel title = new JLabel("ESCAPE FROM MAZE", SwingConstants.CENTER);
        title.setForeground(new Color(235, 245, 255));
        title.setFont(new Font("Arial", Font.BOLD, 34));

        JLabel sub = new JLabel("Choose your game difficulty", SwingConstants.CENTER);
        sub.setForeground(new Color(120, 170, 255));
        sub.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton easy = makeBtn("EASY (90s)", new Color(0, 255, 170));
        JButton normal = makeBtn("NORMAL (60s)", new Color(120, 170, 255));
        JButton hard = makeBtn("HARD (45s)", new Color(255, 120, 120));

        JLabel hint = new JLabel("WASD Move / R Restart / ESC Exit", SwingConstants.CENTER);
        hint.setForeground(new Color(200, 210, 230));
        hint.setFont(new Font("Arial", Font.PLAIN, 14));

        c.gridy = 0; add(title, c);
        c.gridy = 1; add(sub, c);
        c.gridy = 2; add(easy, c);
        c.gridy = 3; add(normal, c);
        c.gridy = 4; add(hard, c);
        c.gridy = 5; add(hint, c);

        easy.addActionListener(e -> ui.startGame(MazeMap.Difficulty.EASY));
        normal.addActionListener(e -> ui.startGame(MazeMap.Difficulty.NORMAL));
        hard.addActionListener(e -> ui.startGame(MazeMap.Difficulty.HARD));
    }

    private JButton makeBtn(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setBackground(new Color(15, 18, 28));
        b.setForeground(new Color(235, 245, 255));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 200), 2),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        return b;
    }
}
