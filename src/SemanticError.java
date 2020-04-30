public class SemanticError {

    private final int maxNumErrors;

    private int numErrors;

    public SemanticError(){
        this(10);
    }

    public SemanticError(int maxNumErrors){
        this.maxNumErrors = maxNumErrors;
        this.numErrors = 0;
    }

    private String getLineMessage(SimpleNode node){
        if(node.jjtGetLine() == -1){
            if(node.jjtGetNumChildren() == 0)
                return "";
            return getLineMessage((SimpleNode)node.jjtGetChild(0));
        }

        return "at line " + node.jjtGetLine();
    }

    private String getColumnMessage(SimpleNode node){
        if(node.jjtGetColumn() == -1){
            if(node.jjtGetNumChildren() == 0) 
                return "";
            return getColumnMessage((SimpleNode)node.jjtGetChild(0));
        }
        return ", column " + node.jjtGetColumn();
    }

    private String getPositionMessage(SimpleNode node){
        return getLineMessage(node) + getColumnMessage(node);
    }

    public void printError(SimpleNode node, String message) throws SemanticErrorException {
        this.numErrors++;
        System.err.println("ERROR " + getPositionMessage(node) + ": " + message + "\n");
        
        if(numErrors >= maxNumErrors)
            throw new SemanticErrorException("Reached maximum number of semantic errors");
    }

    public int getNumErrors() {
        return numErrors;
    }
}