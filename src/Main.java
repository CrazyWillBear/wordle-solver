// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    // create reader and scanner
    BufferedReader reader;
    Scanner scanner = new Scanner(System.in);

    // init necessary variables
    ArrayList<String> unorder = new ArrayList<String>();
    String guesses = new String();
    String tmp = new String();
    String order = "-----";

    while (true) {
      System.out.print("\n:: What letters do you have so far (type in lowercase, no spaces, no duplicates):\n\t>> ");
      String letters = scanner.nextLine();

      // detect commands
      if (letters.equals("exit")) {
        scanner.close();
        System.out.println("Exitting...");
        return;
      }
      if (letters.equals("clear")) {
        System.out.println("Clearing...");
        guesses = "";
        unorder = new ArrayList<String>();
        order = "-----";
        continue;
      }

      System.out.print("\n:: Put letters into a 5 character long statement putting '-' wherever there is an unknown and a character wherever it DOES belong (ex: if 'o' is the third letter and nothing else is known, put '--o--':\n\t>> ");
      tmp = scanner.nextLine();

      // assign default value if input is empty
      if (tmp.isEmpty()) { tmp = "-----"; }

      // add changes to order variable
      char[] newOrder = order.toCharArray();
      for (int i = 0; i < 5; i++) {
        if (tmp.charAt(i) != '-') { newOrder[i] = tmp.charAt(i); }
      }
      order = String.valueOf(newOrder);

      System.out.print("\n:: Put letters into a 5 character long statement putting '-' wherever there is a blank and a character wherever it DOESN'T belong (ex: if 'o' isn't the third letter and nothing else is known, put '--o--':\n\t>> ");
      tmp = scanner.nextLine();

      // assign default value if input is empty regardless add to unorder arraylist
      if (tmp.isEmpty()) { tmp = "-----"; }
      unorder.add(tmp);

      System.out.print("\n:: What incorrect letters have you guessed so far? (only new incorrect letters, type in lowercase, no spaces, no duplicates):\n\t>> ");

      // add incorrect guesses to guesses list
      guesses += scanner.nextLine();

      // detect errors
      if (order.length() < 5) {
        System.out.println("\n:: ERROR: Shorter than length 5");
        continue;
      }

      try {
        reader = new BufferedReader(new FileReader("words.txt"));
        String line = reader.readLine();

        // read through words file
        while (line != null) {

          // check to see if any letters are in incorrect places
          boolean boolInUnorder = true;
          for (String strUnorder : unorder) {
            if (!inUnorder(line, strUnorder)) {
              boolInUnorder = false;
              break;
            }
          }

          // final check
          if (line.length() == 5 && containsLetters(line, letters) && inOrder(line, order) && !containsBadGuesses(line, guesses) && boolInUnorder) { System.out.println("\t::" + line); }

          line = reader.readLine();
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean containsLetters(String answer, String letters) {
    int c = 0;
    for (char ch : letters.toCharArray()) {
      if (answer.contains(String.valueOf(ch))) { c++; }
    }
    if (c == letters.length()) { return true; }
    return false;
  }

  public static boolean inOrder(String answer, String order) {
    int c = 0;
    int w = 0;
    for (int i = 0; i < 5; i++) {
      if (order.charAt(i) != '-' && answer.charAt(i) == order.charAt(i)) { c++; }
      if (order.charAt(i) != '-') { w++; }
    }
    if (c == w) { return true; }
    return false;
  }

  public static boolean inUnorder(String answer, String unorder) {
    for (int i = 0; i < 5; i++) {
      if (unorder.charAt(i) != '-' && answer.charAt(i) == unorder.charAt(i)) { return false; }
    }
    return true;
  }
  
  public static boolean containsBadGuesses(String answer, String guesses) {
    for (char ch : guesses.toCharArray()) {
      if (answer.contains(String.valueOf(ch))) { return true; }
    }
    return false;
  }
}