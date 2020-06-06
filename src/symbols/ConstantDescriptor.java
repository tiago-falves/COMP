package symbols;

public class ConstantDescriptor {
    protected boolean isConstant;
    protected boolean isSimple;

    public ConstantDescriptor() {
        this.isConstant = false;
        this.isSimple = false;
    }

    public ConstantDescriptor(boolean isSimple) {
        this.isConstant = false;
        this.isSimple = isSimple;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public void setConstant(boolean isConstant) {
        this.isConstant = isConstant;
    }
    
    public boolean isSimple() {
        return isSimple;
    }

}