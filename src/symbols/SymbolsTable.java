package symbols;

import java.util.LinkedHashMap;
import java.util.Map;
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

    public int getSize() {
        return table.size();
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
            return false;
        }

        List<Descriptor> descriptors = new LinkedList<>();
        descriptors.add(descriptor);
        table.put(name, descriptors);
        return true;
    }

    public List<Descriptor> getDescriptor(String identifier){
        List<Descriptor> descriptors = null;
        if(table.containsKey(identifier)){
            descriptors = new LinkedList<Descriptor>(table.get(identifier));
        }
        
        if(parent != null){
            List<Descriptor> parentDescriptors = parent.getDescriptor(identifier);
            if(parentDescriptors != null){
                if(descriptors == null){
                    descriptors = parentDescriptors;
                }else{
                    for(int i = 0; i < parentDescriptors.size(); i++){
                        descriptors.add(parentDescriptors.get(i));
                    }
                }
            }
        }

        return descriptors;
    }

    public LinkedHashMap<String, List<Descriptor>> getTable(){
        return this.table;
    }

    public void print(String prefix) {
        /*
        if(this.parent != null) 
            this.parent.print(prefix);
        */    

        for(Map.Entry<String, List<Descriptor>> entry : table.entrySet()) {
            String name = entry.getKey();

            for(Descriptor descriptor : entry.getValue()) {

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
}