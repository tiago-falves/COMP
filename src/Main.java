import symbols.SymbolsTable;


public class Main {
	
	public static void main(String[] args) throws ParseException, java.io.FileNotFoundException {

        ParseException.resetWhileErrors();

        SymbolsTable symbolsTable = new SymbolsTable();

        Javamm myJavamm = new Javamm(new java.io.FileInputStream(args[0]));
        SimpleNode root = myJavamm.JavaMM(); // returns reference to root node

        int errors = ParseException.getWhileErrors();
        if(errors > 0){
            throw new ParseException("Errors");
        }

        //root.dump(""); // prints the tree on the screen
        root.dumpTest("", 10);
        TableGenerator tb = new TableGenerator(root);
        tb.build();

        System.out.println("\n\n\nPRINTING SYMBOLS TABLE...\n");
        tb.getTable().print("");
        System.out.println();

        CodeGenerator codeGenerator = new CodeGenerator(root);

	}	
}