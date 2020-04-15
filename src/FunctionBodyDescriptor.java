
public class FunctionBodyDescriptor extends Descriptor {
    
    // Local table: parent = parameter table
    protected SymbolsTable localTable;

    public FunctionBodyDescriptor(SymbolsTable parameterTable){
        localTable = new SymbolsTable();
        localTable.setParent(parameterTable);
    }

    public SymbolsTable getLocalTable() {
        return this.localTable;
    }
    
    public void addSymbol(String name, Descriptor descriptor){
        this.localTable.addSymbol(name, descriptor);
    }

    /*
    public void setSymbolTable(SymbolsTable localTable){
        this.localTable = localTable;
    }
    */
}