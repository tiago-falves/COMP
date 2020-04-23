import javax.management.remote.JMXConnectorServerFactory;

import symbols.*;

import java.util.List;
import java.util.ArrayList;

public class TableGenerator {
    SimpleNode rootNode;
    SymbolsTable symbolsTable;

    public TableGenerator(SimpleNode rootNode) {
        this.rootNode = rootNode;
        this.symbolsTable = new SymbolsTable();
    }

    public SymbolsTable getTable() {
        return this.symbolsTable;
    }

    public void build() {
        int i = 0;
        // System.out.println(rootNode.jjtGetNumChildren());
        while(i < rootNode.jjtGetNumChildren()) {
            SimpleNode currentNode = (SimpleNode) rootNode.jjtGetChild(i);

            switch(currentNode.getId()) {
                case JavammTreeConstants.JJTIMPORTDECLARATION:
                    ImportDescriptor importDescriptor = inspectImport(currentNode);
                    symbolsTable.addSymbol(importDescriptor.getLastIdentifier(), importDescriptor);
                    break;

                case JavammTreeConstants.JJTCLASSDECLARATION:
                    SimpleNode childNode = (SimpleNode) currentNode.jjtGetChild(0);
                    ClassDescriptor classDescriptor = inspectClass(currentNode);
                    classDescriptor.setName(childNode.jjtGetVal());
                    symbolsTable.addSymbol(classDescriptor.getName(), classDescriptor, false);
                    break;
            }

            i++;
        }
    }

    public ImportDescriptor inspectImport(SimpleNode importNode) {
        ImportDescriptor importDescriptor = new ImportDescriptor();

        // check if static
        if(importNode.jjtGetChild(0).getId() == JavammTreeConstants.JJTSTATIC) {
            importDescriptor.makeStatic();
        }

        // collect identifiers, parameter types and return type
        for (int i = 0; i < importNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) importNode.jjtGetChild(i);

            if(child.getId() == JavammTreeConstants.JJTIDENTIFIER) {
                importDescriptor.addIdentifier(child.jjtGetVal());
            }
            else if (child.getId() == JavammTreeConstants.JJTIMPORTARGS) {
                
                for(int j = 0; j < child.jjtGetNumChildren(); j++) {
                    SimpleNode grandChild = (SimpleNode) child.jjtGetChild(j);
                    TypeString typeString = new TypeString(grandChild.jjtGetVal());
                    importDescriptor.addParameter(typeString.parseType());
                }
            }
            else if (child.getId() == JavammTreeConstants.JJTRETURNIMPORT) {
                if (child.jjtGetNumChildren() == 0) {
                    importDescriptor.setReturn(Type.VOID);
                    continue;
                }
                SimpleNode grandChild = (SimpleNode) child.jjtGetChild(0);
                TypeString typeString = new TypeString(grandChild.jjtGetVal());
                importDescriptor.setType(typeString.parseType());
            }
        }

        return importDescriptor;
    }

    public ClassDescriptor inspectClass(SimpleNode classNode) {
        ClassDescriptor classDescriptor = new ClassDescriptor();

        // collect identifiers, parameter types and return type
        for (int i = 0; i < classNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) classNode.jjtGetChild(i);
            if (child.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
                VariableDescriptor variableDescriptor = inspectVariable(child);
                classDescriptor.addVariable(variableDescriptor);
            }
            else if (child.getId() == JavammTreeConstants.JJTMETHODDECLARATION) {
                FunctionDescriptor functionDescriptor = inspectMethodHeader(child);
                classDescriptor.addMethod(functionDescriptor.getName(),functionDescriptor);
            }
        }

        return classDescriptor;
    }

    public VariableDescriptor inspectVariable(SimpleNode variableNode) {
        VariableDescriptor variableDescriptor = new VariableDescriptor();

        for (int i = 0; i < variableNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) variableNode.jjtGetChild(i);

            if (child.getId() == JavammTreeConstants.JJTTYPE) {
                TypeString typeString = new TypeString(child.jjtGetVal());
                variableDescriptor.setType(typeString.parseType());
            }else if(child.getId() == JavammTreeConstants.JJTCLASSTYPE){
                variableDescriptor.setType(Type.CLASS);
                variableDescriptor.setClassName(child.jjtGetVal());
            }else if (child.getId() == JavammTreeConstants.JJTVARIABLENAME) {
                String name = child.jjtGetVal();
                variableDescriptor.setName(name);
            }
        }

        return variableDescriptor;
    }

    public FunctionDescriptor inspectMethodHeader(SimpleNode methodNode) {
        SimpleNode child = (SimpleNode) methodNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            return inspectMainDeclaration(child);
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            return inspectMethod(methodNode);
        }
        return null;
    }

    public FunctionDescriptor inspectMainDeclaration(SimpleNode mainNode) {
        FunctionDescriptor functionDescriptor = new FunctionDescriptor();
        functionDescriptor.makeStatic();
        functionDescriptor.setReturnValue("void");
        functionDescriptor.setName("main");

        for (int i = 0; i < mainNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) mainNode.jjtGetChild(i);

            if (child.getId() == JavammTreeConstants.JJTMAINARGS) {

                for(int j = 0; j < child.jjtGetNumChildren(); j+=2) {
                    SimpleNode grandChildType = (SimpleNode) child.jjtGetChild(j);
                    SimpleNode grandChildName;
                    if (j+1 < child.jjtGetNumChildren()) {
                        grandChildName = (SimpleNode) child.jjtGetChild(j+1);
                        if (grandChildType.getId() == JavammTreeConstants.JJTTYPE && grandChildName.getId() == JavammTreeConstants.JJTIDENTIFIER) {
                            TypeString typeString = new TypeString(grandChildType.jjtGetVal());
                            String name = grandChildName.jjtGetVal();
                            FunctionParameterDescriptor parameterDescriptor = new FunctionParameterDescriptor(name, typeString.parseType());
                            functionDescriptor.addParameter(parameterDescriptor);
                        }
                    }
                }

            }
            else inspectVariableAndStatement(child, functionDescriptor);
        }

        return functionDescriptor;
    }

    //Inspects all the methods except main
    public FunctionDescriptor inspectMethod(SimpleNode methodNode) {

        FunctionDescriptor functionDescriptor = new FunctionDescriptor();

        //Cycle can have MethodHeader, LineStatement return IntegralLiteral
        for (int i = 0; i < methodNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) methodNode.jjtGetChild(i);
            
            //Method Header Parser
            if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
                for (int j = 0; j < child.jjtGetNumChildren(); j++) {
                    SimpleNode grandChild = (SimpleNode) child.jjtGetChild(j);
                    //Tem 0 childs
                    if(grandChild.getId() == JavammTreeConstants.JJTMODIFIER){
                        functionDescriptor.setAccessVal(grandChild.val);
                    }
                    else if(grandChild.getId() == JavammTreeConstants.JJTTYPE){
                        functionDescriptor.setReturnValue(grandChild.val);
                    }
                    else if(grandChild.getId() == JavammTreeConstants.JJTIDENTIFIER){
                        functionDescriptor.setName(grandChild.val);
                    }
                    else if(grandChild.getId() == JavammTreeConstants.JJTMETHODARGUMENTS){
                        inspectMethodArguments(grandChild,functionDescriptor.getParametersTable());
                    }
                }

            } 
            else if (child.getId() == JavammTreeConstants.JJTRETURN){
                SimpleNode actualReturn = (SimpleNode) child.jjtGetChild(0);
                functionDescriptor.setActualReturnValue(actualReturn.val);


            }
            else inspectVariableAndStatement(child, functionDescriptor);
        }

        return functionDescriptor;

    }

    public void inspectMethodArguments(SimpleNode methodArgumentsNode,SymbolsTable parametersTable) {

        for (int i = 0; i < methodArgumentsNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) methodArgumentsNode.jjtGetChild(i);
            String name = "";
            Type type = Type.VOID;

            //Method Header Parser
            if (child.getId() == JavammTreeConstants.JJTMETHODARGUMENT) {
                for (int j = 0; j < child.jjtGetNumChildren() ; j++) {

                    SimpleNode grandChild = (SimpleNode) child.jjtGetChild(j);

                    if (grandChild.getId() == JavammTreeConstants.JJTTYPE) {
                        TypeString typeString = new TypeString(grandChild.val);
                        type = typeString.parseType();
                    } else if (grandChild.getId() == JavammTreeConstants.JJTIDENTIFIER) {
                        name = grandChild.val;
                    }
                }
            }
            FunctionParameterDescriptor parameter = new FunctionParameterDescriptor(name,type);
            parametersTable.addSymbol(name, parameter, false);

        }


    }

    public void inspectVariableAndStatement(SimpleNode variableAndStatementNode, FunctionDescriptor functionDescriptor) {
        if (variableAndStatementNode.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
            VariableDescriptor variableDescriptor = inspectVariable(variableAndStatementNode);
            functionDescriptor.addBodyVariable(variableDescriptor.getName(),variableDescriptor);
        }else if (variableAndStatementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
            //TODO: Analizar statement
            inspectLineStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }/*else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT || variableAndStatementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
            inspectBlockStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }else{
            System.err.println("Error: Unknown symbol");
        }*/
    }

    public void inspectBlockStatement(SimpleNode statementNode, SymbolsTable statementParent){

        /*BlockDescriptor blockDescriptor = new BlockDescriptor(statementParent);
        System.out.println("Cheguei ao statement");

        for (int j = 0; j < statementNode.jjtGetNumChildren() ; j++) {

            SimpleNode child = (SimpleNode) statementNode.jjtGetChild(j);

            if (child.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
                VariableDescriptor variableDescriptor = inspectVariable(child);
                blockDescriptor.addSymbol(variableDescriptor.getName(),variableDescriptor);
                System.out.println("Var declaration \n");

            } 
            else if( child.getId() == JavammTreeConstants.JJTWHILESTATEMENT || child.getId() == JavammTreeConstants.JJTIFSTATEMENT){
                inspectBlockStatement(child,blockDescriptor.getLocalTable());
                System.out.println("Mais um statement\n");

            }else if(child.getId() == JavammTreeConstants.JJTLINESTATEMENT){
                inspectLineStatement(statementNode, blockDescriptor.getLocalTable());
            }else{
                System.err.println("Error: Unknown symbol");
            }
        }*/

    }

    public void inspectLineStatement(SimpleNode statementNode, SymbolsTable symbolTable ){
        if(statementNode.jjtGetNumChildren() < 3){
            System.err.println("Error: Invalid Line Statement");
            return;
        }

        SimpleNode firstChild = (SimpleNode) statementNode.jjtGetChild(0);
        
        if(firstChild.getId() != JavammTreeConstants.JJTIDENTIFIER && firstChild.getId() != JavammTreeConstants.JJTTHIS){
            System.err.println("Error: First Node of Line Statement must be an Identifier");
            return;
        }

        SimpleNode secondChild = (SimpleNode) statementNode.jjtGetChild(1);

        if(secondChild.getId() == JavammTreeConstants.JJTEQUAL){
            List<Descriptor> firstDescriptorList = symbolTable.getDescriptor(firstChild.jjtGetVal());
            if(firstDescriptorList == null){
                System.out.println("Error: Variable "+firstChild.jjtGetVal()+" not declared");
                return;
            }

            TypeDescriptor typeDescriptor = null;
            if(firstDescriptorList.size() == 1){
                typeDescriptor = (TypeDescriptor) firstDescriptorList.get(0);
            }else{
                for(int i = 0; i < firstDescriptorList.size(); i++){
                    if(firstDescriptorList.get(i).getClass() == VariableDescriptor.class){
                        typeDescriptor = (TypeDescriptor) firstDescriptorList.get(i);
                        break;
                    }
                }
                if(typeDescriptor == null){
                    System.err.println("Error: Variable " + firstChild.jjtGetVal()+" doesn't have a type");
                    return;
                }
            }

            //Assignement
            Type type = typeDescriptor.getType();
            inspectAssignement(statementNode, symbolTable, type);
        }
        else{
            //Function call
            inspectFunctionCall(statementNode, symbolTable);
        }
    }

    public void inspectAssignement(SimpleNode statementNode, SymbolsTable symbolsTable, Type type){

    }
    
    public String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolTable){
        return inspectFunctionCall(statementNode, symbolTable, 0);
    }

    public String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolTable, int initialChild){
        List<String> identifiers = new ArrayList<>();
        SimpleNode node = (SimpleNode) statementNode.jjtGetChild(initialChild);
        identifiers.add(node.jjtGetVal());

        SimpleNode child = (SimpleNode) statementNode.jjtGetChild(initialChild+1);
        int nextChild = initialChild+1;
        if(child.getId() == JavammTreeConstants.JJTDOT){
            node = (SimpleNode) statementNode.jjtGetChild(initialChild+2);
            identifiers.add(node.jjtGetVal());
            nextChild = initialChild+3;
        }

        SimpleNode argumentsNode = (SimpleNode) statementNode.jjtGetChild(nextChild);
        if(argumentsNode.getId() != JavammTreeConstants.JJTARGUMENTS){
            System.err.println("Error: Unexpected node with id=" + argumentsNode.getId());
            return null;
        }

        List<String> parameters = new ArrayList<>();
        for(int i = 0; i < argumentsNode.jjtGetNumChildren(); i++){
            SimpleNode argumentNode = (SimpleNode) argumentsNode.jjtGetChild(i);
            if(argumentNode.getId() != JavammTreeConstants.JJTARGUMENT){
                System.err.println("Error: Unexpected argument node with id=" + argumentNode.getId());
                return null;
            }
            String parameterType = inspectArgument(argumentNode, symbolTable);
            parameters.add(parameterType);
        }

        //TODO Verificar se a função existe e o seu retorno

        return null;
    }

    public String inspectArgument(SimpleNode argumentNode, SymbolsTable symbolsTable){ 
        SimpleNode node;
        if(argumentNode.jjtGetNumChildren() == 1){
            node = (SimpleNode) argumentNode.jjtGetChild(0);

            switch(node.getId()){
                case JavammTreeConstants.JJTINTEGERLITERAL: {
                    return "int";
                }
                case JavammTreeConstants.JJTTRUE: 
                case JavammTreeConstants.JJTFALSE: {
                    return "boolean";
                }
                case JavammTreeConstants.JJTIDENTIFIER: {
                    List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                    if(descriptors.size() > 1){
                        System.err.println("Error: Argument " + node.jjtGetVal() + " is defined more than once");
                        return null;
                    }
                    TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                    
                    Type type = descriptor.getType();
                    if(type == Type.CLASS){
                        return descriptor.getClassName();
                    }
                    return (new StringType(type)).getString();
                }
                default: {
                    //TODO Adicionar mais casos
                    System.out.println("Argument Node ID: " + node.getId()); //Para detetar novos casos 
                }
            }
        }else{
            //TODO Se tiver mais do que 1 children pode ser: chamada a função; operação
            String type = null;
            
            for(int i = 0; i < argumentNode.jjtGetNumChildren(); i++){
                node = (SimpleNode) argumentNode.jjtGetChild(i);
                switch(node.getId()){
                    case JavammTreeConstants.JJTINTEGERLITERAL: {
                        if(type == null){
                            type = "int";
                        }else if(!type.equals("int")){
                            System.err.println("ERROR: INT IS INCOMPATIBLE WITH " + type);
                            return null;
                        }
                        break;
                    }
                    case JavammTreeConstants.JJTTRUE: 
                    case JavammTreeConstants.JJTFALSE: {
                        System.err.println("ERROR: OPERATION CAN'T INVOLVE A BOOLEAN TYPE");
                        return null;
                    }
                    case JavammTreeConstants.JJTIDENTIFIER: {
                        if(i+1 < argumentNode.jjtGetNumChildren()){
                            SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(i+1);
                            if(nextNode.getId() == JavammTreeConstants.JJTDOT){
                                String functionType = inspectFunctionCall(argumentNode, symbolsTable, i);
                                if(type == null){
                                    type = functionType;
                                }else if(!type.equals(functionType)){
                                    System.err.println("ERROR: " + functionType + " IS INCOMPATIBLE WITH " + type);
                                    return null;
                                }
                                i += 3; // Jump function identifiers
                                continue;
                            }
                        }

                        List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                        if(descriptors == null ){
                            System.err.println("Error: Argument " + node.jjtGetVal() + " is not defined");
                            return null;
                        }else if(descriptors.size() > 1){
                            System.err.println("Error: Argument " + node.jjtGetVal() + " is defined more than once");
                            return null;
                        }
                        TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                        
                        Type descriptorType = descriptor.getType();
                        if(descriptorType == Type.CLASS){
                            System.err.println("ERROR: OPERATION CAN'T INVOLVE A CLASS TYPE");
                            return null;
                        }

                        String descType = (new StringType(descriptorType)).getString();
                        if(type == null){
                            type = descType;
                        }else if(!type.equals(descType)){
                            System.err.println("ERROR: " + descType + " IS INCOMPATIBLE WITH " + type);
                            return null;
                        }
                        break;
                    }
                    default:{ //Plus, Minus, ...

                    }
                }
            }

            return type;
        }
        /**
         * Must return the argument type after check if it's valid
         */
        
         return null;
    }
}