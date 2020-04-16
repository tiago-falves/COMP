package symbols;

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
    public boolean addSymbol(VariableDescriptor descriptor) { //Alterar
        if (table.containsKey(descriptor.getName())) {
            System.out.println("A symbol with that identifier already exists.");
            return false;
        }
        table.put(descriptor.getName(), descriptor);
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

    public boolean symbolDefined(String identifier){
        boolean containsKey = table.containsKey(identifier);

        if(!containsKey) {
            if(this.parent == null){
                return false;
            } else
                return this.parent.symbolDefined(identifier);
        } else
            return true;
    }

    @Override
    public String toString() {
        String res = "Table:\n";
        for (String s : table.keySet()) {
            res = res.concat(table.get(s).toString() + "\n");
        }
        return res;
    }
}