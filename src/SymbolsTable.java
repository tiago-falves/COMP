import java.util.LinkedHashMap;

public class SymbolsTable {
    private LinkedHashMap<String, Descriptor> table;
    private SymbolsTable parent;

    public SymbolsTable() {
        this.parent = null;
        table = new LinkedHashMap<String, Descriptor>();
    }

    public SymbolsTable getParent() {
        return this.parent;
    }

    public void setParent(SymbolsTable parent) {
        this.parent = parent;
    }

    public boolean addSymbol(String name, Descriptor descriptor) {
        if (table.containsKey(name)) {
            System.out.println("A symbol with that identifier already exists.");
            return false;
        }
        table.put(name, descriptor);
        return true;
    }

    public Descriptor getDescriptor(String identifier){
        if(table.containsKey(identifier)){
            return table.get(identifier);
        }
        
        if(parent != null){
            return parent.getDescriptor(identifier);
        }

        return null;
    }
}