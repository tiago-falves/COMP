
import codeGeneration.*;
import symbols.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.io.File;


public class CodeGenerator {
    private PrintWriter out;
    private StringBuilder builder;
    private ClassDescriptor classDescriptor;

    public CodeGenerator(ClassDescriptor classDescriptor) {
        this.builder = new StringBuilder();
        this.classDescriptor = classDescriptor;
        //Creates the corresponding j file
        String className = classDescriptor.getName();
        try {
            File f = new File("compiled/jasmin/");
            f.mkdirs();
            FileWriter file = new FileWriter("compiled/jasmin/" + className + ".j", false);
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            this.out = new PrintWriter(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Main functions that generates Jasmin code
    public void generate(){
        ClassHeader classHeader = new ClassHeader(this.classDescriptor.getName());
        SuperHeader superHeader = new SuperHeader(this.classDescriptor.getParentClass());
        Fields classVars = new Fields(this.classDescriptor.getVariablesTable());
        Initializer initializer = new Initializer(this.classDescriptor.getParentClass());

        write(classHeader.generate() + "\n");
        write(superHeader.generate() + "\n");
        write(classVars.generate() + "\n");
        write(initializer.generate() + "\n");

        generateFunctions();

        out.println(this.builder);
        out.close();
    }
    //Iterates through all the functions of the class and generates function code
    public void generateFunctions(){
        SymbolsTable functionsTable = classDescriptor.getFunctionsTable();
        LinkedHashMap<String, List<Descriptor>> table = functionsTable.getTable();
        Set<String> keys = table.keySet();
        for(String k:keys){
            List<Descriptor> functionDescriptors = table.get(k);
            for (Descriptor descriptor : functionDescriptors){
                FunctionDescriptor functionDescriptor= (FunctionDescriptor) descriptor;
                if(!functionDescriptor.isExtended())
                    generateFunction(functionDescriptor);
                nl();
            }
        }

    }

    //Calls function generator and writes function code
    private void generateFunction(FunctionDescriptor functionDescriptor) {
        FunctionGenerator functionGenerator = new FunctionGenerator(functionDescriptor,this.classDescriptor);
        write(functionGenerator.generate());
    }

    private void nl(){
        this.builder.append("\n");
    }

    private void write(String content){
        this.builder.append(content);
    }

    public static String searchClass(SimpleNode rootNode) {
        int i = 0;
        while(i < rootNode.jjtGetNumChildren()) {
            SimpleNode currentNode = (SimpleNode) rootNode.jjtGetChild(i);
            if(currentNode.getId() == JavammTreeConstants.JJTCLASSDECLARATION) {
                    SimpleNode className = (SimpleNode) currentNode.jjtGetChild(0);
                    return className.jjtGetVal();
            }
            i++;
        }
        return "";
    }

    public static ClassDescriptor getClass(SimpleNode root,SymbolsTable symbolsTable){
        String classIdentifier = searchClass(root);
        List<Descriptor> classes =  symbolsTable.getDescriptor(classIdentifier);
        return (ClassDescriptor) classes.get(0);
    }
}
