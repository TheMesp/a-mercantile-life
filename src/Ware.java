/**
 * David Loewen
 * Represents some form of merchandise.
 */
public class Ware {
    public int value;
    private int cityID;
    public Ware(int cityID, int value) { 
        this.value = value;
        this.cityID = cityID;
    }
    public int getID(){return cityID;}
    
}
