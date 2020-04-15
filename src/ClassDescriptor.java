public class ClassDescriptor extends Descriptor{
    
    // Local table: parente = parameter table
    protected SymbolsTable functionsTable;
    
    private boolean isStatic;
    private String access;

    public ClassDescriptor(String access, Boolean isStatic){
        this.access = access;
        this.isStatic = isStatic;
        this.functionsTable = new SymbolsTable();
    }

    public boolean isStatic(){
        return this.isStatic;
    }
    public String getAccess(){
        return access;
    }

}