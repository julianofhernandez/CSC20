//***********************************************
//author: Julian Hernandez
//date: 2/14/2021
//title: FilterSort
//description: Lab 2
//*************************************************
import java.util.*;
import java.io.*;

// The entry for our program
class Main {
  public static void main(String[] args) throws FileNotFoundException {
    Scanner file = new Scanner(new File("data.txt"));
    if (!file.hasNext()) {
      System.out.println("This file is empty");
    }
    int[] data = new int[8];
    int count = 0;
    while(file.hasNext()) {
      try {
        String potentialInteger = file.next();
        int foundInteger = Integer.parseInt(potentialInteger);
        data[count] = foundInteger;
        count++;
      } catch (NumberFormatException e) {
        continue;
      } catch (ArrayIndexOutOfBoundsException e) {
        data = doubleArrayAndCopy(data);
      }
    }
    Arrays.sort(data);
    System.out.println(Arrays.toString(data));
  }

  // creates a new array double the size and puts the original data into it
  public static int[] doubleArrayAndCopy(int[] arr) {
    int length = arr.length;
    int[] returnArr = new int[length*2];
    for(int i = 0; i < length; i++) {
      returnArr[i] = arr[i];
    }
    return returnArr;
  }
}

// Testing: You should test your program in a variety of situations -- missing file, empty file, file with no integers, file with integers and non-integers, long file, short file, etc. 