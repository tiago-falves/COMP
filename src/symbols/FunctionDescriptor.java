package symbols;

public class FunctionDescriptor extends Descriptor {
    
    protected FunctionBodyDescriptor bodyDescriptor;
    private String returnValue;
    private boolean isStatic;
    private Access access;

    public FunctionDescriptor(Access access){
        this.isStatic= false;
        this.access = access;
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
    
    public boolean isStatic(){
        return this.isStatic;
    }
    
    public Access getAccess(){
        return access;
    }
    
    public void setAccess(Access access){
        this.access = access;
    }

}