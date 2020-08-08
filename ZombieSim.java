package cs2113.zombies;

import cs2113.util.DotPanel;

import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.*;

/** You must add a way to represent humans.  When there is not a zombie apocalypse occurring, humans
 * should follow these simple rules:
 * 		if (1 in 10 chance):
 * 			turn to face a random direction (up/down/left/right)
 * 		Move in the current direction one space if not blocked by a wall
 * We will add additional rules for dealing with sighting or running into zombies later. */

public class ZombieSim extends JFrame {

	private static final long serialVersionUID = -5176170979783243427L;

	/** The Dot Panel object you will draw to! */

	protected static DotPanel dp;

	/** Define constants using static final variables. */

	public static final int MAX_X = 200;
	public static final int MAX_Y = 200;
	private static final int DOT_SIZE = 4;
	private static final int NUM_HUMANS = 200;
	private static final int NUM_BUILDINGS = 80;
	public City city;

	/** This fills the frame with a "DotPanel", a type of drawing canvas that
	 * allows you to easily draw squares to the screen. */

	public ZombieSim() {
		this.setSize(MAX_X * DOT_SIZE, MAX_Y * DOT_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Braaiinnnnnsss");

		//Create and set the size of the panel.
		dp = new DotPanel(MAX_X, MAX_Y, DOT_SIZE);

		//Add the panel to the frame.
		Container cPane = this.getContentPane();
		cPane.add(dp);

		/** Initialize the DotPanel canvas:
		 * You CANNOT draw to the panel BEFORE this code is called.
		 * You CANNOT add new widgets to the frame AFTER this is called. */

		this.pack();
		dp.init();
		dp.clear();
		dp.setPenColor(Color.red);
		this.setVisible(true);

		// Create our city.
		final City[] city = {new City(MAX_X, MAX_Y, NUM_BUILDINGS, NUM_HUMANS)};

		/** GUI for Mouse */
		MouseListener listener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// when mouse is clicked, check location
				if (city[0].checkDropDimensions(e.getX()/DOT_SIZE, e.getY()/DOT_SIZE)) {
					city[0].zombieDrop(e.getX()/DOT_SIZE, e.getY()/DOT_SIZE);
				} else {
					System.out.println("Error: Zombies cannot be drawn inside walls!");
				}
			}
			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }
		};
		this.addMouseListener(listener);

		/** Space Bar */
		KeyListener listener1 = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// when space bar is clicked, clear the city
					city[0] = new City(MAX_X, MAX_Y, NUM_BUILDINGS, NUM_HUMANS);
				}
			}
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) { }
		};
		this.addKeyListener(listener1);


		/** This is the Run Loop (aka "simulation loop" or "game loop")
		 * It will loop forever, first updating the state of the city
		 * (e.g., having humans take a single step) and then it will
		 * draw the newly updated simulation. Since we don't want
		 * the simulation to run too fast for us to see, it will sleep
		 * after repainting the screen. Currently it sleeps for
		 * 33 milliseconds, so the program will update at about 30 frames
		 * per second. */

		while (true) {
			// Run update rules for city
            // and everything in it
			city[0].update();
			// Draw to screen
            // and then refresh
			city[0].draw();
			dp.repaintAndSleep(33);

			// check if the humans are null
			if (city[0] != null || city[0].humans != null) {
				if (city[0].humans.size() <= 0)
					// we assume the game is over
					// and effectively it is a reset
					city[0] = new City(MAX_X, MAX_Y, NUM_BUILDINGS, NUM_HUMANS);
				}
			}
		}

	public static void main(String[] args) {
		// Create a new GUI window
		new ZombieSim();
	}
}