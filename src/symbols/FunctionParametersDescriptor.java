package symbols;

public class FunctionParametersDescriptor {
    protected SymbolsTable parametersTable;

    public FunctionParametersDescriptor(){
        parametersTable = new SymbolsTable();
    }

    public SymbolsTable getParametersTable() {
        return this.parametersTable;
    }

    public void addSymbol(String name, Descriptor descriptor){
        this.parametersTable.addSymbol(name, descriptor);
    }

}