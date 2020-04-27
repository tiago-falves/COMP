import symbols.ClassDescriptor;
import symbols.SymbolsTable;


public class Main {
	
	public static void main(String[] args) throws ParseException, java.io.FileNotFoundException, SemanticErrorException {

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
        if (tb.getNumErrors() > 0) 
            throw new SemanticErrorException();

        System.out.println("\n\n\nPRINTING SYMBOLS TABLE...\n");
        tb.getTable().print("");
        System.out.println();

        symbolsTable = tb.getTable();
        ClassDescriptor classDescriptor = CodeGenerator.getClass(root, symbolsTable);
        CodeGenerator codeGenerator = new CodeGenerator(classDescriptor);
        codeGenerator.generate();
	}

}