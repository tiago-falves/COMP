import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MastermindAux {
    
    public static final String[] colors = {"B", "G", "O", "P", "R", "Y"};

    public static int getMaxColors(){
        return colors.length;
    }

    public static int[] getUserInput(int max){
        int[] input = new int[max];

        System.out.println("Color Options: Blue (0), Green(1), Orange(2), Purple(3), Red(4), Yellow(5)");
        
        int num;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for(int i = 0; i < max; i++){
            System.out.print("Choose a color to position " + i + ": ");
            
            do{
                try{
                    String s = br.readLine();
                    num = Integer.parseInt(s);
                    if(num <= 5 && num >= 0){
                        break;
                    }
                    System.out.println("Error in input");
                }catch(Exception e){System.out.println("Error in input");}
            }while(true);

            input[i] = num;
        }

        return input;
    }

    public static void menu(){
        System.out.println(" _______________________________________________");
        System.out.println("|         __   __ ___  __  _                 _  |");
        System.out.println("| |\\  /| |__| |__  |  |_  |_| |\\  /| | |\\ | | \\ |"); 
        System.out.println("| | \\/ | |  |  __| |  |__ | \\ | \\/ | | | \\| |_/ |");
        System.out.println("|_______________________________________________|\n");
    }

    public static void printResult(int[] result){
        System.out.println();
        
        String s1 = result[0] == 1? new String(" is") : new String("s are");
        String s2 = result[1] == 1? new String(" has") : new String("s have"); 

        System.out.println(result[0] + " peg" + s1 + " correct in both color and position");
        System.out.println(result[1] + " peg" + s2 + " a correct color in the wrong position");

        System.out.println();
    }

    public static void printWinMessage(){
        System.out.println("\nCongratulations! You won the game!\n");
    }

    public static void printLoseMessage(){
        System.out.println("\nYou lost the game! Better luck next time...\n");
    }
}   