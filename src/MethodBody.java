import symbols.FunctionDescriptor;
import symbols.VariableDescriptor;

public class MethodBody {
    private SimpleNode functionNode;
    private FunctionDescriptor functionDescriptor;

    public MethodBody(SimpleNode functionNode, FunctionDescriptor functionDescriptor){
        this.functionNode = functionNode;
        this.functionDescriptor = functionDescriptor;
        if(functionNode.getId() ==JavammTreeConstants.JJTMETHODDECLARATION) build();
        else if(functionNode.getId() ==JavammTreeConstants.JJTMAINDECLARATION) System.out.println("\nMain por fazer");


    }

    public void print(){
        System.out.println("\n\nMAIN BODY");
        functionNode.dump("");

    }

    public void build(){
        for (int j = 0; j < this.functionNode.jjtGetNumChildren(); j++) {
            SimpleNode child = (SimpleNode) this.functionNode.jjtGetChild(j);
            switch (child.getId()) {
                case JavammTreeConstants.JJTVARIABLEDECLARATION:
                    //System.out.println("Var Declaration");
                    break;
                case JavammTreeConstants.JJTLINESTATEMENT:
                    //System.out.println("Line Statement");
                case JavammTreeConstants.JJTWHILESTATEMENT:
                    //System.out.println("While");
                case  JavammTreeConstants.JJTIFSTATEMENT:
                    //System.out.println("IF");
                case JavammTreeConstants.JJTRETURN:
                    //System.out.println("Return");
                case JavammTreeConstants.JJTMETHODHEADER:
                    //System.out.println("Method Header");

                default:
                    //System.out.println("Falta tratar deste caso: " + child.getId());
            }
        }

    }
}
