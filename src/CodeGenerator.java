import symbols.ClassDescriptor;
import symbols.Descriptor;
import symbols.ImportDescriptor;
import codeGeneration.CodeGeneratorConstants;


public class CodeGenerator {
    public CodeGenerator(SimpleNode rootNode){
        int i;
        i = 0;
        while(i < rootNode.jjtGetNumChildren()) {
            SimpleNode currentNode = (SimpleNode) rootNode.jjtGetChild(i);
            Descriptor descriptor =  currentNode.getDescriptor();
            if (descriptor instanceof ImportDescriptor){
                System.out.println("Import");
            } else if (descriptor instanceof ClassDescriptor){
                System.out.println("Class");
            }
            i ++;
        }

    }
}
