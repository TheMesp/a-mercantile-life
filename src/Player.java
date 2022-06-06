/**
 * David Loewen
 * Human player - prompts the console for inputs
 */
import java.util.Scanner;
import java.util.Hashtable;
import java.util.ArrayList;
public class Player{
    private final int STARTING_IMM = 100;
    private String name;
    private GameManager gameManager;
    private Hashtable<String, Integer> stats;
    public Player(String n, GameManager gm) {
        name = n;
        gameManager = gm;
        stats = new Hashtable<String, Integer>(100);
        stats.put("Cash", STARTING_IMM);
        stats.put("City", 0);
        stats.put("Plan", 0);
        stats.put("SWares", 0);//specific wares - moved to specific city wares later
        stats.put("RWares", 0);//randomized wares - will be dispersed randomly 
        //0Wares through to 6Wares: specific city wares
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            stats.put(("" + i + "Wares"), 0);
        }
        stats.put("TWares", 0);//Total Wares
        stats.put("SRep", 0);
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            stats.put(("" + i + "Rep"), 0);
        }
        stats.put("0Rep", 50);
        stats.put("CRep", 0);
        stats.put("IndCash", 400);
        stats.put("Guelph",0);//Does this city support the guelphs?
        stats.put("Ghibelline",0);//Does this city support the ghibellines?
        
            
    }
    //Sum total wares
    private int getTotalWares(){
        int output = 0;
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            output += stats.get("" + i + "Wares");
        }
        return output;
    }
    //Sum total rep
    private int getTotalRep(){
        int output = 0;
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            output += stats.get("" + i + "Rep");
        }
        return output;
    }
    public Decision getDecision(Event e){
        //Safety: If the event has no decisions, have a generic "Continue..." decision that does nothing
        if(e.numDecisions() == 0){
            e.addDecision(new Decision("Continue..."));
        }
        int output = -1;
        Scanner scan = gameManager.getScanner();
        System.out.println(e.toString());
        System.out.println("\nType a number to make a decision:\n");
        System.out.println(e.printDecisions());
        output = scan.hasNextInt() ? scan.nextInt() : -1;
        while(output <= 0 || output > e.numDecisions()){
            if(scan.hasNextInt()){
                System.out.println("Invalid number. Please enter a number between 1 and "+e.numDecisions()+ ".");
                output = scan.nextInt();
            }else{
                System.out.println("Invalid input. Please enter a number between 1 and "+e.numDecisions()+ ".");
                output = -1;
                scan.next();//Wipe the irrelevant line
            }
        }
        Decision doutput = e.getDecision(output - 1);
        doutput.applyEffects(this); 
        resolveStats();
        if(e instanceof YearlyEvent){
            System.out.println(getSummary());
        }
        return doutput;
    }
    public Hashtable<String, Integer> getStats(){return stats;}
    
    //resolves temp stats like Plan, SWares and RWares
    private void resolveStats(){
        stats.put("TWares", getTotalWares());//Update ware total
        if(stats.get("IndCash") < 0){stats.put("IndCash", 0);}
        if(stats.get("Cash") < 0){stats.put("Cash", 0);}
        if(stats.get("SWares") != 0){
            String nWares = "" + stats.get("City") + "Wares";
            stats.put(nWares, stats.get(nWares) + Math.max(stats.get("SWares"), -1 * stats.get(nWares)));
            stats.put("SWares", 0);
        }
        if(stats.get("SRep") != 0){
            String nRep = "" + stats.get("City") + "Rep";
            stats.put(nRep, stats.get(nRep) + stats.get("SRep"));
            stats.put("SRep", 0);
        }
        stats.put("CRep", stats.get("" + stats.get("City") + "Rep"));
        while(stats.get("RWares") != 0){
            //choose a stat
            String nextStat = "" + (int)(Math.random() * CityList.NUM_CITIES) + "Wares";
            int nextVal = (int)Math.ceil(Math.random() * stats.get("RWares"));
            nextVal = nextVal <= 0 ? nextVal - 1 : nextVal;//subtract 1 if negative
            stats.put("RWares", stats.get("RWares") - nextVal);
            stats.put(nextStat, stats.get(nextStat) + Math.max(nextVal, -1 * stats.get(nextStat)));
        }
        while(stats.get("Plan") != 0){
            Scanner scan = gameManager.getScanner();
            String nWares = "" + stats.get("City") + "Wares";
            int nextInt = 0;       
            while(nextInt != 3 && nextInt != 4 && nextInt != 5){
                System.out.println(getSummary());
                System.out.println("Type a number to choose an action:");
                System.out.println("1: Buy " + CityList.getCityGoods(stats.get("City")) + " from local markets");
                System.out.println("2: Sell Wares to " + CityList.cityPrefixes[stats.get("City")] + " markets");
                System.out.println("3: Set sail to a new city (Will take a month and end the current planning session!)");
                System.out.println("4: Build Reputation in current city (Will take a month and end the current planning session!)");
                System.out.println("5: Cash in half of Bills of Exchange (Will take a month and end the current planning session!)");
                System.out.println("6: Review Stat Summary");
                nextInt = scan.hasNextInt() ? scan.nextInt() : 0;
                while(nextInt <= 0 || nextInt > 6){
                    if(scan.hasNextInt()){
                        System.out.println("Invalid number. Please enter a number between 1 and 5.");
                        nextInt = scan.nextInt();
                    }else{
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                        nextInt = 0;
                        scan.next();//Wipe the irrelevant line
                    }
                }
                if(nextInt == 1){
                    //Buy goods
                    if(stats.get("Cash") != 0){
                        System.out.println("How many wares would you like to purchase? (1 cash = 1 ware)");
                        nextInt = scan.hasNextInt() ? scan.nextInt() : -1;
                        while(nextInt < 0 || nextInt > stats.get("Cash")){
                            if(scan.hasNextInt()){
                                System.out.println("Invalid number. Please enter a number between 0 and "+stats.get("Cash")+ ".");
                                nextInt = scan.nextInt();
                            }else{
                                System.out.println("Invalid input. Please enter a number between 0 and "+stats.get("Cash")+ ".");
                                nextInt = -1;
                                scan.next();//Wipe the irrelevant line
                            }
                        }
                        stats.put("Cash", stats.get("Cash") - nextInt);
                        stats.put(nWares, stats.get(nWares) + nextInt);
                        System.out.println("Purchased " + nextInt + " " + CityList.getCityGoods(stats.get("City")));
                    }else{
                        System.out.println("You have no cash with which to buy goods!");
                    }
                    nextInt = 0;                    
                }else if(nextInt == 2){
                    //Sell goods
                    if(getTotalWares() != 0){
                        if(CityList.canSell[stats.get("City")] && stats.get("" + stats.get("City") + "Rep") >= CityList.rep[stats.get("City")]){
                            System.out.println("Which type of wares would you like to bring to the " + CityList.cityPrefixes[stats.get("City")] + " market?");
                            ArrayList<Integer> options = new ArrayList<Integer>();
                            //only add options for goods you actually have
                            for(int i = 0; i < CityList.NUM_CITIES; i++){
                                if(stats.get("" + i + "Wares") > 0){
                                    options.add(i);
                                }
                            }
                            //Print options for player viewing
                            for(int i = 0; i < options.size(); i++){
                                System.out.println((i+1) + ": " + CityList.getCityGoods(options.get(i))
                                                       + " (Stock: " + stats.get("" + options.get(i) + "Wares")
                                                       + ", Value Per Unit: " + String.format("%.5g",CityList.calcMult(stats.get("City"), options.get(i))) + ")");
                            }
                            //Fetch player response
                            nextInt = scan.hasNextInt() ? scan.nextInt() : 0;
                            while(nextInt < 1 || nextInt > options.size()){
                                if(scan.hasNextInt()){
                                    System.out.println("Invalid number. Please enter a number between 1 and "+options.size()+ ".");
                                    nextInt = scan.nextInt();
                                }else{
                                    System.out.println("Invalid input. Please enter a number between 1 and "+options.size()+ ".");
                                    nextInt = 0;
                                    scan.next();//Wipe the irrelevant line
                                }
                            }
                            nextInt--;
                            //Get player sale amount
                            int wareNum = options.get(nextInt);
                            int wareStock = stats.get("" + wareNum + "Wares");
                            System.out.println("How much " + CityList.getCityGoods(wareNum) + " would you like to sell?\n"
                                                   + "(Stock: " + wareStock + ", "
                                                   + "Value Per Unit: " + String.format("%.5g",CityList.calcMult(stats.get("City"), wareNum)) + ")");
                            nextInt = scan.hasNextInt() ? scan.nextInt() : -1;
                            while(nextInt < 0 || nextInt > wareStock){
                                if(scan.hasNextInt()){
                                    System.out.println("Invalid number. Please enter a number between 0 and "+wareStock+ ".");
                                    nextInt = scan.nextInt();
                                }else{
                                    System.out.println("Invalid input. Please enter a number between 0 and "+wareStock+ ".");
                                    nextInt = -1;
                                    scan.next();//Wipe the irrelevant line
                                }
                            }
                            int totalProfit = (int)(CityList.calcMult(stats.get("City"), wareNum) * nextInt);
                            System.out.println("Sold " + nextInt + " " + CityList.getCityGoods(wareNum) + " for $" + totalProfit);
                            stats.put("" + wareNum + "Wares", wareStock - nextInt);
                            stats.put("Cash", stats.get("Cash") + totalProfit);
                            nextInt = 0;
                        }else{
                            if(CityList.canSell[stats.get("City")]){
                                System.out.println("You do not have a high enough reputation in "+CityList.cityNames[stats.get("City")]+" to reliably sell goods!");
                                System.out.println("(Your reputation: " + stats.get("" + stats.get("City") + "Rep") + ", Needed reputation: "+CityList.rep[stats.get("City")]+")");
                            }else{
                                System.out.println(CityList.cityNames[stats.get("City")] + " does not possess an urban market!");
                            }
                        }
                    }else{
                        System.out.println("You don't have any goods to sell!");
                    }
                    nextInt = 0;
                }else if(nextInt == 3){
                    //Sail to new city
                    System.out.println("Choose your sailing destination:");
                    ArrayList<Integer> destinations = new ArrayList<Integer>();
                    
                    for(int i = 0; i < CityList.NUM_CITIES; i++){
                        if(i != stats.get("City") && CityList.canVisit[i]){
                            destinations.add(i);
                        }
                    }
                    for(int i = 0; i < destinations.size(); i++){
                        System.out.println(i + 1 + ": " + CityList.cityNames[destinations.get(i)]);
                    }
                    nextInt = scan.hasNextInt() ? scan.nextInt() : 0;
                    while(nextInt < 1 || nextInt > destinations.size()){
                        if(scan.hasNextInt()){
                            System.out.println("Invalid number. Please enter a number between 1 and "+destinations.size()+ ".");
                            nextInt = scan.nextInt();
                        }else{
                            System.out.println("Invalid input. Please enter a number between 1 and "+destinations.size()+ ".");
                            nextInt = 0;
                            scan.next();//Wipe the irrelevant line
                        }
                    }
                    nextInt--;
                    System.out.println("Setting sail to " + CityList.cityNames[destinations.get(nextInt)] + "!");
                    stats.put("City", destinations.get(nextInt));
                    int cashConversion = (int)(0.75 * stats.get("Cash"));
                    System.out.println("Converted " + cashConversion + " Cash into Bills of Exchange.");
                    stats.put("IndCash", stats.get("IndCash") + cashConversion);
                    stats.put("Cash", stats.get("Cash") - cashConversion);
                    stats.put("Guelph", CityList.likesPope[stats.get("City")]);
                    stats.put("Ghibelline", CityList.likesHRE[stats.get("City")]);
                    nextInt = 3;
                }else if(nextInt == 4){
                    stats.put("" + stats.get("City") + "Rep", stats.get("" + stats.get("City") + "Rep") + 10);
                    System.out.println("Built connections in " + CityList.cityNames[stats.get("City")] + ", gaining 10 reputation. (new total: " + stats.get("" + stats.get("City") + "Rep") + ")");
                }else if(nextInt == 5){
                    stats.put("IndCash", stats.get("IndCash") / 2);
                    stats.put("Cash", stats.get("Cash") + stats.get("IndCash"));
                    System.out.println("Converted " + stats.get("IndCash") + " value from Bills of Exchange into cash");
                }
            }
            stats.put("Plan", 0);
        }
    }
    //returns summary stats
    public String getSummary(){
        String output = "";
        output += "=======================================\n";
        output += "***************************************\n";
        output += "=======================================\n";
        output += "Summary for " + name + "\n";
        output += "Current City of Residence: " + CityList.cityNames[stats.get("City")] + "\n";
        output += "Immediate Cash: " + stats.get("Cash") + "\n";
        output += "Indirect Cash (Bills of Exchange): " + stats.get("IndCash") + "\n";
        output += "Stock of " + CityList.getCityGoods(stats.get("City")) + ": " + stats.get("" + stats.get("City") + "Wares") + "\n";
        output += "Stock of other goods:\n";
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            if(i != stats.get("City") && stats.get("" + i + "Wares") != 0){
                output += "\t" + CityList.getCityGoods(i) + ": " + stats.get("" + i + "Wares") + "\n";
            }
        }
        if(CityList.canSell[stats.get("City")]){
            output += "Reputation in " + CityList.cityNames[stats.get("City")] + ": " + stats.get("" + stats.get("City") + "Rep") + " (" + CityList.rep[stats.get("City")] + " Reputation is needed to sell goods in "+ CityList.cityNames[stats.get("City")] +")\n";
        }else{
            output += "Reputation is not recognized in this location.\n";
        }
        output += "Reputation in other cities:\n";
        for(int i = 0; i < CityList.NUM_CITIES; i++){
            if(i != stats.get("City")){
                if(CityList.canSell[i]){
                    output += "\t" + CityList.cityNames[i] + ": " + stats.get("" + i + "Rep") + "\n";
                }
            }
        }
        output += "=======================================\n";
        output += "***************************************\n";
        output += "=======================================\n";
        return output;
    }
    public String getName(){return name;}
    public void getFinalSummary(){
        System.out.println("=======================================\n***************************************\n=======================================");
        System.out.println("GAME OVER!\n");
        int finalCash = stats.get("Cash");
        int finalInd = stats.get("IndCash");
        int totalWares = getTotalWares();
        int totalRep = getTotalRep();
        System.out.println("Final Cash: " + finalCash);
        System.out.println("Final Value of Bills of Exchange: " + finalInd);
        System.out.println("Final Value of Reputation " + totalRep);
        System.out.println("Final Value of Wares: " + totalWares);
        
        System.out.println("\nFinal Score: " + (finalCash + finalInd + totalRep + totalWares));
        
    }
}
