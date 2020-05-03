package symbols;

public class VariableDescriptor extends NamedTypeDescriptor{
    boolean isInitialized;

    public VariableDescriptor(Type type) {
        super(type);
        
        this.isInitialized = false;
    }

    public VariableDescriptor() {
        super(Type.VOID);
    }

    public void setInitialized() {
        this.isInitialized = true;
    }

    public boolean isInitialized(){
        return isInitialized;
    }



    public void print(String prefix) {
        String initializedString;
        if (this.isInitialized) initializedString = "init";
        else initializedString = "non-init";

        System.out.println(prefix + name + " --> " + this.type + " (" + initializedString + ")");
    }
    
}