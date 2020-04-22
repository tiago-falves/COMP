package symbols;

public class FunctionParameterDescriptor extends Descriptor{
    private String name;
    private Type type;

    public FunctionParameterDescriptor(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void print(String prefix) {
        System.out.println(prefix + name + " --> " + this.type);
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