
public class VariableDescriptor extends Descriptor{
    Type type;
    boolean isInitialized;

    public VariableDescriptor(Type type) {
        this.type = type;

    }
    public Type getType(){
        return this.type;
    }
    public boolean isInitialized(){
        return isInitialized;
    }
    
}