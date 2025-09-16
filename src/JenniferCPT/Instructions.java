package JenniferCPT;
/* Instructions screen (BACK button, background, no HierarchyListener) */
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Instructions extends JPanel implements Runnable {
    private Thread repaintThread;
    private volatile boolean running = false;
    private final Image background = Images.instructions; // may be null

    public Instructions() {
        super();
        setLayout(null);

        JButton back = new JButton("BACK");
        back.addActionListener(e -> Main.navigation.show(Main.centerPanel, "Menu"));
        back.setForeground(new Color(253, 217, 52));
        back.setBackground(new Color(238, 28, 39));
        back.setBounds(Main.WINDOW_WIDTH - 160, Main.WINDOW_HEIGHT - 120, 80, 30);
        add(back);
    }

    // Start the repaint loop when the component is realized/added
    @Override public void addNotify() {
        super.addNotify();
        if (!running) {
            running = true;
            repaintThread = new Thread(this, "InstructionsRepaint");
            repaintThread.start();
        }
    }

    // Stop the repaint loop when the component is removed/disposed
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
        if (background != null) g.drawImage(background, 0, 0, this);
    }
}
