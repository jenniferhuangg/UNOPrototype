package JenniferCPT;
/* UNO GUI — bots limited to +2/wild right after +2/wild is played; no user skip added; stockpile centered */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable {

    // ===== Hands =====
    private final java.util.List<Card> human = new ArrayList<>();
    private final java.util.List<Card> bot1  = new ArrayList<>();
    private final java.util.List<Card> bot2  = new ArrayList<>();
    private final java.util.List<Card> bot3  = new ArrayList<>();
    private final java.util.List<java.util.List<Card>> turn = Arrays.asList(human, bot1, bot2, bot3);

    // +1 clockwise, -1 counterclockwise
    private static int direction = +1;

    // Stockpile (top at index 0)
    private final java.util.List<Card> stockpile = new ArrayList<>();

    // ===== UI =====
    private final JPanel top = new JPanel();
    private final JPanel left = new JPanel();
    private final JPanel right = new JPanel();
    private final JPanel center = new JPanel();     // holds a centered wrapper
    private final JPanel bottom = new JPanel();
    private final JPanel bottoml = new JPanel();
    private final JPanel bottomr = new JPanel();
    private final JPanel bottomc = new JPanel();    // player's hand (centered)

    private String colourchoice; // last chosen colour
    private final JLabel stockpilecard = new JLabel();
    private final Object[] options = { "red", "yellow", "green", "blue" };

    // Controls
    private final JButton unob = new JButton(new ImageIcon(Images.unobutton));
    private final JButton deck = new JButton(new ImageIcon(Images.unostack));

    // Labels
    private final JLabel humanname = new JLabel(Menu.getName);
    private final JLabel bot1name = new JLabel("Bot 1 🤖");
    private final JLabel bot2name = new JLabel("Bot 2 🤖");
    private final JLabel bot3name = new JLabel("Bot 3 🤖");

    private final JLabel bot1count = new JLabel("# of Cards: 0");
    private final JLabel bot2count = new JLabel("# of Cards: 0");
    private final JLabel bot3count = new JLabel("# of Cards: 0");
    private final JLabel humancount = new JLabel("# of Cards: 0");

    // Arrows
    private final ImageIcon icoClock1   = new ImageIcon(Images.clock1);
    private final ImageIcon icoClock2   = new ImageIcon(Images.clock2);
    private final ImageIcon icoCounter1 = new ImageIcon(Images.counter1);
    private final ImageIcon icoCounter2 = new ImageIcon(Images.counter2);

    private final JLabel clockwise1   = new JLabel(icoClock1);
    private final JLabel clockwise2   = new JLabel(icoClock2);
    private final JLabel counterwise1 = new JLabel(icoCounter1);
    private final JLabel counterwise2 = new JLabel(icoCounter2);

    private ImageIcon flipClock1, flipClock2, flipCounter1, flipCounter2;
    private boolean arrowsFlipped = false; // toggled ONLY on reverse

    // Bot deck backs
    private final JLabel bot1deck = new JLabel(new ImageIcon(Images.unodeck));
    private final JLabel bot2deck = new JLabel(new ImageIcon(Images.unodeck));
    private final JLabel bot3deck = new JLabel(new ImageIcon(Images.unodeck));

    // Turn + timer
    private final JLabel playerturn = new JLabel();
    private static int currentindex = 0;
    private static int secondsLeft  = 20; // default for human (bots use 10)
    private final JLabel timercount = new JLabel();

    // UNO tracking
    private static final int[] wincount = new int[4];
    private static int skipNext = 0;

    // One Swing timer (UI-safe)
    private final javax.swing.Timer tickTimer;

    // Track if current player already acted (bots)
    private boolean actedThisTurn = false;

    // ===== NEW: restrict next bot after +2 or wild =====
    private boolean restrictNextBotToPlusOrWild = false;

    public Game() {
        super();
        setLayout(new BorderLayout());
        Color bg = new Color(255, 110, 66);
        for (JPanel p : new JPanel[]{top,left,right,center,bottom,bottoml,bottomr,bottomc}) p.setBackground(bg);
        setBackground(bg);

        // Center the player's hand row
        bottomc.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));

        // Make UNO button clearly visible
        unob.setOpaque(true);
        unob.setContentAreaFilled(true);
        unob.setBackground(Color.WHITE);
        unob.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        unob.setToolTipText("Press when you have 2 cards on your turn");

        // Precompute vertically flipped arrow icons
        flipClock1   = verticalFlip(icoClock1);
        flipClock2   = verticalFlip(icoClock2);
        flipCounter1 = verticalFlip(icoCounter1);
        flipCounter2 = verticalFlip(icoCounter2);

        // ===== Deal initial hands =====
        addCards(human, 7, true);
        addCards(bot1,  7, false);
        addCards(bot2,  7, false);
        addCards(bot3,  7, false);

        // ===== SAFE STOCKPILE START =====
        Card starter;
        do { starter = randomCard(); } while (isActionCard(starter));
        stockpile.clear();
        stockpile.add(0, starter);
        updatestockpile();

        // ===== Buttons =====
        unob.addActionListener(e -> {
            if (currentindex == 0 && human.size() == 2) {
                wincount[0] = 1;
                unob.setEnabled(false);
            }
        });

        // HUMAN draw-one then try to play immediately
        deck.addActionListener(e -> {
            if (currentindex != 0) return;   // human only
            drawOneThenTryPlay(human, true);
            updateCounts();
        });

        // ===== Layout =====
        // LEFT (Bot 1)
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        bot1deck.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot1name.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot1count.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(bot1deck);
        left.add(Box.createVerticalStrut(8));
        left.add(bot1name);
        left.add(Box.createVerticalStrut(4));
        left.add(bot1count);

        // TOP (Bot 2)
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bot2deck.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot2name.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot2count.setAlignmentX(Component.CENTER_ALIGNMENT);
        top.add(bot2deck);
        top.add(Box.createVerticalStrut(8));
        top.add(bot2name);
        top.add(Box.createVerticalStrut(4));
        top.add(bot2count);

        // RIGHT (Bot 3)
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        bot3deck.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot3name.setAlignmentX(Component.CENTER_ALIGNMENT);
        bot3count.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(bot3deck);
        right.add(Box.createVerticalStrut(8));
        right.add(bot3name);
        right.add(Box.createVerticalStrut(4));
        right.add(bot3count);

        // BOTTOM LEFT: timer + human name
        bottoml.setLayout(new BoxLayout(bottoml, BoxLayout.X_AXIS));
        timercount.setOpaque(true);
        timercount.setBackground(Color.WHITE);
        bottoml.add(timercount);
        bottoml.add(Box.createHorizontalStrut(12));
        bottoml.add(humanname);

        // BOTTOM RIGHT: UNO + human count
        bottomr.setLayout(new BoxLayout(bottomr, BoxLayout.X_AXIS));
        bottomr.add(unob);
        bottomr.add(Box.createHorizontalStrut(12));
        bottomr.add(humancount);

        bottom.setLayout(new BorderLayout());
        playerturn.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.add(playerturn, BorderLayout.NORTH);
        bottom.add(bottomc,   BorderLayout.CENTER);
        bottom.add(bottoml,   BorderLayout.WEST);
        bottom.add(bottomr,   BorderLayout.EAST);

        // ===== CENTER ROW (CENTERED) =====
        center.setLayout(new BorderLayout());
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        mid.setOpaque(false);

        // Start with clockwise visible; counter hidden
        counterwise1.setVisible(false);
        counterwise2.setVisible(false);

        mid.add(clockwise1);
        mid.add(counterwise1);
        mid.add(Box.createHorizontalStrut(12));
        mid.add(deck);
        mid.add(Box.createHorizontalStrut(12));
        mid.add(stockpilecard);
        mid.add(Box.createHorizontalStrut(12));
        mid.add(clockwise2);
        mid.add(counterwise2);

        center.add(mid, BorderLayout.CENTER);

        add(top,    BorderLayout.NORTH);
        add(left,   BorderLayout.WEST);
        add(right,  BorderLayout.EAST);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // ===== Timer every second =====
        tickTimer = new javax.swing.Timer(1000, e -> {
            secondsLeft--;
            timercount.setText("TIMER: " + secondsLeft + " seconds");
            if (secondsLeft <= 0) onTimeout();
        });

        updateCounts();
        startTurn(0);
    }

    // ========================= Turn Flow =========================

    private void startTurn(int playerIdx) {
        currentindex = playerIdx;
        actedThisTurn = false;

        // Human uses Menu.getSpeed (fallback 20). Bots fixed at 10s.
        if (currentindex == 0) {
            try {
                int s = Integer.parseInt(Menu.getSpeed);
                if (s < 5) s = 5; else if (s > 120) s = 120;
                secondsLeft = s;
            } catch (Exception ignore) {
                secondsLeft = 20;
            }
        } else {
            secondsLeft = 10; // each bot visible for 10 seconds
        }

        timercount.setText("TIMER: " + secondsLeft + " seconds");
        playerturn.setText(nameOf(playerIdx) + " turn");

        unob.setEnabled(currentindex == 0 && human.size() == 2);
        enableHumanButtons(currentindex == 0);

        tickTimer.restart();

        // Let bots act once early, but stay visible until timeout
        if (currentindex > 0) {
            javax.swing.Timer botThink = new javax.swing.Timer(900, e -> {
                if (!actedThisTurn) botActThenResolve();
            });
            botThink.setRepeats(false);
            botThink.start();
        }
    }

    private void onTimeout() {
        tickTimer.stop();

        if (currentindex > 0) {
            // Bot: if it didn’t act yet, draw one and (maybe) auto-play that one
            if (!actedThisTurn) drawOneThenTryPlay(turn.get(currentindex), false);
            updateCounts();
            endTurn(true);
            return;
        }

        // Human: draw-one then try to play immediately; then pass
        drawOneThenTryPlay(human, true);
        updateCounts();
        endTurn(true);
    }

    private void endTurn(boolean byTimeout) {
        applyUnoPenaltyIfNeeded(currentindex);

        for (int w = 0; w < 4; w++) {
            if (wincount[w] == 1 && turn.get(w).isEmpty()) {
                tickTimer.stop();
                resetAll();
                if (w == 0) Main.navigation.show(Main.centerPanel, "Winner!!");
                else        Main.navigation.show(Main.centerPanel, "Loser!!");
                return;
            }
        }

        int step = (skipNext == 1) ? 2 : 1;
        skipNext = 0;
        int next = mod4(currentindex + step * direction);
        startTurn(next);
    }

    /** Bots: try to play. If no play, draw exactly one; if that drawn card is playable, play it; else keep it. */
    private void botActThenResolve() {
        boolean played = false;
        java.util.List<Card> deckRef = turn.get(currentindex);

        // ===== NEW: if restricted (after +2/wild), bot may only play +2 or wild =====
        if (restrictNextBotToPlusOrWild) {
            int idx = findPlus2OrWildPlayableIndex(deckRef);
            if (idx >= 0) {
                put(deckRef, idx, false);
                played = true;
                actedThisTurn = true;
            } else {
                // draw one and try only if it's +2/wild and playable
                int before = deckRef.size();
                addCards(deckRef, 1, false);
                Card drawn = deckRef.get(before);
                if (isPlus2OrWild(drawn) && isPlayable(drawn)) {
                    put(deckRef, before, false);
                    played = true;
                    actedThisTurn = true;
                }
            }
            // restriction only applies to the immediate next bot turn
            restrictNextBotToPlusOrWild = false;

            if (deckRef.size() == 2) wincount[currentindex] = 1;
            if (played) resolveTopAction();
            updateCounts();
            return; // do not continue with normal logic this turn
        }

        // Normal bot behavior
        int idx = findPlayableIndex(deckRef);
        if (idx >= 0) {
            put(deckRef, idx, false);
            played = true;
            actedThisTurn = true;
        } else {
            drawOneThenTryPlay(deckRef, false);
            played = actedThisTurn;
        }

        if (deckRef.size() == 2) wincount[currentindex] = 1;

        if (played) resolveTopAction();
        updateCounts();
        // bot remains visible until its timer ends
    }

    /** Draw exactly one card; if that specific card is playable now, play it immediately. */
    private void drawOneThenTryPlay(java.util.List<Card> deckRef, boolean isHuman) {
        int before = deckRef.size();
        addCards(deckRef, 1, isHuman);         // draw 1 (creates a button if human)
        Card drawn = deckRef.get(before);      // the newly drawn card

        boolean needsColour = ("wild".equals(drawn.getAction()) || "change".equals(drawn.getAction()));

        if (isPlayable(drawn)) {
            if (isHuman && needsColour) {
                colourchoice = (String) JOptionPane.showInputDialog(
                        this, "Choose a Colour", "Colour Change",
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (colourchoice == null) { // cancelled -> keep the card
                    updateCounts();
                    return;
                }
            }
            put(deckRef, before, isHuman);
            resolveTopAction();
            updatestockpile();
            actedThisTurn = true;
        }
        updateCounts();
    }

    // ========================= Helpers =========================

    private String nameOf(int i) {
        if (i == 0) return Menu.getName + "'s";
        if (i == 1) return "Bot 1's 🤖";
        if (i == 2) return "Bot 2's 🤖";
        if (i == 3) return "Bot 3's 🤖";
        return "Player";
    }

    private static int mod4(int v) { int r = v % 4; return (r < 0) ? r + 4 : r; }
    private Card topOfStock() { return stockpile.get(0); }

    private boolean isActionCard(Card c) {
        String a = c.getAction();
        return "wild".equals(a) || "plus2".equals(a) || "skip".equals(a) || "change".equals(a) || "reverse".equals(a);
    }

    private void updatestockpile() { stockpilecard.setIcon(topOfStock().getImage()); }

    private void updateCounts() {
        bot1count.setText("# of Cards: " + bot1.size());
        bot2count.setText("# of Cards: " + bot2.size());
        bot3count.setText("# of Cards: " + bot3.size());
        humancount.setText("# of Cards: " + human.size());
    }

    private void enableHumanButtons(boolean enable) {
        for (Component c : bottomc.getComponents()) if (c instanceof JButton) c.setEnabled(enable);
        deck.setEnabled(enable);
        unob.setEnabled(enable && human.size() == 2);
    }

    private int findPlayableIndex(java.util.List<Card> deck) {
        for (int i = 0; i < deck.size(); i++) {
            if (isPlayable(deck.get(i))) return i;
        }
        return -1;
    }

    // ===== NEW helpers for restriction =====
    private boolean isPlus2OrWild(Card c) {
        String a = c.getAction();
        return "plus2".equals(a) || "wild".equals(a);
    }

    private int findPlus2OrWildPlayableIndex(java.util.List<Card> deck) {
        for (int i = 0; i < deck.size(); i++) {
            Card c = deck.get(i);
            if (isPlus2OrWild(c) && isPlayable(c)) return i;
        }
        return -1;
    }
    // =====================================

    private void put(java.util.List<Card> pdeck, int index, boolean isHuman) {
        Card putcard = pdeck.get(index);
        stockpile.add(0, putcard);
        pdeck.remove(index);
        updatestockpile();

        if (isHuman) {
            for (int i = 0; i < bottomc.getComponentCount(); i++) {
                Component comp = bottomc.getComponent(i);
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    Object tag = btn.getClientProperty("cardRef");
                    if (tag == putcard) { bottomc.remove(i); break; }
                }
            }
            bottomc.revalidate();
            bottomc.repaint();
        }
    }

    /** Deal/add cards. If human, also add buttons bound to the Card object. */
    private void addCards(java.util.List<Card> deck, int amt, boolean humanHand) {
        // Safety guards
        if (amt < 0) amt = 0;
        if (amt > 100) amt = 100;
        if (deck.size() > 1000) {
            System.err.println("Guard: deck too big (" + deck.size() + "), skipping addCards.");
            return;
        }

        for (int c = 0; c < amt; c++) {
            Card card = randomCardForDeck(deck);
            deck.add(card);

            if (humanHand) {
                JButton btn = new JButton(card.getImage());
                btn.putClientProperty("cardRef", card);
                btn.addActionListener(ev -> {
                    if (currentindex != 0) return;     // not your turn
                    if (!isPlayable(card)) return;      // not playable now

                    // Force color choice for human when playing wild/change
                    if ("wild".equals(card.getAction()) || "change".equals(card.getAction())) {
                        colourchoice = (String) JOptionPane.showInputDialog(
                                this, "Choose a Colour", "Colour Change",
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (colourchoice == null) return; // cancelled
                    }

                    int idx = human.indexOf(card);
                    if (idx >= 0) {
                        put(human, idx, true);
                        resolveTopAction();
                        updateCounts();
                        endTurn(false);
                    }
                });
                bottomc.add(btn);
            }
        }
        if (humanHand) { bottomc.revalidate(); bottomc.repaint(); }
    }

    /** Random card (not added). */
    private Card randomCard() {
        String colour;
        String action;
        int num = 0;

        int ranC = (int)(Math.random()*4) + 1;
        if      (ranC == 1) colour = "red";
        else if (ranC == 2) colour = "yellow";
        else if (ranC == 3) colour = "green";
        else                colour = "blue";

        int ranA = (int)(Math.random()*6) + 1;
        if      (ranA == 1) { action = "regular"; num = (int)(Math.random()*10); }
        else if (ranA == 2) { action = "skip"; }
        else if (ranA == 3) { action = "reverse"; }
        else if (ranA == 4) { action = "plus2"; }
        else if (ranA == 5) { action = "wild";   colour = "wild"; }
        else                { action = "change"; colour = "wild"; }

        if (ranA == 2 || ranA == 3 || ranA == 4) num = 0;
        return new Card(colour, num, action, 0);
    }

    /** Random card, index aligned to destination deck size. */
    private Card randomCardForDeck(java.util.List<Card> deck) {
        Card c = randomCard();
        return new Card(c.getColour(), c.getNum(), c.getAction(), deck.size());
    }

    /** Playability rules. */
    /** Playability rules.
 * - WILD/CHANGE: always playable
 * - ACTION (+2 / reverse / skip): playable if SAME ACTION TYPE as top OR SAME COLOR
 * - NUMBER (0..9): playable if SAME COLOR or (when top is a number) SAME NUMBER
 */
    private boolean isPlayable(Card play) {
        Card top = topOfStock();

        String pa = play.getAction();
        String ta = top.getAction();

        boolean playIsNumber = "regular".equals(pa) && play.getNum() >= 0 && play.getNum() <= 9;
        boolean topIsNumber  = "regular".equals(ta) && top.getNum()  >= 0 && top.getNum()  <= 9;

        // WILD/CHANGE — always allowed
        if ("wild".equals(pa) || "change".equals(pa)) return true;

        // ACTIONS: allow by SAME ACTION TYPE (color not required) OR by SAME COLOR
        if ("plus2".equals(pa) || "reverse".equals(pa) || "skip".equals(pa)) {
            // same action type?
            if (pa.equals(ta)) return true;
            // or same color?
            return play.getColour().equals(top.getColour());
        }

        // NUMBER card: match color OR (if top is a number) match number
        if (playIsNumber) {
            if (play.getColour().equals(top.getColour())) return true;
            return topIsNumber && (play.getNum() == top.getNum());
        }

        // anything else
        return false;
    }


    /** Apply skip/reverse/plus2/wild/change effects from the top card (just played). */
    private void resolveTopAction() {
        Card top = topOfStock();
        String a = top.getAction();

        if ("skip".equals(a)) {
            skipNext = 1;

        } else if ("reverse".equals(a)) {
            toggleDirectionAndArrows();

        } else if ("plus2".equals(a)) {
            giveToNext(2);
            // Restrict the NEXT bot only (do not skip human / no extra skip rules)
            int next = mod4(currentindex + direction);
            restrictNextBotToPlusOrWild = (next != 0);

        } else if ("wild".equals(a)) {
            // choose colour (bots auto-pick); PUSH a colored regular card to keep stockpile icon valid
            if (currentindex > 0) colourchoice = getNewCB();
            if (colourchoice == null) colourchoice = "red";
            setNewC(colourchoice, "regular", 34); // uses your existing assets
            giveToNext(4);
            // Restrict the NEXT bot only
            int next = mod4(currentindex + direction);
            restrictNextBotToPlusOrWild = (next != 0);

        } else if ("change".equals(a)) {
            // choose colour (bots auto-pick); PUSH a colored regular card
            if (currentindex > 0) colourchoice = getNewCB();
            if (colourchoice == null) colourchoice = "red";
            setNewC(colourchoice, "regular", 12);
        }

        updatestockpile();
    }

    /** Push a colored placeholder to the top (so an existing image is used). */
    private void setNewC(String colour, String action, int num) {
        Card colourc = new Card(colour, num, action, stockpile.size());
        stockpile.add(0, colourc);
    }

    private void giveToNext(int num) {
        int next = mod4(currentindex + direction);
        addCards(turn.get(next), num, next == 0);
        updateCounts();
    }

    /** Reverse: invert direction + swap arrow visibility + vertical flip icons (only on reverse). */
    private void toggleDirectionAndArrows() {
        direction = -direction;

        boolean clockwiseNow = (direction == +1);
        clockwise1.setVisible(clockwiseNow);
        clockwise2.setVisible(clockwiseNow);
        counterwise1.setVisible(!clockwiseNow);
        counterwise2.setVisible(!clockwiseNow);

        // flip the images vertically to create a “flip” effect ONLY on reverse
        arrowsFlipped = !arrowsFlipped;
        if (arrowsFlipped) {
            clockwise1.setIcon(flipClock1);
            clockwise2.setIcon(flipClock2);
            counterwise1.setIcon(flipCounter1);
            counterwise2.setIcon(flipCounter2);
        } else {
            clockwise1.setIcon(icoClock1);
            clockwise2.setIcon(icoClock2);
            counterwise1.setIcon(icoCounter1);
            counterwise2.setIcon(icoCounter2);
        }

        center.revalidate();
        center.repaint();
    }

    // === arrow vertical flip ===
    private ImageIcon verticalFlip(ImageIcon src) {
        Image img = src.getImage();
        int w = img.getWidth(null), h = img.getHeight(null);
        if (w <= 0 || h <= 0) return src;

        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -h);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        op.filter(bi, flipped);

        return new ImageIcon(flipped);
    }

    private void applyUnoPenaltyIfNeeded(int playerIdx) {
        java.util.List<Card> deck = turn.get(playerIdx);
        if (deck.size() == 1 && wincount[playerIdx] == 0) {
            addCards(deck, 2, playerIdx == 0);
        }
        if (deck.size() != 2) wincount[playerIdx] = 0;
    }

    public static String getNewCB() {
        int choice = (int)(Math.random()*4) + 1;
        if (choice == 1) return "red";
        if (choice == 2) return "blue";
        if (choice == 3) return "yellow";
        return "green";
    }

    public static void resetAll() {
        Arrays.fill(wincount, 0);
        skipNext = 0;
        direction = +1;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            repaint();
            requestFocusInWindow();
            try { Thread.sleep(100); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
