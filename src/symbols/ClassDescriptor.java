package symbols;

public class ClassDescriptor extends Descriptor {
    
    protected SymbolsTable classVariablesTable;
    protected SymbolsTable functionsTable;
    private String name;
    private boolean isStatic;
    private Access access;
    private String parentClass;

    public ClassDescriptor() {
        this.access = Access.DEFAULT;
        this.parentClass="";
        this.isStatic = false;
        this.classVariablesTable = new SymbolsTable();
        this.functionsTable = new SymbolsTable();
        this.functionsTable.setParent(this.classVariablesTable);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public boolean isStatic(){
        return this.isStatic;
    }

    public void addVariable(VariableDescriptor variable) {
        SymbolsTable variablesTable = this.classVariablesTable;
        if (variablesTable == null) {
            variablesTable = new SymbolsTable();
        }
        variablesTable.addSymbol(variable.getName(), variable, false);
    }

    public SymbolsTable getVariablesTable(){
        return this.classVariablesTable;
    }
    
    public SymbolsTable getFunctionsTable(){
        return this.functionsTable;
    }

    public void addFunction(String name,FunctionDescriptor function) {
        functionsTable.addSymbol(name, function);
    }

    public void print(String prefix) {
        String staticString;
        if(isStatic) staticString = "static";
        else staticString = "non-static";

        System.out.println(prefix + "CLASS " + this.name + " (" + staticString + ")");
        functionsTable.print(prefix + "   ");
    }
}