/**
 * David Loewen
 * Game Manager
 */
import java.util.Scanner;
import java.util.ArrayList;
public class GameManager {
    private final int START_YEAR = 1300;
    private Player player;
    private Scanner scanner;
    private EventBank eventBank;
    private PQueue eventQueue;
    public GameManager(String playerName, Scanner scanner){ 
        player = new Player(playerName, this);
        this.scanner = scanner;
        eventQueue = new PQueue();
        eventBank = new EventBank(this);
    }
    
    //Plays the game
    public void playGame(){
        System.out.println("playing...");
        eventQueue.enter(eventBank.loadEvent("Opening Moves"), 0);
        Event secondlast = eventBank.loadEvent("A Deadly Premonition");
        secondlast.setTime(516);
        eventQueue.enter(secondlast, 516);
        Event last = eventBank.loadEvent("Death of a Salesman");
        last.setTime(576);
        eventQueue.enter(last, 576);
        Event nextEvent;
        while(!eventQueue.isEmpty()){
            nextEvent = (Event)eventQueue.leave();
            System.out.println("***************************************");
            System.out.println(convToDate(nextEvent.getTime()));
            System.out.println("***************************************");
            Decision decision = player.getDecision(nextEvent);
            //Add subsequent events if the decision calls for it
            String futureEvent = decision.getEvent();
            if(futureEvent != null){
                Event subsequentEvent = eventBank.loadEvent(futureEvent);
                subsequentEvent.setTime(nextEvent.getTime() + decision.nextTime);
                eventQueue.enter(subsequentEvent, nextEvent.getTime() + decision.nextTime);
            }
            if(nextEvent instanceof YearlyEvent){
                //Time for the next Yearly Event
                ArrayList<String> banlist = new ArrayList<String>();
                Event nextYearlyEvent = eventBank.loadEvent("The Start of Another Year");
                nextYearlyEvent.setTime(nextEvent.getTime() + 12);
                eventQueue.enter(nextYearlyEvent, nextYearlyEvent.getTime());
                //Now, generate 1-3 random events throughout the year, but only if we are not in Kaffa
                int numEvents = player.getStats().get("City") != 3 ? (int)(Math.random() * 3) + 1 : 0;
                for(int i = 0; i < numEvents; i++){
                    int newTime = nextEvent.getTime() + (int)(Math.random() * 11) + 1;
                    Event nextRandomEvent = eventBank.loadRandomEvent(banlist);
                    nextRandomEvent.setTime(newTime);
                    eventQueue.enter(nextRandomEvent, newTime);
                    banlist.add(nextRandomEvent.getName());
                }                   
            }
            if(nextEvent.getName().equals("Death of a Salesman")){eventQueue.clear();}
        }
        player.getFinalSummary();
    }
    //Converts an integer time to a date. 0 = Jan. 1300.
    private String convToDate(int input){
        int year = START_YEAR + (input / 12);
        input %= 12;
        String output;
        switch(input){
            case 0:
                output = "January";
                break;
            case 1:
                output = "February";
                break;
            case 2:
                output = "March";
                break;
            case 3:
                output = "April";
                break;
            case 4:
                output = "May";
                break;
            case 5:
                output = "June";
                break;
            case 6:
                output = "July";
                break;
            case 7:
                output = "August";
                break;
            case 8:
                output = "September";
                break;
            case 9:
                output = "October";
                break;
            case 10:
                output = "November";
                break;
            case 11:
                output = "December";
                break;
            default:
                output = "ERROR: UNKNOWN MONTH";
        }
        return output + " " + year;
    }
    public Scanner getScanner(){return scanner;}
    public PQueue getEventQueue(){return eventQueue;}
    public Player getPlayer(){return player;}
}
