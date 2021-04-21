import java.util.*;

/**
* Compare will compare two string inputed by the user
* @author Julian Hernandez
* @version v1.0
*/
public class Compare {
	/** 
	* @param args Not needed for this program
	*/
	public static void main(String[] args) {
		boolean result = compare(getInput("one"), getInput("two"));
		if (result) {
			System.out.println("Same");
		} else {
			System.out.println("Not Same");
		}
	}

	/** 
	* @param number The number of the string to input
	* @return the string that was typed by the user
	*/
	public static String getInput(String number) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter you string #" + number + ": ");
		String input = in.nextLine();
		return input;
	}

	/** 
	* @param one The first string to compare
	* @param two the second string to compare
	* @return Whether the strings match
	*/
	public static boolean compare(String one, String two) {
		if (one.length() == 0 && two.length() == 0) {
			return true;
		} else if (one.length() == 0 || two.length() == 0) {
			return false;
		} else {}
		char beginOne = one.charAt(0);
		char beginTwo = two.charAt(0);
		String endOne = one.substring(1);
		String endTwo = two.substring(1);
		if (beginOne == beginTwo && one.length() == two.length()) {
			if (one.length() > 1) {
				return compare(endOne, endTwo);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
