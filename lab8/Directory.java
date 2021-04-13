import java.util.*;
import java.io.*;
/**
* Directory stores names and phone numbers
* 
* @author Julian Hernandez
* @version v1.0
*/
public class Directory {
    HashMap<String, String> directory = new HashMap<String, String>();
    
    /** This instatiates the class with the values in the file names
    @param names the file to search through
    @throws FileNotFoundException It opens a file*/
    public Directory(String names) throws FileNotFoundException {
        File fileName = new File(names);
        Scanner reader =  new Scanner(fileName);

        while (reader.hasNext()) {
            String line = reader.nextLine();
            String tokens[] = line.split(":");
            directory.put(tokens[0], tokens[1]);
        }
    }

    /** number look up given name
    @param name the name to lookup the number for
    @return "" if not found other the value found*/
    public String getPhoneNumber(String name) {
        String returnValue = directory.get(name);
        if (returnValue == null) {
            returnValue = "";
        }
        return returnValue;
    }

    /** remove a directory entry
    @param name the name to find
    */
    public void delete(String name) {
        directory.remove(name);
    }

    /** add friend given number
    @param name the name to add
    @param number the number to correlate to the name
    */
    public void add(String name, String number) {
        directory.put(name, number);
    }

    /** 
    @param name the name to change
    @param number the number to change to*/
    public void change(String name, String number) {
        directory.remove(name);
        directory.put(name, number);
    }

    /** Displays the directory hashmap */
    public void displayDirectory() {
        for (String name : directory.keySet()) {
            String number = directory.get(name);
            System.out.println(name + "\t" + number);

        }
    }
}