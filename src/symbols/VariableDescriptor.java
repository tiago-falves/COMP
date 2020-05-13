package symbols;

public class VariableDescriptor extends NamedTypeDescriptor{
    boolean isInitialized;
    boolean isInitializedInIf;
    boolean isInitializedInFunction;
    boolean wasInitializedPreviously;

    public VariableDescriptor(Type type) {
        super(type);
        
        this.isInitialized = false;
        this.isInitializedInIf = false;
        this.isInitializedInFunction = true;
        this.wasInitializedPreviously = false;
    }

    public VariableDescriptor() {
        super(Type.VOID);
        this.isInitializedInIf = false;
        this.isInitializedInFunction = true;
        this.wasInitializedPreviously = false;
    }

    public void setInitialized() {
        this.isInitialized = true;
    }

    public void setNonInitialized() {
        this.isInitialized = false;
        setNonInitializedPreviously();
    }

    public void setInitializedInIf() {
        this.isInitializedInIf = true;
    }

    public void setInitializedInFunction() {
        this.isInitializedInFunction = true;
    }

    public void setNonInitializedInFunction() {
        this.isInitializedInFunction = false;
    }

    public void setInitializedPreviously() {
        this.wasInitializedPreviously = true;
    }

    public void setNonInitializedPreviously() {
        this.wasInitializedPreviously = false;
    }

    public boolean isInitialized(){
        return isInitialized;
    }

    public boolean isInitializedInIf(){
        return isInitializedInIf;
    }

    public boolean isInitializedInFunction(){
        return isInitializedInFunction;
    }

    public boolean wasInitializedPreviously(){
        return wasInitializedPreviously;
    }

    public void print(String prefix) {
        String initializedString;
        if (this.isInitialized) initializedString = "init";
        else initializedString = "non-init";

        System.out.println(prefix + name + " --> " + this.type + " (" + initializedString + ")");
    }
    
}