package symbols;


import llir.LLIRNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionDescriptor extends TypeDescriptor {

    //protected FunctionBodyDescriptor bodyDescriptor;
    protected SymbolsTable bodyTable;
    protected SymbolsTable parametersTable;

    private boolean isStatic;
    private boolean isExtended;
    private Access access;
    private String name;
    private List<LLIRNode> functionBody;


    public FunctionDescriptor(){
        super(null);
        this.isStatic = false;
        this.isExtended = false;
        this.access = Access.DEFAULT;
        this.bodyTable = new SymbolsTable();
        this.parametersTable = new SymbolsTable();
        this.bodyTable.setParent(this.parametersTable);
        this.functionBody = new ArrayList<>();
    }

    public void addParameter(FunctionParameterDescriptor parameterDescriptor) {
        this.parametersTable.addSymbol(parameterDescriptor.getName(), parameterDescriptor, false);
    }

    public void addBodyVariable(String name, VariableDescriptor variableDescriptor) {
        this.bodyTable.addSymbol(name, variableDescriptor, false);
    }


    public SymbolsTable getBodyTable() {
        return bodyTable;
    }

    public SymbolsTable getParametersTable() {
        return parametersTable;
    }

    public Type getReturnValue() {
        return type;
    }
    
    public void setReturnValue(String returnValue){

        TypeString typeString = new TypeString(returnValue);
        Type type = typeString.parseType();
        this.type = type;
    }

    public void makeStatic() {
        this.isStatic = true;
    }
    
    public boolean isStatic(){
        return this.isStatic;
    }

    public void setAccessVal(String val){
        if(val == "public"){
            this.access = Access.PUBLIC;
        } else if(val == "private"){
            this.access = Access.PRIVATE;
        } else if(val=="protected"){
            this.access = Access.PROTECTED;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void print(String prefix) {
        String newPrefix = prefix + "   ";

        String staticString;
        if(isStatic) staticString = "static";
        else staticString = "non-static";

        // printing function name and if it's static
        System.out.println(prefix + "FUNCTION " + this.name + " (" + staticString + ")");

        // printing return value
        System.out.println(newPrefix + "Return:\n" + newPrefix + "   " + this.type);

        // printing parameters
        if(parametersTable.getSize() > 0) {
            System.out.println(newPrefix + "Parameters:");
            parametersTable.print(newPrefix + "   ");
        } else {
            System.out.println(newPrefix + "Parameters:");
            System.out.println(newPrefix + "   " + "None");
        }

        // printing body
        if(bodyTable.getSize() > 0) {
            System.out.println(newPrefix + "Body:");
            bodyTable.print(newPrefix + "   ");
        } else {
            System.out.println(newPrefix + "Body:");
            System.out.println(newPrefix + "   " + "None");
        }

        System.out.println();
    }


    public void addLLIRNode(LLIRNode node){
        functionBody.add(node);
    }


    public List<LLIRNode> getFunctionBody() {
        return functionBody;
    }
    
    public void setExtended(){
        this.isExtended = true;
    }

    public boolean isExtended(){
        return this.isExtended;
    }
}