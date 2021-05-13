import java.util.*;

/**
* The driving class for running and testing this program
* 
* @author Julian Hernandez
* @version v1.0
*/
public class SmartStringListDriver {
    /**
    This function is the main driver of the phone book
    @param args This isn't required for this program */
    public static void main(String[] args) {
        SmartStringList list = new SmartStringList(5);
        list.add("banana");
        list.add("carrot");
        list.add("dandelion");
        list.add("eggplant");
        list.add("asparagus");
        // Testing indexOf that worked
        println(Integer.toString(list.indexOf("Asparagus")));
        // Testing indexOf that doesn't work
        println(Integer.toString(list.indexOf("fungi")));
        println("sorting...");
        list.sort();
        // Testing sorted order
        println(Integer.toString(list.indexOf("asparagus")));
        println(Integer.toString(list.indexOf("banana")));
        println(Integer.toString(list.indexOf("carrot")));
        println(Integer.toString(list.indexOf("dandelion")));
        println(Integer.toString(list.indexOf("eggplant")));
        println(list.get(0));
        println(list.get(1));
        println(list.get(2));
        println(list.get(3));
        println(list.get(4));
    }

    /**This is just to simplify the print process 
     * @param input String to print
    */
    public static void print(String input) {
        System.out.print(input);
    }

    /**This is just to simplify the println process 
     * @param input String to print
    */
    public static void println(String input) {
        System.out.println(input);
    }

    /**This is just to simplify the println process
    */
    public static void println() {
        System.out.println("");
    }

    /**This is just to simplify the input process
    * @return the string input
    */
	public static String input() {
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}
}