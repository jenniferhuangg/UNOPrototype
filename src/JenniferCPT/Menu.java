package JenniferCPT;
/* Menu screen that launches Game properly */
import java.awt.*;
import javax.swing.*;

public class Menu extends JPanel {
    JButton play = new JButton("PLAY");
    JButton instructions = new JButton("INSTRUCTIONS");
    JButton name = new JButton("NAME");
    JButton speed = new JButton("SPEED");
    ImageIcon sound = new ImageIcon(Images.unosound);
    JButton music = new JButton(sound);

    public static String getName = "Guest";
    public static String getSpeed = "20"; // default seconds per turn

    Image background = Images.unomain;

    public Menu() {
        super();
        setLayout(null);

        // PLAY -> create brand new Game panel, add, show, start its thread
        play.addActionListener(e -> {
            try {
                for (Component c : Main.centerPanel.getComponents()) {
                    if (c instanceof Game) { Main.centerPanel.remove(c); break; }
                }
                Game g = new Game();
                Main.centerPanel.add(g, "Game");
                Main.navigation.show(Main.centerPanel, "Game");
                Main.centerPanel.revalidate();
                Main.centerPanel.repaint();
                new Thread(g, "GameLoop").start();   // <- run() starts
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to start game: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        instructions.addActionListener(e -> Main.navigation.show(Main.centerPanel, "Instructions"));

        name.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(null, "What's your name?", "NAME",
                    JOptionPane.QUESTION_MESSAGE);
            if (s != null && !s.trim().isEmpty()) getName = s.trim();
        });

        speed.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(null, "Enter seconds per turn (5–120)", "SPEED",
                    JOptionPane.QUESTION_MESSAGE);
            if (s != null && !s.trim().isEmpty()) getSpeed = s.trim();
        });

        music.addActionListener(e -> { /* music() if you add it later */ });

        add(play); add(instructions); add(name); add(speed); add(music);

        play.setBounds((Main.WINDOW_WIDTH - 80) / 2, 445, 80, 30);
        play.setForeground(new Color(253, 217, 52));
        play.setBackground(new Color(238, 28, 39));

        instructions.setBounds((Main.WINDOW_WIDTH - 160) / 2, 565, 160, 30);
        instructions.setForeground(new Color(253, 217, 52));
        instructions.setBackground(new Color(238, 28, 39));

        name.setBounds((Main.WINDOW_WIDTH - 80) / 2, 485, 80, 30);
        name.setForeground(new Color(253, 217, 52));
        name.setBackground(new Color(238, 28, 39));

        speed.setBounds((Main.WINDOW_WIDTH - 80) / 2, 525, 80, 30);
        speed.setForeground(new Color(253, 217, 52));
        speed.setBackground(new Color(238, 28, 39));

        music.setBounds(1000, 20, 80, 30);
        music.setBackground(new Color(238, 28, 39));
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        if (background != null) g.drawImage(background, 0, 0, this);
    }
}
