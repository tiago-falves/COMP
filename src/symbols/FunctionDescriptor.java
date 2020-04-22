package symbols;

public class FunctionDescriptor extends TypeDescriptor {



    //protected FunctionBodyDescriptor bodyDescriptor;
    protected SymbolsTable bodyTable;
    protected SymbolsTable parametersTable;

    private Type actualReturnValue;
    private boolean isStatic;
    private Access access;
    private String name;

    public FunctionDescriptor(){
        super(null);
        this.isStatic = false;
        this.access = Access.DEFAULT;
        this.bodyTable = new SymbolsTable();
        this.parametersTable = new SymbolsTable();
        this.bodyTable.setParent(this.parametersTable);
    }

    public void addParameter(FunctionParameterDescriptor parameterDescriptor) {
        this.parametersTable.addSymbol(parameterDescriptor.getName(), parameterDescriptor);
    }

    public void addBodyVariable(String name,VariableDescriptor variableDescriptor) {
        this.bodyTable.addSymbol(name, variableDescriptor);
    }


    public SymbolsTable getBodyTable() {
        return bodyTable;
    }

    public void setBodyTable(SymbolsTable bodyTable) {
        this.bodyTable = bodyTable;
    }

    public SymbolsTable getParametersTable() {
        return parametersTable;
    }

    public void setParametersTable(SymbolsTable parametersTable) {
        this.parametersTable = parametersTable;
    }

    public Type getReturnValue() {
        return type;
    }
    
    public void setReturnValue(String returnValue){

        TypeString typeString = new TypeString(returnValue);
        Type type = typeString.parseType();
        //Nao falta string no type?
        this.type = type;
    }

    public Type getActualReturnValue() {
        return this.actualReturnValue;
    }
    
    public void setActualReturnValue(String returnValue){
        //TODO Dar parse da expressao, por exemplo ver se e true ou false e essas cenas

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