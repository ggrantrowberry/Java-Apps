package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by GrantRowberry on 1/20/17.
 */

public class SpellCorrector implements ISpellCorrector {
    Trie dictionary = new Trie();
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner s = new Scanner(file);
        while(s.hasNext()){
            dictionary.add(s.nextLine().toLowerCase());
        }


        //dictionary.add("YEA");


        /*test1.add("apple");
        test1.add("baboon");

        test1.add("cares");

        test1.add("caress");
        test1.add("dingo");
        System.out.print(test1.toString());
        System.out.println("Node Count: " + test1.getNodeCount());
        System.out.println(test1.getWordCount());
        /*test1.add("cats");
        test1.add("dog");
        test1.add("cat");
        test1.add("frog");

        Trie test2 = new Trie();
        test2.add("dogs");
        test2.add("cats");
        test2.add("dog");
        test2.add("cat");
        test2.add("frog");

        if(test1.equals(test2)){
            System.out.println("Equals");
        } else {
            System.out.println("Not equals");
        }*/
        //System.out.println(dictionary.getWordCount());
        //System.out.println(dictionary.getNodeCount());
        //System.out.print(dictionary.toString());


    }

    @Override
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
        inputWord = inputWord.toLowerCase();
        if(dictionary.find(inputWord) != null){
            return inputWord;
        }

        TreeSet<String> oneEdit = new TreeSet<String>();
        oneEdit.addAll(deletion(inputWord));
        oneEdit.addAll(insertion(inputWord));
        oneEdit.addAll(alteration(inputWord));
        oneEdit.addAll(transposition(inputWord));
        TreeSet<Word> oneEditGood = new TreeSet<Word>();

        for (String edit: oneEdit){
            TrieNode node = (TrieNode) dictionary.find(edit);
            if(node != null){
                Word word = new Word(edit, node.getValue());
                oneEditGood.add(word);
            }
        }
        if(oneEditGood.isEmpty()){

            TreeSet<String> twoEdits = new TreeSet<String>();
            for(String edit: oneEdit){
                twoEdits.addAll(deletion(edit));
                twoEdits.addAll(insertion(edit));
                twoEdits.addAll(alteration(edit));
                twoEdits.addAll(transposition(edit));
            }
            TreeSet<Word> twoEditsGood = new TreeSet<Word>();
            for(String edit: twoEdits){
                TrieNode node = (TrieNode) dictionary.find(edit);
                if(node != null){
                    Word word = new Word(edit, node.getValue());
                    twoEditsGood.add(word);
                }
            }
            if(twoEditsGood.isEmpty()){
                throw new NoSimilarWordFoundException();
            } else {
                return twoEditsGood.first().word;
            }
        }


        return oneEditGood.first().word;
    }

    private TreeSet<String> deletion(String inputWord){
        TreeSet<String> tset = new TreeSet<String>();
        for (int i = 0; i < inputWord.length() ; i++) {
            StringBuilder inputCopy = new StringBuilder(inputWord);
            inputCopy.deleteCharAt(i);
            tset.add(inputCopy.toString());
        }
        return tset;
    }
    private TreeSet<String> insertion(String inputWord){
        TreeSet<String> tset = new TreeSet<String>();
        for (int i = 0; i <= inputWord.length(); i++) {
            for(int j = 0; j < 26; j++){
                StringBuilder inputCopy = new StringBuilder(inputWord);
                char letter = (char)(j + 'a');
                inputCopy.insert(i, letter);
                tset.add(inputCopy.toString());
            }
        }

        return tset;
    }
    private TreeSet<String> alteration(String inputWord) {
        TreeSet<String> tset = new TreeSet<String>();
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder inputCopy = new StringBuilder(inputWord);
                char letter = (char)(j + 'a');
                inputCopy.insert(i,letter);
                inputCopy.deleteCharAt(i+1);
                tset.add(inputCopy.toString());
            }
        }
        return tset;
    }
    private TreeSet<String> transposition(String inputWord){
        TreeSet<String> tset = new TreeSet<String>();
        for (int i = 0; i < inputWord.length()-1; i++) {
                StringBuilder inputCopy = new StringBuilder(inputWord);
                char letter1 = inputCopy.charAt(i);
                char letter2 = inputCopy.charAt(i+1);
                inputCopy.setCharAt(i, letter2);
                inputCopy.setCharAt(i+1, letter1);
                tset.add(inputCopy.toString());

        }
        return tset;
    }

    public class Word implements Comparable<Word>{
        String word;
        int frequency = 0;
        Word(String newWord, int newFrequency){
            this.word = newWord;
            this.frequency = newFrequency;
        }

        @Override
        public int compareTo(Word otherWord){
            if(frequency == otherWord.frequency){
                return word.compareTo(otherWord.word);
            } else {
                return  otherWord.frequency-frequency;
            }
        }

    }
}
