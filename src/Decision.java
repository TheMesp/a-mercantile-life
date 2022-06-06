/**
 * David Loewen
 * Event decision
 */
import java.util.ArrayList;
public class Decision extends Item {
    private String name;
    private ArrayList<Effect> effects;
    private ArrayList<Condition> conditions;
    public int nextTime;
    private String[] possibleEvents;
    public Decision(String n, ArrayList<Effect> effects, ArrayList<Condition> conditions, String[] nextEvents, int nextTime){
        name = n;
        this.effects = effects;
        this.conditions = conditions;
        this.possibleEvents = nextEvents;
        this.nextTime = nextTime;
    }
    public Decision(String n, ArrayList<Effect> effects, ArrayList<Condition> conditions, String nextEvent, int nextTime){
        name = n;
        this.effects = effects;
        this.conditions = conditions;
        this.possibleEvents = new String[]{nextEvent};
        this.nextTime = nextTime;
    }
    public Decision(String n, ArrayList<Effect> effects, ArrayList<Condition> conditions){
        this(n, effects, conditions, (String[])null, 0);
    }
    public Decision(String n, ArrayList<Effect> effects){
        this(n, effects, new ArrayList<Condition>());
    }
    public Decision(String n){
        this(n, new ArrayList<Effect>());
    }
    public String toString(){return name;}
    public ArrayList<Effect> getEffects(){return effects;}
    //Apply the effects of a decision.
    public void applyEffects(Player player){
        for(int i = 0; i < effects.size(); i++){
            effects.get(i).applyEffect(player);
        }
    }
    public String getEvent(){
        String output = null;
        if(possibleEvents != null && possibleEvents.length != 0){
            output = possibleEvents[(int)(Math.random() * possibleEvents.length)];
        }
        return output;
    }
    //Is this decision valid?
    public boolean isValid(){
        for(int i = 0; i < conditions.size(); i++){
            if(!(conditions.get(i).isTrue())){
                return false;
            }
        }
        return true;
    }
}
