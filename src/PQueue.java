/**
 * David Loewen 7775839
 * HIST3138 Final Project
 * Priority Queue
 */
public class PQueue {
    //********************************************************
    //internal priority node class
    //********************************************************
    private class PNode{
        private int priority;
        private Item item;
        private PNode next;
        private PNode(int p, Item i, PNode n){
            priority = p; item = i; next = n;
        }
    }
    private PNode top;
    
    //********************************************************
    //constructor
    //********************************************************
    public PQueue() { 
        top = null;
    }
    
    //********************************************************
    //enter
    //
    //Insert a new item into the priority queue (lowest to the front of the queue)
    //********************************************************
    public void enter(Item newItem, int newPrio){
        if(top == null){
            top = new PNode(newPrio, newItem, null);
        }else{
            //edge case: new top priority
            if(newPrio <= top.priority){
                top = new PNode(newPrio, newItem, top);
            }else{
                PNode curr = top;
                PNode prev = null;
                while(curr != null && newPrio > curr.priority){
                    prev = curr;
                    curr = curr.next;
                }
                //prev cannot be null as the edge case is already covered
                prev.next = new PNode(newPrio, newItem, curr);
            }
        }
    }
    
    //********************************************************
    //leave
    //
    //Fetch the current best item
    //********************************************************
    public Item leave(){
        PNode oldTop = top;
        top = top.next;
        return oldTop.item;
    }
    
    //********************************************************
    //clear
    //
    //clears the entire priority queue.
    //********************************************************
    public void clear(){
        top = null;
    }
    
    //gosh i wonder what this does
    public boolean isEmpty(){return top == null;}
    
    
}
