import java.util.*;
import java.io.*;
/**
* The driving class for this program
* 
* @author Julian Hernandez
* @version v1.0
*/
public class TrainHelper {
    /** The menu logic
    * @throws FileNotFoundException this is because it opens a file */
	public static void trainMenu() throws FileNotFoundException {
        Train train = new Train("car.txt");
		Scanner input = new Scanner(System.in);
		boolean quit = false;
        String factory; int stop; String material;
		while (!quit) {
		    println("Train Depot Menu Options");
            println("Enter A to Attach a train car");
            println("Enter R to Detach a car at factory");
            println("Enter D to Display all the train cars");
            println("Enter S to Search the train carrs");
            println("Enter M to Merge two train cars");
            println("Enter Q to Quit");
			String option = input.nextLine().toLowerCase();
			switch(option) {
				case "a":
                    println("Please enter the Factory name");
                    factory = input();
                    println("Please enter the Stop Number");
                    stop= inputInt();
                    println("Please enter the Material Name");
                    material= input();
					train.attach(factory, stop, material);
                    println("Adding a new car - %s %f %s".format(factory, stop, material));
					break;
				case "r":
                    println("Please enter the Factory name");
                    factory = input();
                    train.detach(factory);
                    println("The car is detached at " + factory);
					break;
				case "d":
					train.displayTrainCars();
					break;
				case "s":
                    println("Please enter the Factory name");
                    factory = input();
                    train.search(factory);
					break;
				case "m":
					train.merge("update.txt");
					break;
				case "q":
					quit = true;
					return;
				default:
					println("Invalid option, try again.");
			}
		}
	}

    /**This is just to simplify the println process 
    */
	public static void println() {
		System.out.println("");
	}

    /**This is just to simplify the println process 
     * @param input String to print
    */
	public static void println(String input) {
		System.out.println(input);
	}

    /**This is just to simplify the input process
    * @return the string input
    */
	public static String input() {
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}

    /**This is just to simplify the input process with integer conversion
    * @return the integer input
    */
	public static int inputInt() {
		while (true) {
			try {
				String rawInput = input();
				int intInput = Integer.parseInt(rawInput);
				return intInput;
			} catch (Exception e) {
				println("That's not an integer, try again.");
				continue;
			}
		}
	}
}
