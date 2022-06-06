/**
 * David Loewen
 * A Random Event is an event that can be chosen at random to fill in gaps in a month.
 */
public class RandomEvent extends Event {
    public RandomEvent(String name, String description, int time, PQueue eventQueue){
        super(name,description,time,eventQueue);
    }
    protected RandomEvent(RandomEvent other){
        super(other);
    }
    public RandomEvent deepCopy(){
        return new RandomEvent(this);
    }
}
