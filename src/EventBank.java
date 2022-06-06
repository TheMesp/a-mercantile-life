/**
 * David Loewen 7775839
 * Event Bank class. Handles reading events and dispersing them.
 */
import java.util.Hashtable;
import java.util.ArrayList;
import java.io.*;
public class EventBank {
    private final boolean DEBUG = false;
    private Hashtable<String, Event> eventTable;
    private Object[] eventNames;
    private GameManager gameManager;
    public EventBank(GameManager gameManager){
        this.gameManager = gameManager;
        eventTable = new Hashtable<String, Event>(100);
        eventNames = null;
        loadEvents("../events/events.txt");
    }
    //Loads the events from a file.
    private void loadEvents(String filepath){
        String lastEvent = "top";//Used for bugfixing
        try{
            BufferedReader bR = new BufferedReader(new FileReader(filepath));
            String nextLine = "";
            Player player = gameManager.getPlayer();
            while(bR.ready()){
                nextLine = bR.readLine();
                if(nextLine.equals("START")){
                    //Start of a new event
                    Event newEvent;
                    String eventType = "";
                    String title = "";
                    String description = "";
                    //Read the event type
                    eventType = bR.readLine();
                    //Read the event name
                    title = bR.readLine();
                    //Read the event description
                    nextLine = bR.readLine();
                    while(!(nextLine.startsWith("-") || nextLine.equals("END"))){
                        description += nextLine + "\n";
                        nextLine = bR.readLine();
                    }
                    if(eventType.equals("YearlyEvent")){
                        newEvent = new YearlyEvent(title, description, 0, gameManager.getEventQueue());
                    }else if(eventType.equals("RandomEvent")){
                        newEvent = new RandomEvent(title, description, 0, gameManager.getEventQueue());
                    }else{
                        //Default: Assume a regular event
                        newEvent = new Event(title, description, 0, gameManager.getEventQueue());
                    }                
                    while(!nextLine.equals("END")){
                        if(nextLine.startsWith("-")){
                            //Start of a new Decision to add to the event
                            String decisionName = nextLine.substring(1);
                            String effectName;
                            String[] nextEvents = null;
                            int nextTime = 0;
                            int effectValue;
                            boolean effectSet;
                            ArrayList<Effect> effects = new ArrayList<Effect>();
                            ArrayList<Condition> conditions = new ArrayList<Condition>();
                            //get rid of the tab added for readability in the file
                            nextLine = bR.readLine().trim();                            
                            while(nextLine.startsWith("#")){
                                //Check conditions
                                nextLine = nextLine.substring(1);
                                String[] fields = nextLine.split(" ");
                                Condition nextCondition = new Condition(player, fields[0], fields[1].charAt(0), Integer.parseInt(fields[2]));
                                conditions.add(nextCondition);
                                nextLine = bR.readLine().trim();
                                if(DEBUG){System.out.println(title + ": " + decisionName + ": " + "Condition added: " + nextCondition.toString());}
                            }
                            if(nextLine.startsWith(">")){
                                //This decision has a subsequent event!
                                nextLine = nextLine.substring(1);
                                String[] fields = nextLine.split(">");
                                nextEvents = fields[0].split("&");
                                nextTime = Integer.parseInt(fields[1]);
                                nextLine = bR.readLine().trim();
                                if(DEBUG){
                                    for(int i = 0; i < nextEvents.length; i++){
                                        System.out.println(title + ": " + decisionName + ": " + "Possible Future Event added: " + nextEvents[i]);
                                    }
                                }
                            }
                            while(!(nextLine.startsWith("-") || nextLine.equals("END"))){
                                //Read the effects
                                String[] fields = nextLine.split(" ");
                                effectName = fields[0].replace('_', ' ');
                                effectValue = Integer.parseInt(fields[2]);
                                if(fields[1].equals("-")){effectValue *= -1;}
                                effectSet = fields[1].equals("=");
                                Effect nextEffect = new Effect(effectName, effectValue, effectSet);
                                effects.add(nextEffect);
                                if(DEBUG){System.out.println(title + ": " + decisionName + ": " + "Decision effect added: " + nextEffect.toString());}
                                nextLine = bR.readLine().trim();
                            }
                            newEvent.addDecision(new Decision(decisionName, effects, conditions, nextEvents, nextTime));
                        }
                    }
                    eventTable.put(newEvent.getName(), newEvent);
                    lastEvent = newEvent.getName();
                    System.out.println("Loaded " + lastEvent);
                }
            }
            bR.close();
            eventNames = eventTable.keySet().toArray();
            System.out.println("Loaded " + eventNames.length + " events from events.txt!");
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Error: events.txt formatted incorrectly!");
            System.out.println("Last succesful event: " + lastEvent);
        }
    }
    //returns a random event that is not on the banlist
    public Event loadRandomEvent(ArrayList<String> banlist){
        Event output = null;   
        String nextEventName;
        do{
            nextEventName = eventNames[(int)(Math.random() * eventNames.length)].toString();
            if(banlist == null || !banlist.contains(nextEventName)){
                output = loadEvent(nextEventName);
            }
        }while(!(output instanceof RandomEvent));
        return output;
    }
    //Return a specific event
    public Event loadEvent(String id){
        Event newEvent = eventTable.get(id);
        if(newEvent != null){
            return newEvent.deepCopy();
        }else{
            System.out.println("ERROR: " +id+ " NOT FOUND. LOADING DEBUG EVENT");
            return eventTable.get("Debug");
        }
    }
    
}
