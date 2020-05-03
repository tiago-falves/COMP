package symbols;

public class NamedTypeDescriptor extends TypeDescriptor {
    protected String name;
    public NamedTypeDescriptor(Type type) {
        super(type);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    @Override
    void print(String prefix) {

    }
}
