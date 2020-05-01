package llir;

import symbols.ClassDescriptor;
import symbols.NamedTypeDescriptor;
import symbols.Type;
import symbols.VariableDescriptor;

public class LLIRClassVariable extends LLIRExpression {
    private ClassDescriptor classDescriptor;

    public LLIRClassVariable(ClassDescriptor variable) {
        this.classDescriptor = variable;
    }

    public LLIRClassVariable() {
        this.classDescriptor= new ClassDescriptor();
    }

    public ClassDescriptor getClassDescriptor(){
        return classDescriptor;
    }
}