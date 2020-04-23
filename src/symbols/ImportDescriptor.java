package symbols;

import java.util.ArrayList;

public class ImportDescriptor extends TypeDescriptor {
    boolean isStatic;
    ArrayList<String> identifiers;
    ArrayList<Type> parameters;
    Type returnType;

    public ImportDescriptor() {
        super(null);
        this.isStatic = false;
        this.identifiers = new ArrayList();
        this.parameters = new ArrayList();
        this.returnType = null;

    }

    // Identifiers
    public ArrayList<String> getIdentifiers() {
        return this.identifiers;
    }

    public String getLastIdentifier() {
        return this.identifiers.get(this.identifiers.size() - 1);
    }

    public void setIdentifiers(ArrayList<String> identifiers) {
        this.identifiers = identifiers;
    }

    public void addIdentifier(String identifier) {
        this.identifiers.add(identifier);
    }


    // Parameters 
    public boolean hasParameters() {
        return this.parameters.size() != 0;
    }

    public ArrayList<Type> getParameters() {
        return this.parameters;
    }

    public void setParameters(ArrayList<Type> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Type parameter) {
        this.parameters.add(parameter);
    }


    // Static
    public void makeStatic() {
        this.isStatic = true;
    }

    public boolean isStatic() {
        return this.isStatic;
    }


    // Return
    public boolean hasReturn() {
        return this.type != null;
    }

    public Type getReturn() {
        return this.returnType;
    }

    public void setReturn(Type returnType) {
        this.returnType = returnType;
    }

    public void print(String prefix) {
        String newPrefix = prefix + "   ";

        String staticString;
        if(isStatic) staticString = "static";
        else staticString = "non-static";

        StringBuilder identifierBuilder = new StringBuilder();
        for(String identifier : identifiers) {
            identifierBuilder.append(identifier);
        }

        // printing the import name
        System.out.println(prefix + "IMPORT " + identifierBuilder.toString() + " (" + staticString + ")");

        // printing return value
        System.out.println(newPrefix + "Return:\n" + newPrefix + "   " + this.returnType);

        // printing parameters
        if(parameters.size() > 0) {
            System.out.println(newPrefix + "Parameters:");
            for(Type paramater : this.parameters) {
                System.out.println(newPrefix + "   " + paramater);
            }
        } else {
            System.out.println(newPrefix + "Parameters:");
            System.out.println(newPrefix + "   " + "None");
        }


        System.out.println("");
    }
}