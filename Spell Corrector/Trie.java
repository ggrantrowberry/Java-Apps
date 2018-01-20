package spell;

/**
 * Created by GrantRowberry on 1/20/17.
 */

public class Trie implements spell.ITrie {
    private int wordCount = 0;
    public int nodeCount = 0;
    TrieNode root = new TrieNode(this);
    public Trie() {
        root.forRoot();
    }


    @Override
    public void add(String word) {
        word = word.toLowerCase();
        TrieNode current = root;
        for(int i = 0; i < word.length(); i++){
            int index = word.charAt(i)-'a';

            current = current.addChild(index);

        }
        if(current.getValue() == 0) {
            ++wordCount;
        }
        current.incrementValue();
        int p = current.getValue();

    }

    @Override
    public INode find(String word) {
        if (word.length() == 0){
            return null;
        }
        TrieNode current = root;
        for(int i = 0; i < word.length(); i++){
            int index = word.charAt(i) - 'a';
            if(current.children == null){
                return null;
            }
            current = current.getChild(index);
            if (current == null){
                return null;
            }
            if(i == word.length()-1){
                 //Checks the case that there is a shorter version of a word in the trie that wasn't actually a valid word
                if(current.getValue() > 0){
                    return current;
                } else {
                    return null;
                }
            }


        }
        return null;


    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();
        stringHelper(root, currentWord, output);

        return output.toString();
    }

    private void stringHelper(TrieNode node, StringBuilder currentWord, StringBuilder output){


        if(node.children == null){
            output.append(currentWord);
            output.append("\n");
        } else {
            if(node.getValue() > 0){
                output.append(currentWord);
                output.append("\n");
            }
            for (int i = 0; i < 26; i++) {
                if(node.children[i] != null){
                    char letter = (char)(i + 'a');
                    currentWord.append(letter);
                    stringHelper(node.children[i], currentWord, output);
                    currentWord.deleteCharAt(currentWord.length()-1);
                }
            }
        }


    }

    @Override
    public int hashCode() {
        return wordCount * nodeCount;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        if(o == this){
            return true;
        }
        if(((Trie)o).getWordCount() != this.wordCount){
            return false;
        }
        if(((Trie)o).getNodeCount() != this.getNodeCount()){
            return false;
        }
        if(equalsHelper(((Trie)o).root, this.root)){
            return true;
        } else {
            return false;
        }
    }

    public boolean equalsHelper(TrieNode node1, TrieNode node2){
        if(node1.getValue() != node2.getValue()){
            return false;
        }
        if(node1.children == null && node2.children == null){

        } else if(node1.children != null && node2.children == null  || node1.children == null && node2.children != null){
            return false;
        } else {
            for (int i = 0; i < 26; i++) {
                if (node1.children[i] == null && node2.children[i] != null) {
                    return false;
                }
                if (node1.children[i] != null && node2.children[i] == null) {
                    return false;
                }


                if (node1.children[i] == null && node2.children[i] == null) {

                } else if (equalsHelper(node1.children[i], node2.children[i])) {

                } else {
                    return false;
                }

            }
        }
        return true;
    }

}
