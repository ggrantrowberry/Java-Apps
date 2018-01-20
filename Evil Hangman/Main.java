package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by GrantRowberry on 1/27/17.
 */

public class Main {

    public static void main(String[] args) {
        EvilHangmanGame game = new EvilHangmanGame();
        try {
            File dictionary = new File(args[0]);

            int wordLength = Integer.parseInt(args[1]);
            int guesses = Integer.parseInt(args[2]);
            game.startGame(dictionary, wordLength);
            Scanner in = new Scanner(System.in);

            while (guesses > 0) {
                System.out.println("You have " + guesses + " left");
                System.out.println("Used letters " + game.getlettersUsed());
                System.out.println("Word: "+ game.getCurrentWord());
                char guess;
                while(true){
                    System.out.print("Enter guess: ");
                    String s = in.nextLine();
                    s = s.toLowerCase();
                    if(!s.isEmpty()) {
                        guess = s.charAt(0);
                        if(!Character.isLetter(guess)){
                            System.out.println("Invalid input. Please input a letter.");}
                        else
                        { break;
                        }
                    } else {
                        System.out.println("Invalid input. Please input a letter");
                    }

                }



                try{
                    int correctGuesses = game.isGoodGuess(guess);
                    if (correctGuesses > 0){
                        System.out.println("Yes, there is " + correctGuesses + " " + guess);
                        if(!game.getCurrentWord().contains("-")){
                            System.out.println("You Won!");
                            System.out.println("The correct word was " + game.getLosingWord());
                            guesses = 0;
                        }
                    } else {
                        System.out.println("Sorry, there are no " + guess + "'s");
                        guesses--;
                        if(guesses == 0){
                            System.out.println("You lose!");
                            System.out.println("The correct word was " + game.getLosingWord());
                        }
                    }
                }
                catch(IEvilHangmanGame.GuessAlreadyMadeException e){
                    System.out.println("Guess already made. Try again.");
                }


            }
            in.close();

        } catch (FileNotFoundException e) {
            System.out.println("Invalid File");
            e.printStackTrace();
        }
    }
}
