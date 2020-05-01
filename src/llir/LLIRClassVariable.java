package llir;

import symbols.ClassDescriptor;

public class LLIRClassVariable extends LLIRExpression {
    private ClassDescriptor classDescriptor;
    private String name;

    public LLIRClassVariable(ClassDescriptor variable) {
        this.classDescriptor = variable;
    }

    public LLIRClassVariable() {
        this.classDescriptor= new ClassDescriptor();
        this.name ="";
    }

    public ClassDescriptor getClassDescriptor(){
        return classDescriptor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}