package symbols;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class SymbolsTable {
    private LinkedHashMap<String, List<Descriptor>> table;
    private SymbolsTable parent;

    public SymbolsTable() {
        this.parent = null;
        table = new LinkedHashMap<String, List<Descriptor>>();
    }

    public SymbolsTable getParent() {
        return this.parent;
    }

    public void setParent(SymbolsTable parent) {
        this.parent = parent;
    }

    public boolean addSymbol(String name, Descriptor descriptor){
        return addSymbol(name, descriptor, true);
    }

    public boolean addSymbol(String name, Descriptor descriptor, boolean allowsDuplicates) {
        if (table.containsKey(name)) {
            if(allowsDuplicates){
                List<Descriptor> descriptorList = table.get(name);
                descriptorList.add(descriptor);
                return true;
            }

            System.out.println("A symbol with that identifier already exists.");
            return false;
        }

        List<Descriptor> descriptors = new LinkedList<>();
        descriptors.add(descriptor);
        
        table.put(name, descriptors);
        return true;
    }

    public List<Descriptor> getDescriptor(String identifier){
        if(table.containsKey(identifier)){
            return table.get(identifier);
        }
        
        if(parent != null){
            return parent.getDescriptor(identifier);
        }

        return null;
    }
}