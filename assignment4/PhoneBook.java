import java.util.*;

/**
* Holds the logic for the phone book
* 
* @author Julian Hernandez
* @version v1.0
*/
public class PhoneBook {
    /** The list of contacts */
    public ArrayList<Contact> contacts;
    /** The constructor for the phonebook */
    public PhoneBook(){
        this.contacts = new ArrayList<Contact>();
    }
    
    /** Adds a contact to the phonebook
    @param c contact to add to the list */
    public void add(Contact c){
        this.contacts.add(c);
    }

    /** Returns a printable string with the entire contact list
    @return A printable list of all contacts */
    public String toString(){
        String returnString = "";
        returnString += String.format("%-10s%-15s%-15s%-20s%s\n", 
                            "firstName", "lastName", "homeNumber", "officeNumber", "emailAddress");
        for (int i  = 0; i< contacts.size(); i++) {
            returnString += contacts.get(i);
        }
        return returnString;
    }

    /** Sort phonebook by first name */
    public void bubbleSort(){
        int n = this.contacts.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (this.contacts.get(j).getFirstName().compareTo(this.contacts.get(j+1).getFirstName()) > 0)
                {
                    // swap arr[j+1] and arr[j]
                    Contact temp = this.contacts.get(j);
                    this.contacts.set(j, this.contacts.get(j+1));
                    this.contacts.set(j+1, temp);
                }
            }
        }
    }

    /** Sorts the list of last name */
    public void selectionSort(){
        int n = this.contacts.size();
  
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min = i;
            for (int j = i+1; j < n; j++)
                if (this.contacts.get(j).getLastName().compareTo(this.contacts.get(min).getLastName()) < 0) {
                    min = j;
                }
  
            // Swap the found minimum element with the first
            // element
            Contact temp = this.contacts.get(min);
            this.contacts.set(min, this.contacts.get(i));
            this.contacts.set(i, temp);
        }
    }

    /** Sort phone book by last using selection sort algorithm. 
    * @param name the name to search for
    * @return true if the contact is found, false otherwise
    */
    public boolean binarySearch(String name){
        this.selectionSort();
        // Search contact by last name using binary search. 
        // return true if contact is found else false
        int lowIndex = 0;
        int highIndex = this.contacts.size()-1;
        int middleIndex = (lowIndex + highIndex)/2;

        while (lowIndex <= highIndex) {
            middleIndex = (lowIndex + highIndex)/2;
            if(this.contacts.get(middleIndex).getLastName().equals(name)) {
                return true;
            // if middle is less than name
            } else if (this.contacts.get(middleIndex).getLastName().compareTo(name) < 0) {
                lowIndex = middleIndex + 1;
            // if middle is greater than name
            } else if (this.contacts.get(middleIndex).getLastName().compareTo(name) > 0) {
                highIndex = middleIndex - 1;
            }
        }
        // We reach here when element is not present
        // in array
        return false;
    }

}
