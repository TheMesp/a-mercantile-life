/**
 * David Loewen
 * A Yearly Event is a special kind of event that takes place at the start of every month.
 */
public class YearlyEvent extends Event {
    public YearlyEvent(String name, String description, int time, PQueue eventQueue){
        super(name,description,time,eventQueue);
    }
    protected YearlyEvent(YearlyEvent other){
        super(other);
    }
    public YearlyEvent deepCopy(){
        return new YearlyEvent(this);
    }
}
