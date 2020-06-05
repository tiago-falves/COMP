package symbols;

public class ConstantBoolean extends ConstantDescriptor {
    private boolean value;
    
    public ConstantBoolean(boolean value) {
        super(true);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}