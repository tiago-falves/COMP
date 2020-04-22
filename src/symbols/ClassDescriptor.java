package symbols;

public class ClassDescriptor extends Descriptor{
    
    // Local table: parent = parameter table
    protected SymbolsTable functionsTable;
    private String name;
    private boolean isStatic;
    private Access access;

    public ClassDescriptor() {
        this.access = Access.DEFAULT;
        this.isStatic = false;
        this.functionsTable = new SymbolsTable();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
        variablesTable.addSymbol(variable.getName(), variable, false);
    }
    public SymbolsTable getVariablesTable(){
        return this.functionsTable.getParent();
    }
    public SymbolsTable getFunctionParamtersTable(){
        return this.functionsTable;
    }

    public void addMethod(String name,FunctionDescriptor method) {
        functionsTable.addSymbol(name, method);
    }

    public void print(String prefix) {
        String staticString;
        if(isStatic) staticString = "static";
        else staticString = "non-static";

        System.out.println(prefix + "CLASS " + this.name + " (" + staticString + ")");
        functionsTable.print(prefix + "   ");
    }
}