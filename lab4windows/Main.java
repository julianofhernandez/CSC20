// import RandomWalker;

public class Main {
	public static void main(String[] args) {
		RandomWalker walker = new RandomWalker();
		
		if (walker.getX() != 0) {
			System.out.println("X coordinate didn't initialize properly");
		} else {
			System.out.println("X coordinate initialized properly");
		}
		if (walker.getY() != 0) {
			System.out.println("Y coordinate didn't initialize properly");
		} else {
			System.out.println("Y coordinate initialized properly");
		}
		
		// This will run the program 20 times, ensure that x and y values can move both up and down
		int lastX = walker.getX();
		int lastY = walker.getY();
		
		for (int i = 0; i < 20; i++) {
			walker.move();			
			if (walker.getX() > lastX) {
				System.out.println("Walker can move left");
			}
			if (walker.getX() < lastX) {
				System.out.println("Walker can move right");
			}
			if (walker.getY() > lastY) {
				System.out.println("Walker can move up");
			}
			if (walker.getY() < lastY) {
				System.out.println("Walker can move down");
			}
			lastX = walker.getX();
			lastY = walker.getY();
		}
		System.out.println("Steps taken: " + walker.getSteps());
		if (walker.getSteps() != 20) {
			System.out.println("Steps isn't counting correctly");
		} else {
			System.out.println("Steps are conting correctly");
		}
	}
}