package symbols;

public class VariableDescriptor extends Descriptor{
    Type type;
    boolean isInitialized;
    String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public VariableDescriptor(Type type) {
        this.type = type;
    }
    public VariableDescriptor() {

    }
    public Type getType(){
        return this.type;
    }
    public void setType(Type type){
         this.type = type ;
    }
    public boolean isInitialized(){
        return isInitialized;
    }

    @Override
    public String toString() {
        String res = "Variable:\n" + type + " " + name;

        return res;
    }
    
}