
public class VariableDescriptor extends Descriptor{
    String type;
    boolean isInitialized;

    public VariableDescriptor(String name, String type) {
        super(name);
        this.type = type;

    }
    public String getType(){
        return this.type;
    }
    public boolean isInitialized(){
        return isInitialized;
    }
    
}