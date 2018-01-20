package spell;

/**
 * Created by GrantRowberry on 1/20/17.
 */

public class TrieNode implements ITrie.INode {
    int frequency = 0;
    TrieNode[] children;
    static int nodeAdded = 1;
    private Trie t;

    public TrieNode(Trie t){
        this.t = t;
        t.nodeCount++;
    }
    @Override
    public int getValue() {
        return frequency;
    }
    public void forRoot(){
        children = new TrieNode[26];
    }
    public void incrementValue(){
        frequency++;
    }
    public boolean hasChildren(){
        if(children == null){
            return false;
        }
        return true;
    }
    public TrieNode addChild(int index){
        if(children == null){
            children = new TrieNode[26];
            children[index] =  new TrieNode(t);
            return children[index];
        } else {
            if(children[index] == null){
                children[index] = new TrieNode(t);
            }
            return children[index];
        }

    }


    public TrieNode getChild(int index){
        return children[index];
    }


}
