/*
 * David Loewen 7775839
 * HIST3138 Final Project
 * CK3 Game Runner
 */
import java.util.Scanner;
    
public class GameRunner{
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        
        String input = "";
        System.out.println("Welcome to \"It's A Mercantile Life\"!\nPlease enter your name:");
        input = scanner.nextLine();
        GameManager gameManager = new GameManager(input, scanner);
        gameManager.playGame();
        System.out.println("Thank you for playing!\n\nCreated By David Loewen 7775839\n\nPress Enter to exit.");
        input = "qwertyuio********p";
        while(input.equals("qwertyuio********p")){
            input = scanner.nextLine();
        }
        scanner.close();
        scanner = new Scanner(System.in);
        scanner.nextLine();
        scanner.close();
    }
}