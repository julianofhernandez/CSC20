import java.util.*;

/**
* An array of strings that is smart
* 
* @author Julian Hernandez
* @version v1.0
*/
public class SmartStringList {
    private String[] arr;
    private boolean sorted = true;
    private int currentSize = 0;

    /**constructs a list of given max capacity (use an array internally)
    @param capacity Capacity of internal list */
    public SmartStringList(int capacity) {
        this.arr = new String[capacity];
    }

    /** adds given String to end of list 
    * @param s String to search for*/
    public void add(String s) {
        if (currentSize < arr.length) {
            this.arr[currentSize] = s;
            currentSize++;
        }
        if (currentSize > 3) {
            if (this.arr[currentSize-2].compareToIgnoreCase(this.arr[currentSize-1]) > 0) {
                sorted = false;
            }
        }
    }

    /** returns the String at index i. 
    * @param i Index to get
    * @return The string at that index
    */
    public String get(int i) {
        return arr[i];
    }

    /** returns the position of the given value or -1 if not found. 
    * @param s String to search for
    * @return The index of that string
    */
    public int indexOf(String s) {
        if (this.sorted) {
            System.out.println("Searching using binary search");
            int min = 0;
            int max = currentSize;
            int mid;
            while (min < max) {
                mid = (max + min)/2;
                int search = s.compareToIgnoreCase(arr[mid]);
                if (search == 0) {
                    return mid;
                } else if (search > 0) {
                    min = mid + 1;
                } else if (search < 0) {
                    max = mid;
                }
            }
            return -1;
        } else {
            System.out.println("Searching using linear search");
            for (int i = 0; i < currentSize; i++) {
                if (s.equalsIgnoreCase(this.arr[i])) {
                    return i;
                }
            }
            return -1;
        }
    }

    /** Sort the array */
    public void sort() {
        sort(this.arr, 0, currentSize);
    }

    /** puts the list in sorted order, if not already. 
    @param arr Array to sort
    @param start The index of the beginning of the sort
    @param end The index of the end of the sort
    */
    public void sort(String[] arr, int start, int end) {
        // arr[0] = "a";
        int n = end - start;
        String min = arr[start];
        int minIndex = start;
        for (int i = start; i<end; i++) {
            if (arr[i].compareToIgnoreCase(min) < 0) {
                min = arr[i];
                minIndex = i;
            }
        }
        String temp = arr[start];
        arr[start] = min;
        arr[minIndex] = temp;
        if (n <= 1) {
            return;
        } else {
            sort(arr, start +1, end);
        }
        this.sorted = true;
    }
}