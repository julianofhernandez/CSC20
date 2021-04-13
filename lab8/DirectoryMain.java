import java.io.FileNotFoundException;
import java.util.*;
class DirectoryMain{
  public static void main(String[] args) throws FileNotFoundException{
    Directory phoneDirectory = new Directory("names.txt"); //create the directory
    System.out.println();
    phoneDirectory.displayDirectory(); //display all names
    System.out.println();
    phoneDirectory.add("Dwight Cheeseboro","916-423-4324"); //add Dwight
    phoneDirectory.change("Dwight Cheeseboro","924-335-6757"); //change his number
    System.out.println();
    phoneDirectory.displayDirectory(); //display all current names
    System.out.println();
    phoneDirectory.delete("Rita Hayworth"); //remove Rita
    System.out.print("Rita Hayworth's number:" ); //check if Rita is still there
    if (phoneDirectory.getPhoneNumber("Rita Hayworth").equals(""))
      System.out.println(" Not Found");
    else 
      System.out.println("Rita Hayworth's number:    "             +phoneDirectory.getPhoneNumber("Rita Hayworth"));
    System.out.println();
    phoneDirectory.displayDirectory();  //display all current names
    System.out.println();
    //look up Johns number
    System.out.println("John Adam's number:    " +phoneDirectory.getPhoneNumber("John Adams"));
    //look up a non existing Amal's number to verify she is not a friend
    System.out.print("Amal Rona's number:    " );
    if (phoneDirectory.getPhoneNumber("Amal Rona").equals(""))
      System.out.println(" Not Found");
    else 
      System.out.println(phoneDirectory.getPhoneNumber("Amal Rona")); 
  }
}