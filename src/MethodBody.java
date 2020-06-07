import symbols.FunctionDescriptor;
import symbols.VariableDescriptor;

public class MethodBody {
    private SimpleNode functionNode;
    private FunctionDescriptor functionDescriptor;

    public MethodBody(SimpleNode functionNode, FunctionDescriptor functionDescriptor){
        this.functionNode = functionNode;
        this.functionDescriptor = functionDescriptor;
    }

    public void print(){
        System.out.println("\n\nMAIN BODY");
        functionNode.dump("");
    }
}
