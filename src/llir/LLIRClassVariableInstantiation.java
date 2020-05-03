package llir;

import symbols.ClassDescriptor;
import symbols.NamedTypeDescriptor;
import symbols.Type;
import symbols.VariableDescriptor;

public class LLIRClassVariableInstantiation extends LLIRExpression {
    private ClassDescriptor classDescriptor;

    public LLIRClassVariableInstantiation(ClassDescriptor variable) {
        this.classDescriptor = variable;
    }

    public LLIRClassVariableInstantiation() {
        this.classDescriptor= new ClassDescriptor();
    }

    public ClassDescriptor getClassDescriptor(){
        return classDescriptor;
    }
}