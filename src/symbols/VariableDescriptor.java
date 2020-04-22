package symbols;

public class VariableDescriptor extends Descriptor{
    Type type;
    boolean isInitialized;
    String name;

    public void setType(Type type) {
        this.type = type;
    }
    public void setInitialized(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    public Type getType(){
        return this.type;
    }
    public boolean isInitialized(){
        return isInitialized;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void print(String prefix) {
        String initializedString;
        if (this.isInitialized) initializedString = "init";
        else initializedString = "non-init";

        System.out.println(prefix + name + " --> " + this.type + " (" + initializedString + ")");
    }
    
}