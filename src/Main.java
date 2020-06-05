import optimizations.OptimizationsR;
import symbols.ClassDescriptor;
import symbols.SymbolsTable;


public class Main {
	
	public static void main(String[] args) throws ParseException, java.io.FileNotFoundException, SemanticErrorException {

        // Check Flags
        boolean debugMode = false;
        boolean initializedWarning = false;
        
        for(int i = 1; i < args.length; i++){
            if(args[i].equals("-d") || args[i].equals("--debug")){
                debugMode = true;
            }else if(args[i].equals("-Winit") || args[i].equals("--InitializedWarning")){
                initializedWarning = true;
            }
        }

        ParseException.resetWhileErrors();

        SymbolsTable symbolsTable = new SymbolsTable();

        Javamm myJavamm = new Javamm(new java.io.FileInputStream(args[0]));
        SimpleNode root = myJavamm.JavaMM(); // returns reference to root node

        int errors = ParseException.getWhileErrors();
        if(errors > 0){
            throw new ParseException("Errors");
        }

        if(debugMode)
            root.dumpTest("", 10);

        TableGenerator tb = new TableGenerator(root, initializedWarning);

        if(debugMode)
            System.out.println("\nBUILDING SYMBOLS TABLE WITH SEMANTIC ANALYSIS...\n");
        
        tb.build();
        
        if (tb.getNumErrors() > 0) 
            throw new SemanticErrorException();

        if(debugMode){
            System.out.println("\n\n\nPRINTING SYMBOLS TABLE...\n");
            tb.getTable().print("");
            System.out.println();
        }

        symbolsTable = tb.getTable();
        OptimizationsR optimizationsR = new OptimizationsR();
        ClassDescriptor classDescriptor = CodeGenerator.getClass(root, symbolsTable);
        CodeGenerator codeGenerator = new CodeGenerator(classDescriptor);
        codeGenerator.generate();

        OptimizationsR.print();
	}

}