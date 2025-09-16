# UNO — Java Prototype  

A classic UNO card game implemented in **Java**.  
Play against three bots with turn timers, action cards, and UNO button logic — all in a clean, responsive desktop UI.  

---

## ✨ Features  

- **4 Players**: Human player vs. **Bot 1**, **Bot 2**, **Bot 3**  
- **Turn Timers**  
  - Human: configurable time limit (default: 20s)  
  - Bots: 10s visible “thinking” window so you can watch their move  
- **Card Rules**  
  - **Number cards (0–9):** Match either **color** or **number**  
  - **Action cards (+2, Reverse, Skip):** Must match **color**  
  - **Wild / Color Change cards:** Always playable; pick a new color (bots auto-choose)  
- **Draw Logic**  
  - If no playable card → draw exactly **1**  
  - If the drawn card is playable → auto-play it  
- **UNO Button**: Lights up when the player has 2 cards left  
- **Direction Arrows**: Flip only when a **Reverse** card is played  
- **Centered Layout**: Player hand and center stockpile stay centered  
- **Win / Lose Screens** with restart option  

---
## ▶️ Running the Game  

### Prerequisites  
- Java 8 or later (JDK)  

### Compile & Run (Clone git environment first)

```bash
# Navigate into the src folder
cd src

# Compile all source files
javac JenniferCPT/*.java

# Run the program
java JenniferCPT.Main


