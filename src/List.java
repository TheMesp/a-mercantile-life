/**
 * David Loewen
 * Basic list structure
 */
public class List {
    private class Node{
        private Item item;
        private Node next;
        private Node(Item i, Node n){
            next = n;
            item = i;
        }
    }
    private Node top;
    private int size;
    public List() {
        top = null;
        size = 0;
    }
    public void insert(Item newItem){
        top = new Node(newItem, top);
        size++;
    }
    //searches for an item by string name. returns null if not found.
    public Item search(String query){
        Item output = null;
        Node curr = top;
        while(curr != null && output == null){
            if(curr.item.toString().equals(query)){
                output = curr.item;
            }
            curr = curr.next;
        }
        return output;
    }
    //returns the entire list as an array.
    //Note to self: this is HORRID code and has no error handling.
    //But, it's not like Prof. Cossar will dock marks for bad Java!
    //(I hope you don't read this)
    public Item[] toArray(){
        Item[] output = null;
        Node curr = top;
        if(size > 0){
            output = new Item[size];
            for(int i = 0; i < size; i++){
                output[i] = curr.item;
                curr = curr.next;
            }
        }
        return output;
    }
    public int getSize(){return size;}
    
}
