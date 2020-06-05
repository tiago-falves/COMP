package symbols;

public class NamedTypeDescriptor extends TypeDescriptor {
    protected String name;
    ConstantDescriptor constantDescriptor;

    public NamedTypeDescriptor(Type type) {
        super(type);
        constantDescriptor = new ConstantDescriptor();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setConstantDescriptor(ConstantDescriptor constantDescriptor) {
        this.constantDescriptor = constantDescriptor;
    }

    public ConstantDescriptor getConstantDescriptor() {
        return this.constantDescriptor;
    }

    @Override
    void print(String prefix) {

    }
}
