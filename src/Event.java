/**
 * David Loewen
 * 7775839
 * Event class. Represents an event encountered in game.
 */
import java.util.ArrayList;

public class Event extends Item{
    //What is the event queue, so we can add more events if needed?
    private PQueue eventQueue;
    private String name;
    private String description;
    private int time;//What time does this event occur at?
    private ArrayList<Decision> decisions;
    //Valid decisions!
    private ArrayList<Decision> validDecisions;
    public Event(String name, String description, int time, PQueue eventQueue){
        decisions = new ArrayList<Decision>();
        validDecisions = new ArrayList<Decision>();
        this.name = name;
        this.description = description;
        this.time = time;
        this.eventQueue = eventQueue;
    }
    protected Event(Event e){
        this.decisions = e.decisions;
        this.validDecisions = e.validDecisions;
        this.name = e.name;
        this.description = e.description;
        this.time = e.time;
        this.eventQueue = e.eventQueue;
    }
    //returns a deep copy of this event.
    public Event deepCopy(){
        return new Event(this);
    }
    public void addDecision(Decision decision){
        decisions.add(decision);
    }
    //How many decisions does this event have?
    public int numDecisions(){
        validateDecisions();
        return validDecisions.size();
    }
    public Decision getDecision(int num){
        validateDecisions();
        if(validDecisions.size() <= num || num < 0){
            System.out.println("ERROR: Decision out of scope?");
            return null;
        }else{
            return validDecisions.get(num);
        }
    }
    public String getName(){return name;}
    public int getTime(){return time;}
    public void setTime(int newTime){time = newTime;}
    public PQueue getEventQueue(){return eventQueue;}
    
    //toString should just print the name and description of the event.
    public String toString(){
        return name + "\n\n" + description;
    }
    public String printDecisions(){
        String output = "";
        validateDecisions();
        for(int i = 0; i < validDecisions.size(); i++){
            output += "" + (i+1) + ": " + validDecisions.get(i).toString() + "\n";
        }
        return output;
    }
    //update validDecisions
    private void validateDecisions(){
        validDecisions = new ArrayList<Decision>();
        for(int i = 0; i < decisions.size(); i++){
            if(decisions.get(i).isValid()){
                validDecisions.add(decisions.get(i));
            }
        }
    }
}
