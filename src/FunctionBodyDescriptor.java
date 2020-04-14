
public class FunctionBodyDescriptor extends FunctionParameterDescriptor{
    protected SymbolsTable symbolTable;


    public FunctionBodyDescriptor(String name,String type){
        super(name,type);
    }
    public SymbolsTable getSymbolTable() {
        return this.symbolTable;
    }
    public void setSymbolTable(SymbolsTable symbolTable){
        this.symbolTable = symbolTable;
    }

}