package symbols;

public abstract class TypeDescriptor extends Descriptor {
    protected Type type;

    public TypeDescriptor(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
