/**
 * David Loewen
 * Effects attached to a decision
 * Or, what the decision will do if chosen.
 * It modifies a stat (id) by a value. If set is true, the stat is instead SET to that value.
 */
//import java.util.Hashtable;
public class Effect{
    private String id;
    private int value;
    private boolean set;
    public Effect(String id, int value, boolean set){
        this.id = id;
        this.value = value;
        this.set = set;
    }
    //Apply the effect to the player.
    public void applyEffect(Player player){
        int nextValue = set ? value : (Integer)player.getStats().get(id) + value;
        player.getStats().put(id, nextValue);
    }
    public String getID(){return id;}
    public int getValue(){return value;}
    //Get ready...
    public boolean getSet(){return set;}
    //Go!
    public String toString(){
        return "Stat: " + id + "\nValue: " + value;
    }
}