package JenniferCPT;
/* Lose screen (no HierarchyListener) */
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Lose extends JPanel implements Runnable {
    private Thread repaintThread;
    private volatile boolean running = false;
    private final Image background = Images.unolose; // may be null
    private final JButton back = new JButton("BACK");

    public Lose() {
        super();
        setLayout(null);

        back.addActionListener(e -> Main.navigation.show(Main.centerPanel, "Menu"));
        back.setForeground(new Color(253, 217, 52));
        back.setBackground(new Color(238, 28, 39));
        back.setBounds((Main.WINDOW_WIDTH - 80) / 2, 485, 80, 30);
        add(back);
    }

    @Override public void addNotify() {
        super.addNotify();
        if (!running) {
            running = true;
            repaintThread = new Thread(this, "LoseRepaint");
            repaintThread.start();
        }
    }

    @Override public void removeNotify() {
        stopThread();
        super.removeNotify();
    }

    private void stopThread() {
        running = false;
        if (repaintThread != null) {
            repaintThread.interrupt();
            repaintThread = null;
        }
    }

    @Override public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                repaint();
                if (isShowing()) requestFocusInWindow();
                Thread.sleep(50);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (background != null) {
            int w = getWidth(), h = getHeight();
            g.drawImage(background, 0, 0, w, h, this);
        }
    }
}
