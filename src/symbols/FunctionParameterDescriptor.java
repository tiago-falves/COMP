package symbols;

public class FunctionParameterDescriptor extends NamedTypeDescriptor{


    public FunctionParameterDescriptor(String name, Type type) {
        super(type);
        this.name = name;
    }

    public void print(String prefix) {
        System.out.println(prefix + name + " --> " + this.type);
    }


}