package symbols;

public class BlockDescriptor extends Descriptor {
    
    // Local table: parent = header table
    protected SymbolsTable blockTable;
    
    public BlockDescriptor(SymbolsTable headerTable){
        blockTable = new SymbolsTable();
        blockTable.setParent(headerTable);
    }

    public SymbolsTable getLocalTable() {
        return this.blockTable;
    }

    public void setLocalTable(SymbolsTable blockTable) {
        this.blockTable = blockTable;
    }
    
    public void addSymbol(String name, Descriptor descriptor){
        this.blockTable.addSymbol(name, descriptor);
    }

    public void print(String prefix) {
        System.out.println("");
    }
}