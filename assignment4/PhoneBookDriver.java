import java.util.*;
import java.io.*;

/**
* The driving class for this program
* 
* @author Julian Hernandez
* @version v1.0
*/
public class PhoneBookDriver {
    /**
    This function is the main driver of the phone book
    @throws FileNotFoundException this happens because we are opening a file
    @param args This isn't required for this program */
    public static void main(String[] args) throws FileNotFoundException {
        String dataFile = "ContactDetails.txt";
        // a.    Create the PhoneBook.
        PhoneBook phoneBook = new PhoneBook();

        // b.    Read data from file ContactDetails.txt. Each line has new contact info.
        File fileObj = new File(dataFile);
        Scanner reader = new Scanner(fileObj);
        String inline; 
        String firstName; String lastName; long homeNumber; long officeNumber; String emailAddress;
        while (reader.hasNextLine()) {
            try {
                inline = reader.nextLine();
                String tokens[] = inline.split(",");
                firstName = tokens[0];

                lastName = tokens[1];

                if (tokens[2].equals("-")) {
                    homeNumber = 0;
                } else {
                    homeNumber = Long.parseLong(tokens[2]);
                }

                if (tokens[3].equals("-")) {
                    officeNumber = 0;
                } else {
                    officeNumber = Long.parseLong(tokens[3]);
                }

                emailAddress = tokens[4];

                // Create Contact objects for the above details and add them to the phonebook array list. It there is a “-” at the home or office number set it to 0.
                Contact newContact = new Contact(firstName, lastName, homeNumber, officeNumber, emailAddress);
                phoneBook.add(newContact);
            } catch (Exception E) {
                E.printStackTrace();
                println("Could not parse contact");
            }
        }
        phoneBookMenu(phoneBook);
    }

    /** This holds the logic for the main menu
    @param phoneBook This is the phonbook to manipulate */
    public static void phoneBookMenu(PhoneBook phoneBook) {
        boolean quit = false;
        Scanner input = new Scanner(System.in);
        while (!quit) {
            println("\nPhone Book Menu Options");
            // A.    Print Phonebook
            println("A.    Print Phonebook");
            // B.    Sort phone book by first name // use bubble sort
            println("B.    Sort phone book by first name");
            // C.    Sort phone book by last name // use selection sort
            println("C.    Sort phone book by last name");
            // D.    Search contact by last name  // use binary search
            println("D.    Search contact by last name");
            // Q.  Quit
            println("Q. to Quit");
            String option = input.nextLine().toLowerCase();
            switch(option) {
                case "a":
                    print(phoneBook.toString());
                    break;
                case "b":
                    phoneBook.bubbleSort();
                    break;
                case "c":
                    phoneBook.selectionSort();
                    break;
                case "d":
                    print("Enter the last name to search: ");
                    boolean found = phoneBook.binarySearch(input());
                    if (found) {
                        println("Contact found");
                    } else {
                        println("Contact not found");
                    }
                    break;
                case "q":
                    println("Good Bye!");
                    quit = true;
                    return;
                default:
                    println("Invalid option, try again.");
            }
        }
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


                    
// d.    Print Phonebook - print() - Write a static function to print Phone book using the toString method of the Phonebook class

// e.    Search contact by last name  - search(String lastName) : Write this static function – it prompts user for last name and search the phone book using binary search. If contact found print its details. If contact not found print “Contact Not Found”.

// f.    Run all the menu options and print the results. 
// Note : all the functions should be carried out using the given sorting or searching algorithms only.
