package symbols;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public int getSize() {
        return table.size();
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

    public void print(String prefix) {
        /*
        if(this.parent != null) 
            this.parent.print(prefix);
        */    

        for(Map.Entry<String, Descriptor> entry : table.entrySet()) {
            String name = entry.getKey();
            Descriptor descriptor = entry.getValue();

            if(descriptor.getClass().getName().equals("symbols.ClassDescriptor")) {
                descriptor.print(prefix);
            } 
            else if(descriptor.getClass().getName().equals("symbols.FunctionDescriptor")) {
                descriptor.print(prefix);
            }
            else if(descriptor.getClass().getName().equals("symbols.FunctionParameterDescriptor")) {
                descriptor.print(prefix);
            }     
            else if(descriptor.getClass().getName().equals("symbols.VariableDescriptor")) {
                descriptor.print(prefix);
            }     
            else if(descriptor.getClass().getName().equals("symbols.ImportDescriptor")) {
                descriptor.print(prefix);
            }                 
            else {
                System.out.println(prefix + name + " ---> " + descriptor.getClass().getName());
            }

        }
    }
}