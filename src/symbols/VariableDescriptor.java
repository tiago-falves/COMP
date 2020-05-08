package symbols;

public class VariableDescriptor extends NamedTypeDescriptor{
    boolean isInitialized;
    boolean initializedInIfClause;

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

    public void setInitializedInIfClause() {
        this.initializedInIfClause = true;
    }

    public boolean isInitialized(){
        return isInitialized;
    }

    public boolean isInitializedInIfClause(){
        return initializedInIfClause;
    }

    public void print(String prefix) {
        String initializedString;
        if (this.isInitialized) initializedString = "init";
        else initializedString = "non-init";

        System.out.println(prefix + name + " --> " + this.type + " (" + initializedString + ")");
    }
    
}