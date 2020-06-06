package symbols;

public class ConstantInt extends ConstantDescriptor {
    private int value;
    
    public ConstantInt(int value) {
        super(true);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}