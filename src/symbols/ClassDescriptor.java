package symbols;

public class ClassDescriptor extends Descriptor{
    
    // Local table: parent = parameter table
    protected SymbolsTable functionsTable;
    
    private boolean isStatic;
    private Access access;

    public ClassDescriptor() {
        this.access = Access.DEFAULT;
        this.isStatic = false;
        this.functionsTable = new SymbolsTable();
    }

    public boolean isStatic(){
        return this.isStatic;
    }
    public Access getAccess(){
        return access;
    }

    public void makeStatic() {
        this.isStatic = true;
    }

    public void addVariable(VariableDescriptor variable) {
        SymbolsTable variablesTable = this.functionsTable.getParent();
        if (variablesTable == null) {
            variablesTable = new SymbolsTable();
        }
        variablesTable.addSymbol(variable.getName(), variable);
    }
    public SymbolsTable getVariablesTable(){
        return this.functionsTable.getParent();
    }
    public SymbolsTable getFunctionParamtersTable(){
        return this.functionsTable;
    }

    public void addMethod(FunctionDescriptor method) {
        functionsTable.addSymbol(method.getName(), method);
    }

}