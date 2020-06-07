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
    public void print(String prefix) {
        System.out.println("");
    }
}