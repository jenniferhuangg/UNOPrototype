package JenniferCPT;

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {
    public static final int WINDOW_WIDTH = 1100, WINDOW_HEIGHT = 650;
    public static final CardLayout navigation = new CardLayout();
    public static JFrame frame;
    public static JPanel centerPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("UNO");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setLocationRelativeTo(null);

            centerPanel = new JPanel(Main.navigation);
            frame.add(centerPanel, BorderLayout.CENTER);

            centerPanel.add(new Menu(), "Menu");
            centerPanel.add(new Instructions(), "Instructions");
            centerPanel.add(new Win(), "Winner!!");
            centerPanel.add(new Lose(), "Loser!!");

            navigation.show(centerPanel, "Menu");
            frame.setVisible(true);
        });
    }
}
