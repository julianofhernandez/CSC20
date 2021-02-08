//***********************************************
//author: Julian Hernandez
//date: 2/1/2021
//title: MagicBall
//description: Lab 1
//*************************************************

import java.util.*;
import java.util.Scanner;
import java.io.*;

class MagicBall {
  public static final int SIZE = 20;
  public static String[] answers = new String[20];

  // This is the main portion of the file
  public static void main(String[] args) throws FileNotFoundException {
    readAnswers();
    playGame();
  }

  // This reads in the answers from answers.txt and puts them in an array
  public static void readAnswers() throws FileNotFoundException {
    Scanner myReader = new Scanner(new File("answers.txt"));

    for(int i =0; i < SIZE; i++) {
      String line = myReader.nextLine();
      answers[i] = line;
    }
    myReader.close();
  }

  // This is our main game logic
  public static void playGame() throws FileNotFoundException {
    Random rand = new Random();
    boolean exit = false;
    while (!exit) {
      String input;
      int random_int = rand.nextInt(SIZE);
      Scanner inputScanner = new Scanner(System.in);
      System.out.println("What is your question?");
      input = inputScanner.nextLine();
      if (input.equals("")) {
        exit = true;
      } else {
        System.out.println(answers[random_int]);
      }
    }
  }
}