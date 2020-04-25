
import symbols.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class CodeGenerator {
    private PrintWriter out;
    private StringBuilder builder;
    private ClassDescriptor classDescriptor;


    public CodeGenerator(SimpleNode root, SymbolsTable symbolsTable) {
        this.builder = new StringBuilder();
        this.classDescriptor = getClass(root,symbolsTable);
        try {
            FileWriter file = new FileWriter("src/codeGeneration/generatorFile.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(file);
            this.out = new PrintWriter(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generate(){

        generateHeader();
        nl();
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

            }
        }

    }

    private void generateFunction(FunctionDescriptor functionDescriptor) {
        generateFunctionHeader(functionDescriptor);
        generateFunctionBody(functionDescriptor);

    }

    private void generateFunctionBody(FunctionDescriptor functionDescriptor) {
    }

    private void generateFunctionHeader(FunctionDescriptor function){
        if(function.getName().equals("main"))
            generateMainHeader(function);
        else
            generateMethodHeader(function);
        nl();

        //TODO Mudar para nao ser hardcoded
        write(".limit stack 10");
        nl();

        write(".limit locals 10");
        nl();

  
    }

    private void generateMethodHeader(FunctionDescriptor function) {
        write(".method public ");
        write(function.getName());
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

    private void generateHeader() {
        write(".class public ");
        write(this.classDescriptor.getName());
        nl();
        if(this.classDescriptor.getParentClass() != ""){
            write(".super ");
            write(this.classDescriptor.getParentClass());
        } else {
            write(".super java/lang/Object");
        }
        nl();

    }



    private void write(String content){
        this.builder.append(content);
    }

    public String searchClass(SimpleNode rootNode) {
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

    public ClassDescriptor getClass(SimpleNode root,SymbolsTable symbolsTable){
        String classIdentifier = searchClass(root);
        List<Descriptor> classes =  symbolsTable.getDescriptor(classIdentifier);
        return (ClassDescriptor) classes.get(0);
    }
}
