
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
    private HashMap<FunctionDescriptor,SimpleNode> funtionNodes;


    public CodeGenerator(ClassDescriptor classDescriptor, HashMap<FunctionDescriptor,SimpleNode> funtionNodes) {
        this.builder = new StringBuilder();
        this.classDescriptor = classDescriptor;
        this.funtionNodes = funtionNodes;
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

            }
        }

    }

    private void generateFunction(FunctionDescriptor functionDescriptor) {

        FunctionHeader functionHeader = generateFunctionHeader(functionDescriptor);
        FunctionFooter footer = new FunctionFooter();

        write(functionHeader.generate());
        generateFunctionBody(functionDescriptor);
        write(footer.generate());


    }

    private FunctionHeader generateFunctionHeader(FunctionDescriptor function){

        FunctionParameters functionParameters = new FunctionParameters(function);
        String methodDescriptor = functionParameters.generate();
        String returnType = CodeGeneratorConstants.getJvmType(function);

        return new FunctionHeader(function.getName(),methodDescriptor,returnType);

    }


    private void generateFunctionBody(FunctionDescriptor functionDescriptor) {
        //SimpleNode functionNode = this.funtionNodes.get(functionDescriptor);
        //MethodBody methodBody = new MethodBody(functionNode,functionDescriptor);
        VarDeclarations varDeclarations = new VarDeclarations(functionDescriptor);
        write(varDeclarations.generate());
        generateStatements(functionDescriptor);

    }

    private void generateStatements(FunctionDescriptor functionDescriptor) {
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
