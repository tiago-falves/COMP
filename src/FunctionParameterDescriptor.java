
public class FunctionParameterDescriptor extends FunctionDescriptor{
    protected SymbolsTable symbolTable;
    private FunctionBodyDescriptor bodyDescriptor;


    public FunctionParameterDescriptor(String name,String type){
        super(name,type);
    }
    public SymbolsTable getSymbolTable() {
        return this.symbolTable;
    }
    public void setSymbolTable(SymbolsTable symbolTable){
        this.symbolTable = symbolTable;
    }

    public FunctionBodyDescriptor getbodyDescriptor() {
        return this.bodyDescriptor;
    }
    public void setbodyDescriptor(FunctionBodyDescriptor bodyDescriptor){
        this.bodyDescriptor = bodyDescriptor;
    }
}