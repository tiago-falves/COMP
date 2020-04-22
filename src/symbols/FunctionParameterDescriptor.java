package symbols;

public class FunctionParameterDescriptor extends TypeDescriptor{
    private String name;


    public FunctionParameterDescriptor(String name, Type type) {
        super(type);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    //protected SymbolsTable parametersTable;

    /*public FunctionParameterDescriptor(){
        parametersTable = new SymbolsTable();
    }

    public SymbolsTable getParameterTable() {
        return this.parametersTable;
    }

    public void addSymbol(String name, Descriptor descriptor){
        this.parameterTable.addSymbol(name, descriptor);
    }*/

}