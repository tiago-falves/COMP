import optimizations.RegisterReducer;
import symbols.ClassDescriptor;
import symbols.SymbolsTable;
import optimizations.OptimizationManager;
import optimizations.OptimizationsRException;


public class Main {
	
	public static void main(String[] args) throws ParseException, java.io.FileNotFoundException, SemanticErrorException, OptimizationsRException {

        // Check Flags
        boolean debugMode = false;
        boolean initializedWarning = false;
        
        for(int i = 1; i < args.length; i++){
            if(args[i].equals("-d") || args[i].equals("--debug")){
                debugMode = true;
            }else if(args[i].equals("-Winit") || args[i].equals("--InitializedWarning")){
                initializedWarning = true;
            }else if(args[i].equals("-o")){
                OptimizationManager.constantPropagation = true;
            }else if(args[i].equals("-f")){
                OptimizationManager.constantFolding = true;
            }else if(args[i].matches("-r=\\d+")){
                try {
                    OptimizationManager.reducedLocals = true;
                    OptimizationManager.maximumLocalVariables = Integer.parseInt(args[i].substring(3));
                } catch (final NumberFormatException e) {
                    System.err.println("Invalid -r option");
                }
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
        ClassDescriptor classDescriptor = CodeGenerator.getClass(root, symbolsTable);
        CodeGenerator codeGenerator = new CodeGenerator(classDescriptor);
        codeGenerator.generate();

        if (OptimizationManager.error) {
            throw new OptimizationsRException();
        } else {
            System.out.println("\nCode generated successfuly\n");
        }
	}

}