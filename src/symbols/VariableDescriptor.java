package symbols;

public class VariableDescriptor extends NamedTypeDescriptor{
    boolean isInitialized;
    boolean isInitializedInIf;

    public VariableDescriptor(Type type) {
        super(type);
        
        this.isInitialized = false;
        this.isInitializedInIf = false;
    }

    public VariableDescriptor() {
        super(Type.VOID);
    }

    public void setInitialized() {
        this.isInitialized = true;
    }

    public void setInitializedInIf() {
        this.isInitializedInIf = true;
    }

    public boolean isInitialized(){
        return isInitialized;
    }

    public boolean isInitializedInIf(){
        return isInitializedInIf;
    }

    public void print(String prefix) {
        String initializedString;
        if (this.isInitialized) initializedString = "init";
        else initializedString = "non-init";

        System.out.println(prefix + name + " --> " + this.type + " (" + initializedString + ")");
    }
    
}