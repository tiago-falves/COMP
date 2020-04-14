public class ClassDescriptor extends Descriptor{
    protected SymbolsTable symbolTable;
    private boolean isStatic;
    private String access;

    public ClassDescriptor(String name,String access,Boolean isStatic){
        super(name);
        this.access = access;
        this.isStatic = isStatic;
    }

    public boolean isStatic(){
        return this.isStatic;
    }
    public String getAccess(){
        return access;
    }

}