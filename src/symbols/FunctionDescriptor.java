package symbols;

public class FunctionDescriptor extends Descriptor {
    
    //protected FunctionBodyDescriptor bodyDescriptor;
    protected SymbolsTable bodyTable;
    protected SymbolsTable parametersTable;
    private Type returnValue;
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

    public Type getReturnValue() {
        return this.returnValue;
    }
    
    public void setReturnValue(String returnValue){
        //Nao falta string no type?
        if(returnValue == "int"){
            this.returnValue = Type.INT;
        } else if(returnValue == "int[]"){
            this.returnValue = Type.INT_ARRAY;
        } else if(returnValue=="void"){
            this.returnValue = Type.VOID;
        } else if(returnValue=="boolean"){
            this.returnValue = Type.BOOLEAN;
        } else{
            this.returnValue = Type.CLASS;
        }
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

}