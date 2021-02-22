import java.util.*;

/**
* RandomWalker is a walker class that stores it's location and can move around the room
* 
* @author Julian Hernandez
* 
*/
public class RandomWalker {
	private int x;
	private int y;
	private int steps;
	
	/**
	* <p>The constructor sets all starting values to 0
	* </p>
	*/
	public RandomWalker() {
		this.x = 0;
		this.y = 0;
		this.steps = 0;
	}
	
	/**
	* <p>Instructs this random walker to update its coordinates by randomly making one of the 4 possible moves (up, down, left, or right).
	* </p>
	*/
	public void move() {
		Random rand = new Random();
		int choice = rand.nextInt(4);
		if (choice == 0) {
			this.x += 1;
		} else if (choice == 1) {
			this.x -= 1;
		} else if (choice == 2) {
			this.y += 1;
		} else if (choice == 3) {
			this.y -= 1;
		} else {
			throw new IllegalArgumentException();
		}
		this.steps += 1;
	}
	
	/**
	* <p>Returns this random walker's current x-coordinate.
	* </p>
	* @return the walk's current x-coordinate
	*/
	public int getX() {
		return this.x;
	}

	/**
	* <p>Returns this random walker's current y-coordinate.
	* </p>
	* @return the walk's current y-coordinate
	*/
	public int getY() {
		return this.y;
	}

	/**
	* <p>Returns the total steps taken
	* </p>
	* @return the total steps this walker has taken
	*/
	public int getSteps() {
		return this.steps;
	}
}