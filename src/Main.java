import symbols.SymbolsTable;
import java.util.ArrayList;



public class Main {
	
	public static void main(String[] args) throws ParseException, java.io.FileNotFoundException {
        ArrayList<SymbolsTable> symbolsTables = new ArrayList<SymbolsTable>();


        ParseException.resetWhileErrors();

        Javamm myJavamm = new Javamm(new java.io.FileInputStream(args[0]));
        myJavamm.setSymbolsTables(symbolsTables);
        SimpleNode root = myJavamm.JavaMM(); // returns reference to root node

        //root.generateSymbolTable();
        //root.analyseSemantics();

        int errors = ParseException.getWhileErrors();
        if(errors > 0){
            throw new ParseException("Errors"); 
        }
        root.dump(""); // prints the tree on the screen

        for (SymbolsTable s : symbolsTables) {
            System.out.println(s.toString());
        }
	}	
}