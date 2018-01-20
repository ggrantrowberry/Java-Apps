package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.Pack200;

/**
 * Created by GrantRowberry on 1/27/17.
 */

public class EvilHangmanGame implements hangman.IEvilHangmanGame {
    private String currentWord = new String();
    Set<String> wordsLeft = new TreeSet<>();
    private TreeSet<Character> lettersUsed = new TreeSet<>();
    int wordLength;




    public void startGame(File Dictionary, int wordLength) throws FileNotFoundException {
        this.wordLength = wordLength;
        Scanner s = new Scanner(Dictionary);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < wordLength ; i++) {
            b.append("-");
        }
        currentWord = b.toString();

        while(s.hasNext()){
            String temp = s.next();
            if(temp.length() == wordLength){
                wordsLeft.add(temp);
            }
        }

        s.close();

    }


    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Character stringGuess = guess;
        if(lettersUsed.contains(guess)){
            throw new GuessAlreadyMadeException();
        } else {

            lettersUsed.add(stringGuess);
        }

        Map<String,Set<String>> wordGroups = makePattern(guess);
        int largestSubset = 0;
        Map<String,Set<String>> intermediateSubsets= new TreeMap<>();


        for (Map.Entry<String, Set<String>> entry: wordGroups.entrySet()) { //Finds the wordGroup(s) that is the largest
            String key = entry.getKey();
            Set<String> value = entry.getValue();
            if(entry.getValue().size() > largestSubset){
                intermediateSubsets.clear();
                intermediateSubsets.put(key,value);
                largestSubset = entry.getValue().size();
            } else if(entry.getValue().size() == largestSubset){
                intermediateSubsets.put(key,value);
            }
        }

        Map<String,Set<String>> intermediateSubsets2=  new TreeMap<>(intermediateSubsets);

        if(intermediateSubsets.size() > 1){ //If there are two wordGroups of equal size this finds the one with the most dashes
            int mostDashes=0;

            for(Map.Entry<String, Set<String>> entry: intermediateSubsets.entrySet()){
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                int dashCount = 0;
                for (int i = 0; i < key.length(); i++) {
                    if(key.charAt(i) == '-'){
                        dashCount++;
                    }
                }
                if(dashCount > mostDashes){
                    intermediateSubsets2.clear();
                    intermediateSubsets2.put(key,value);
                    mostDashes = dashCount;
                } else if(dashCount == mostDashes){
                    intermediateSubsets2.put(key,value);
                }


            }
                int indexTracker = 0; //This is to make sure that characters besides the last one are looked at
                while(intermediateSubsets2.size() > 1) {
                   intermediateSubsets2 = findRightmostLetter(intermediateSubsets2,indexTracker);
                    indexTracker++;
                }
        }
        Set<String> toReturn = new TreeSet<>();
        String toUpdateCurrentWord = new String();
        for (Map.Entry<String,Set<String>> entry: intermediateSubsets2.entrySet()){
            toUpdateCurrentWord = entry.getKey();
            toReturn =  entry.getValue();
        }
        char[] currentWordArray = currentWord.toCharArray();

        for (int i = 0; i < toUpdateCurrentWord.length(); i++) {
            if(toUpdateCurrentWord.charAt(i) == guess){
                currentWordArray[i] = toUpdateCurrentWord.charAt(i);
            }
        }
        currentWord = String.valueOf(currentWordArray);
        wordsLeft = toReturn;
        return toReturn;
    }









    public String getCurrentWord(){
        return currentWord;
    }

    public char[] getCurrentWordArray() {

        char[] currentWordArray = currentWord.toCharArray();
        return currentWordArray;
    }

    public String getlettersUsed(){
        return lettersUsed.toString();
    }

    public int isGoodGuess(char guess) throws GuessAlreadyMadeException {
        Set<String> possibleWordList = makeGuess(guess);

        int guessCount = 0;
        for (String s: possibleWordList) {
            for (int i = 0; i < s.length(); i++) {
                if(s.charAt(i) == guess){
                    guessCount++;
                }
            }
            return guessCount;
        }
        return 0;
    }


    public  Map<String,Set<String>> makePattern(char guess){

        Map<String, Set<String>> wordGroups = new TreeMap<String, Set<String>>();
        for (String word:wordsLeft) { //Iterates through all the words in the set
            char[] currentWordArray = currentWord.toCharArray();

            for (int i = 0; i < word.length() ; i++) { //Iterates through the characters in the word
                if(word.charAt(i) == guess){ //Checks to see if there is the guess character at that index.
                    currentWordArray[i] = guess;
                } else if(currentWordArray[i] == '-') {

                }


            }
            String keyWord = String.valueOf(currentWordArray);
         if(wordGroups.containsKey(keyWord.toString())){
                wordGroups.get(keyWord.toString()).add(word); //Puts the word in the correct set based on word group
            } else {
                Set<String> addSet= new TreeSet<String>(); //Adds new word group with accompanying word.
                addSet.add(word);
                wordGroups.put(keyWord.toString(),addSet);
            }
            
        }
        return wordGroups;
    }

    private Map<String, Set<String>> findRightmostLetter(Map<String, Set<String>> intermediateSubset, int wordLengthCopy){
        Map<String, Set<String>> subset = new TreeMap<>();

        for (Map.Entry<String, Set<String>> entry : intermediateSubset.entrySet()) {
            String key = entry.getKey();
            if (key.charAt(key.length()-wordLengthCopy-1) != '-') {
                    subset.put(entry.getKey(), entry.getValue());
            }
            }
        if(subset.size() > 0) {
            return subset;
        }
        return intermediateSubset;

    }

    public String getLosingWord(){
        String p = new String();
        for (String s: wordsLeft) {
            return s;
        }
        return p;
    }
}

