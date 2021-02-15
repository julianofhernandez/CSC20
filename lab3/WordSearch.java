//***********************************************
//author: Julian Hernandez
//date: 2/21/2021
//title: WordSearch
//description: Lab 3
//*************************************************

import java.util.*;
import java.io.*;

class WordSearch {
	// These hold how many needles and haystack values we should expect
	public static int needlesCount = 0;
	public static int haystackCount = 0;
	
	// These hold our needle and haystack values that will be pulled from the file
	public static String[] needles = new String[10];
	public static String[] haystack = new String[10];
	public static String[][] finds = new String[10][3];
	
	// Drives our program
	public static void main(String[] args) throws FileNotFoundException {
	
		// Loads our file and puts our needles and haystack into
		loadFile("wordpuzzle.dat");
		
		// Sort our needles array
		sortArrays();
		
		// Searches for needles and stores them
		findNeedles();
		
		// Prints clean output
		printOutput();
	}
	
	// Collects the data from the file and inserts it into the arrays
	public static void loadFile(String path) {
		Scanner dataFile = null;
		File file = new File (new File(path).getAbsolutePath());
        try {  
            dataFile = new Scanner(file);
        } 
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
		
		while (dataFile.hasNext()) {
			String word = dataFile.next();
			if (word.equals("***")) {
				break;
			} else {
				WordSearch.needles[WordSearch.needlesCount] = word;
				WordSearch.needlesCount++;
			}
		}
		while (dataFile.hasNext()) {
			String word = dataFile.next();
			if (word.equals("***")) {
				break;
			} else {
				WordSearch.haystack[WordSearch.haystackCount] = word;
				WordSearch.haystackCount++;
			}
		}
	}
	
	public static void sortArrays() {
		Arrays.sort(WordSearch.needles, 0, WordSearch.needlesCount);
	}
	
	// Searches for needle in the haystack
	public static void findNeedles() {
		for (int i=0; i<needlesCount; i++) {
			// row : found[0]
			// column : found[1]
			int[] found = new int[]{-1,-1};
			for (int j=0; j<haystackCount; j++) {
				found[1] = WordSearch.haystack[j].indexOf(WordSearch.needles[i]);
				if (found[1] != -1) {
					// Add 1 so it is human readable and break
					found[1] = found[1] + 1;
					found[0] = j + 1;
					break;
				}
			}
			WordSearch.finds[i] = new String[]{WordSearch.needles[i], String.valueOf(found[0]), String.valueOf(found[1])};
		}
	}
	
	// This helps print out the data cleanly
	public static void printCleanLine(String searchWord, String row, String column) {
		System.out.printf(" %8s | %3s | %2s\n", searchWord, row, column);
	}
	
	public static void printOutput() {
		System.out.println("The following words can be found in the forward horizontal direction\n");
		printCleanLine("word","row","column");
		printCleanLine("--------","---","------");
		for(int i=0; i<WordSearch.needlesCount; i++) {
			if (!WordSearch.finds[i][1].equals("-1")) {
				printCleanLine(WordSearch.finds[i][0], WordSearch.finds[i][1], WordSearch.finds[i][2]);
			}
			
		}
		System.out.println("\nThe following word(s) can't be found in the horizontal forward direction.\n" +  
		"The following table shows a *-1* as the result because the word wasn't found.\n");
		printCleanLine("word","row","column");
		printCleanLine("--------","---","------");
		for(int i=0; i<WordSearch.needlesCount; i++) {
			if (WordSearch.finds[i][1].equals("-1")) {
				printCleanLine(WordSearch.finds[i][0], WordSearch.finds[i][1], WordSearch.finds[i][2]);
			}
			
		}
	}
}
