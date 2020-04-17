package symbols;

public class FunctionDescriptor extends Descriptor {
    
    //protected FunctionBodyDescriptor bodyDescriptor;
    protected SymbolsTable bodyTable;
    protected SymbolsTable parametersTable;
    private String returnValue;
    private boolean isStatic;
    private Access access;
    private String name;

    public FunctionDescriptor(){
        this.isStatic = false;
        this.access = Access.DEFAULT;
    }

    public void addParameter(FunctionParameterDescriptor parameterDescriptor) {
        this.parametersTable.addSymbol(parameterDescriptor.getName(), parameterDescriptor);
    }

    public void addBodyVariable(VariableDescriptor variableDescriptor) {
        this.bodyTable.addSymbol(variableDescriptor.getName(), variableDescriptor);
    }

    /*public FunctionBodyDescriptor getBodyDescriptor() {
        return this.bodyDescriptor;
    }
    
    public void setBodyDescriptor(FunctionBodyDescriptor bodyDescriptor){
        this.bodyDescriptor = bodyDescriptor;
    }*/

    public String getReturnValue() {
        return this.returnValue;
    }
    
    public void setReturnValue(String returnValue){
        this.returnValue = returnValue;
    }

    public void makeStatic() {
        this.isStatic = true;
    }
    
    public boolean isStatic(){
        return this.isStatic;
    }
    
    public Access getAccess(){
        return access;
    }
    
    public void setAccess(Access access){
        this.access = access;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

}