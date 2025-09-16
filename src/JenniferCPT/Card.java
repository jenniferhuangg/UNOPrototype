package JenniferCPT;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

/* UNO Card model */
public class Card {
    private String colour;  // "red","blue","yellow","green","wild"
    private final int num;  // 0..9 (0 for actions)
    private final String action;  // "regular","skip","reverse","plus2","wild","change"
    private final int index;      // creation index (not part of equality)

    public Card(String c, int n, String a, int i) {
        this.colour = c; this.num = n; this.action = a; this.index = i;
    }

    public ImageIcon getImage() {
        String name = colour + num + action + ".jpg"; // e.g., red5regular.jpg
        ImageIcon src = new ImageIcon(name);
        Image scaled = src.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // --- getters ---
    public String getColour() { return colour; }
    public int getNum() { return num; }
    public String getAction() { return action; }
    public int getIndex() { return index; }

    // --- setter (needed so wild/change can recolor the same card) ---
    public void setColour(String newColour) { this.colour = newColour; }

    // helpers used by your game logic
    public boolean equalsC(Card o) { return o != null && Objects.equals(colour, o.colour); }
    public boolean equalsN(Card o) { return o != null && num == o.num; }
    public boolean equalsA(Card o) { return o != null && Objects.equals(action, o.action); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return num == other.num &&
               Objects.equals(colour, other.colour) &&
               Objects.equals(action, other.action);
    }
    @Override public int hashCode() { return Objects.hash(colour, num, action); }
    @Override public String toString() {
        return "Colour: " + colour + " Number: " + num + " Action: " + action;
    }
}
