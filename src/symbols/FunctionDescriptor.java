package symbols;

public class FunctionDescriptor extends Descriptor {
    
    protected FunctionBodyDescriptor bodyDescriptor;
    private String returnValue;
    private boolean isStatic;
    private Access access;
    private String name;

    public FunctionDescriptor(){
        this.isStatic = false;
        this.access = Access.DEFAULT;
    }

    public FunctionBodyDescriptor getparameterDescriptor() {
        return this.bodyDescriptor;
    }
    
    public void setparameterDescriptor(FunctionBodyDescriptor bodyDescriptor){
        this.bodyDescriptor = bodyDescriptor;
    }

    public String getReturnValue() {
        return this.returnValue;
    }
    
    public void setReturnValue(String returnValue){
        this.returnValue = returnValue;
    }

    public void makeStatic() {
        this.isStatic = true;
    }
    
    public boolean isStatic(){
        return this.isStatic;
    }
    
    public Access getAccess(){
        return access;
    }
    
    public void setAccess(Access access){
        this.access = access;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

}