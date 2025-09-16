package JenniferCPT;
/* Name: Jennifer Huang
 * Date: Jan 18th, 2023
 * Purpose: To store all needed Images for UNO game to access through a method
 */
import java.awt.Image;
import javax.swing.ImageIcon;

public class Images {
// Images to act as backgrounds for other CardLayout JPanels
public static Image unomain = loadImage("unomain.png", Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
public static Image instructions = loadImage("unoinstructions.png", Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
public static Image unogame = loadImage("unogame.png", Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
public static Image unowin = loadImage ("unowin", Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
public static Image unolose = loadImage ("unolose", Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
public static Image unobutton = loadImage("unobutton.png", 50, 50); // logo of uno for the image of the UNO JButton

public static Image unostack = loadImage("unostack.png", 100, 100); // to put Image of a stack of UNO cards on deck JButton
public static Image unosound = loadImage("unosound.png",20,40); // sound icon image for the music JButton

// arrows to show direction of the game
public static Image clock1 = loadImage("reverseclock1.png", 100,200);
public static Image clock2 = loadImage("reverseclock2.png", 100,200);
public static Image counter1 = loadImage("reversecounter1.png", 100,200);
public static Image counter2 = loadImage("reversecounter2.png", 100,200);

// no colour chosen colour change and wild cards
public static Image wild0change = loadImage("wild0change.png", 10, 30);
public static Image wild0wild = loadImage("wild0wild.png", 10, 30);

public static Image unodeck = loadImage ("unodeck.png",200,100); // image of multiple UNO cards shown in a deck

// UNO card images
//12 for number means colour change card, 34 for number means wild card
public static Image blue34regular = loadImage("blue34regular.png", 400, 200);
public static Image blue0plus2 = loadImage("blue0plus2.png", 400, 300);
public static Image blue0regular = loadImage("blue0regular.png", 400, 300);
public static Image blue0reverse = loadImage("blue0reverse.png", 400, 300);
public static Image blue0skip = loadImage("blue0skip.png", 400, 300);
public static Image blue12regular = loadImage("blue12regular.png", 400, 300); 
public static Image blue1regular = loadImage("blue1regular.png", 400, 300);
public static Image blue2regular = loadImage("blue2regular.png", 400, 300);
public static Image blue3regular = loadImage("blue3regular.png", 400, 300);
public static Image blue4regular = loadImage("blue4regular.png", 400, 300);
public static Image blue5regular = loadImage("blue5regular.png", 400, 300);
public static Image blue6regular = loadImage("blue6regular.png", 400, 300);
public static Image blue7regular = loadImage("blue7regular.png", 400, 300);
public static Image blue8regular = loadImage("blue8regular.png", 400, 300);
public static Image blue9regular = loadImage("blue9regular.png", 400, 300);

public static Image green12regular = loadImage("green12regular.png", 400, 300);
public static Image green0plus2 = loadImage("green0plus2.png", 400, 300);
public static Image green0regular = loadImage("green0regular.png", 400, 300);
public static Image green0reverse = loadImage("green0reverse.png", 400, 300);
public static Image green0skip = loadImage("green0skip.png", 400, 300);
public static Image green34regular = loadImage("green34regular.png", 400, 300);
public static Image green1regular = loadImage("green1regular.png", 400, 300);
public static Image green2regular = loadImage("green2regular.png", 400, 300);
public static Image green3regular = loadImage("green3regular.png", 400, 300);
public static Image green4regular = loadImage("green4regular.png", 400, 300);
public static Image green5regular = loadImage("green5regular.png", 400, 300);
public static Image green6regular = loadImage("green6regular.png", 400, 300);
public static Image green7regular = loadImage("green7regular.png", 400, 300);
public static Image green8regular = loadImage("green8regular.png", 400, 300);
public static Image green9regular = loadImage("green9regular.png", 400, 300);

public static Image red12regular = loadImage("red12regular.png", 400, 300);
public static Image red34regular = loadImage("red34regular.png", 400, 300);
public static Image red0plus2 = loadImage("red0plus2.png", 400, 300);
public static Image red0reverse = loadImage("red0reverse.png", 400, 300);
public static Image red0skip = loadImage("red0skip.png", 400, 300);
public static Image red0regular = loadImage("red0regular.png", 400, 300);
public static Image red1regular = loadImage("red1regular.png", 400, 300);
public static Image red2regular = loadImage("red2regular.png", 400, 300);
public static Image red3regular = loadImage("red3regular.png", 400, 300);
public static Image red4regular = loadImage("red4regular.png", 400, 300);
public static Image red5regular = loadImage("red5regular.png", 400, 300);
public static Image red6regular = loadImage("red6regular.png", 400, 300);
public static Image red7regular = loadImage("red7regular.png", 400, 300);
public static Image red8regular = loadImage("red8regular.png", 400, 300);
public static Image red9regular = loadImage("red9regular.png", 400, 300);

public static Image yellow12regular = loadImage("yellow12regular.png", 400, 300);
public static Image yellow0plus2 = loadImage("yellow0plus2.png", 400, 300);
public static Image yellow0regular = loadImage("yellow0regular.png", 400, 300);
public static Image yellow0reverse = loadImage("yellow0reverse.png", 400, 300);
public static Image yellow0skip = loadImage("yellow0skip.png", 400, 300);
public static Image yellow34regular = loadImage("yellow34regular.png", 400, 300);
public static Image yellow1regular = loadImage("yellow1regular.png", 400, 300);
public static Image yellow2regular = loadImage("yellow2regular.png", 400, 300);
public static Image yellow3regular = loadImage("yellow3regular.png", 400, 300);
public static Image yellow4regular = loadImage("yellow4regular.png", 400, 300);
public static Image yellow5regular = loadImage("yellow5regular.png", 400, 300);
public static Image yellow6regular = loadImage("yellow6regular.png", 400, 300);
public static Image yellow7regular = loadImage("yellow7regular.png", 400, 300);
public static Image yellow8regular = loadImage("yellow8regular.png", 400, 300);
public static Image yellow9regular = loadImage("yellow9regular.png", 400, 300);

/* Purpose: To convert images from files into ImageIcons
 * Pre: One String to get the name of the picture in files, two ints to get the width and height for the pictures
 * Post: returns 1 ImageIcon, representing the chosen Image
 */
public static Image loadImage(String name, int width, int height) {
		return new ImageIcon(name).getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); 
	}
}
