
import codeGeneration.CodeGeneratorConstants;
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

        generateClassHeader();
        nl();
        generateConstructor();
        generateFunctions();
        out.println(this.builder);
        out.close();
    }

    private void generateConstructor() {
        write(".method public <init>()V\n\taload_0\n\tinvokenonvirtual java/lang/Object/<init>()V\n\treturn\n.end method\n\n");

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
        generateFunctionFooter(functionDescriptor);

    }



    //Corrigr, para ja vou calcular numero de argumentos
    private int calculateNumberstack(FunctionDescriptor functionDescriptor) {
        return functionDescriptor.getParametersTable().getSize();
    }

    //Calcular numero de variaveis locais
    private int calculateNumberLocals(FunctionDescriptor functionDescriptor) {
        return functionDescriptor.getBodyTable().getSize();
    }

    private void generateFunctionBody(FunctionDescriptor functionDescriptor) {
    }

    private void generateFunctionHeader(FunctionDescriptor function){
        /*if(function.getName().equals("main"))
            generateMainHeader(function);
        else
            generateMethodHeader(function);*/
        generateMethodHeader(function);
        nl();



  
    }

    private void generateMethodHeader(FunctionDescriptor function) {
        write(".method public ");
        generateMethodSignature(function);nl();
        generateMethodStackLocals(function); nl();
        
    }

    private void generateMethodStackLocals(FunctionDescriptor function) {
        int locals = calculateNumberLocals(function);
        int stackSize = calculateNumberstack(function); //TODO O que é isto? como se calcula?
        write("\t.limit stack " + stackSize); nl();
        write("\t.limit locals " + locals); nl();

    }

    private void generateMethodSignature(FunctionDescriptor function) {
        String methodName = function.getName();
        String methodDescriptor = getMethodParameters(function);
        String returnType = getJvmType(function);

        write(methodName + "(" + methodDescriptor + ")" + returnType);

    }
    //TODO como exatamente e que se deve imprimir isto
    private String getMethodParameters(FunctionDescriptor function) {

        String parameters = new String();

        SymbolsTable parametersTable = function.getParametersTable();
        LinkedHashMap<String, List<Descriptor>> table = parametersTable.getTable();
        Set<String> keys = table.keySet();

        for(String k:keys){
            List<Descriptor> functionDescriptors = table.get(k);
            for (Descriptor descriptor : functionDescriptors){
                FunctionParameterDescriptor parameterDescriptor= (FunctionParameterDescriptor) descriptor;
                String parameter = generateMethodParameter(parameterDescriptor);
                parameters=  parameters.concat(parameter);

            }
        }


        return parameters;
    }

    private String generateMethodParameter(FunctionParameterDescriptor parameterDescriptor) {
        return  getJvmType(parameterDescriptor);
    }


    private void generateMainHeader(FunctionDescriptor function) {
        write(".method public static main([Ljava/lang/String;)V");

    }

    private void generateFunctionFooter(FunctionDescriptor functionDescriptor) {
        write(".end method");
    }
    

    private void nl(){
        this.builder.append("\n");
    }
    private void tab(){
        this.builder.append("\t");
    }

    private void generateClassHeader() {
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

    private String getJvmType(TypeDescriptor typeDescriptor){
        Type type = typeDescriptor.getType();
        String className = typeDescriptor.getClassName();
        StringType stringType = new StringType(type);
        String typeString = stringType.getString();

        String jvmType = CodeGeneratorConstants.types.get(typeString);
        if(jvmType != null){
            return jvmType;
        }else{
            return className;
        }

    }
}
