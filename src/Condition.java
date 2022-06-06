/**
 * David Loewen
 * Condition that must be true for a decision to be displayed.
 */
import java.util.Hashtable;
public class Condition {
    private Player player;
    private String var;
    private char symbol;
    private int value;
    public Condition(Player player, String var, char symbol, int value){
        this.player = player;
        this.var = var;
        this.symbol = symbol;
        this.value = value;
    }
    //Does the condition hold?
    public boolean isTrue(){
        boolean output = false;
        Hashtable<String, Integer>stats = player.getStats();
        if(symbol == '>'){
            output = stats.get(var) > value;
        }else if(symbol == '<'){
            output = stats.get(var) < value;
        }else if(symbol == '='){
            output = stats.get(var) == value;
        }else{
            //improper format. Uhh, bad.
            System.out.println("Improper Condition!");
            output = false;
        }
        return output;
    }
    public String toString(){
        return var + " " + symbol + " " + value;
    }
}
