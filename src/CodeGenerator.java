
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


public class CodeGenerator {
    private PrintWriter out;
    private StringBuilder builder;
    private ClassDescriptor classDescriptor;

    public CodeGenerator(ClassDescriptor classDescriptor) {
        this.builder = new StringBuilder();
        this.classDescriptor = classDescriptor;
        try {
            FileWriter file = new FileWriter("src/codeGeneration/generatorFile.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            this.out = new PrintWriter(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generate(){
        ClassHeader classHeader = new ClassHeader(this.classDescriptor.getName());
        SuperHeader superHeader = new SuperHeader(this.classDescriptor.getParentClass());
        Initializer initializer = new Initializer(this.classDescriptor.getParentClass());

        write(classHeader.generate() + "\n");
        write(superHeader.generate() + "\n");
        write(initializer.generate() + "\n");

        generateFunctions();

        out.println(this.builder);
        out.close();
    }

    public void generateFunctions(){
        SymbolsTable functionsTable = classDescriptor.getFunctionsTable();
        LinkedHashMap<String, List<Descriptor>> table = functionsTable.getTable();
        Set<String> keys = table.keySet();
        for(String k:keys){
            List<Descriptor> functionDescriptors = table.get(k);
            for (Descriptor descriptor : functionDescriptors){
                FunctionDescriptor functionDescriptor= (FunctionDescriptor) descriptor;
                generateFunction(functionDescriptor);
                nl();
            }
        }

    }

    private void generateFunction(FunctionDescriptor functionDescriptor) {
        FunctionGenerator functionGenerator = new FunctionGenerator(functionDescriptor);
        write(functionGenerator.generate());

        //generateFunctionBody(functionDescriptor);

    }

    private void generateFunctionBody(FunctionDescriptor functionDescriptor) {
        VarDeclarations varDeclarations = new VarDeclarations(functionDescriptor);
        write(varDeclarations.generate());
        generateStatements(functionDescriptor);

    }

    private void generateStatements(FunctionDescriptor functionDescriptor) {
        // add
    }

    private void generateMainHeader(FunctionDescriptor function) {
        write(".method public static main([Ljava/lang/String;)V");
    }

    private void nl(){
        this.builder.append("\n");
    }

    private void tab(){
        this.builder.append("\t");
    }

    private void generateClassHeader() {
        // add
    }

    private void write(String content){
        this.builder.append(content);
    }

    public static String searchClass(SimpleNode rootNode) {
        int i = 0;
        // System.out.println(rootNode.jjtGetNumChildren());
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