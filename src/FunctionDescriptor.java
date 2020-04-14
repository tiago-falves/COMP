public class FunctionDescriptor extends Descriptor{
    protected FunctionParameterDescriptor parameterDescriptor;
    private String returnValue;
    private boolean isStatic;
    private String access;


    public FunctionDescriptor(String name,String access){
        super(name);
        this.isStatic= false;
        this.access = access;

    }
    public FunctionParameterDescriptor getparameterDescriptor() {
        return this.parameterDescriptor;
    }
    public void setparameterDescriptor(FunctionParameterDescriptor parameterDescriptor){
        this.parameterDescriptor = parameterDescriptor;
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
    public String getAccess(){
        return access;
    }
    public void setAccess(String access){
        this.access = access;
    }

}