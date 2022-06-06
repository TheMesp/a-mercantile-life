/**
 * David Loewen
 * CityList class used for returning city distances, city ware names, and city ID translations.
 */
public class CityList {
    public static final int NUM_CITIES = 7;
    /*
     * 0 = Pisa
     * 1 = Genoa
     * 2 = Ferrara
     * 3 = Kaffa
     * 4 = Naples
     * 5 = Barcelona
     * 6 = Lubeck (You cannot go to lubeck due to extreme distance! Rare events can give you lubeckian Amber - HUGE value
     */
    public static final String[] cityNames = {
        "Pisa",
        "Genoa",
        "Ferrara",
        "Kaffa",
        "Naples",
        "Barcelona",
        "Lübeck"
    };
    public static final String[] goodNames = {
        "Grain",
        "Olive Oil",
        "Fruits",
        "Slaves",
        "Wine",
        "Wool",
        "Amber"
    };
    public static final String[] cityPrefixes = {
        "Pisan",
        "Genovese",
        "Ferrarese",
        "Kaffa",
        "Neapolitan",
        "Barcelonese",
        "Lübecker"
    };
    //Can you travel to this location
    public static final boolean[] canVisit = {
        true,
        true,
        true,
        true,
        true,
        true,
        false
    };
    //Can you sell to this location?
    public static final boolean[] canSell = {
        true,
        true,
        true,
        false,
        true,
        true,
        false
    };
    //Is this location a Ghibelline stronghold?
    public static final int[] likesHRE = {
        1,
        0,
        0,
        0,
        0,
        0,
        1//Lubeck is IN the HRE!
    };
    //Is this location a Guelph stronghold?
    public static final int[] likesPope = {
        0,
        1,
        1,
        0,
        1, //Naples is a bit of a weird case, but in general the pope does exert influence on southern italy
        0,
        0
    };
    //How much rep is needed to sell here?
    public static final int[] rep = {
        25,
        30,
        30,
        99999, //You should never be able to sell to Kaffa or Lubeck.
        30,
        45,
        99999
    };
    //These distances are in tens of nautical miles, rounded.
    //For example, 13 is roughly 130 nautical miles.
    public static final int[][] cityDists = {
        {  0,  8,105,160, 26, 38,289},
        {  8,  0,113,168, 33, 35,287},
        {105,113,  0,156, 82,131,369},
        {160,168,156,  0,137,186,422},
        { 26, 33, 82,137,  0, 56,301},
        { 38, 35,131,186, 56,  0,254},
        {289,287,369,422,301,254,  0}
    };
    public static double calcMult(int city1, int city2){
        return 1.0 + Math.sqrt((double)cityDists[city1][city2]/422.0) * 3;
    }
    public static String getCityGoods(int i){
        return cityPrefixes[i] + " " + goodNames[i];
    }
}
