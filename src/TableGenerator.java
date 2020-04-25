import symbols.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.LinkedHashMap;

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
        HashMap<SimpleNode, FunctionDescriptor> functions = new HashMap<SimpleNode, FunctionDescriptor>();

        // Collect attributes and methods access, return type and name 
        for (int i = 0; i < classNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) classNode.jjtGetChild(i);
            if (child.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
                VariableDescriptor variableDescriptor = inspectVariable(child);
                classDescriptor.addVariable(variableDescriptor);
            }
            else if (child.getId() == JavammTreeConstants.JJTMETHODDECLARATION) {
                FunctionDescriptor functionDescriptor = inspectFunctionHeader(child);
                functionDescriptor.getParametersTable().setParent(classDescriptor.getFunctionsTable());
                classDescriptor.addFunction(functionDescriptor.getName(),functionDescriptor);
                functions.put(child, functionDescriptor);
            }
        }

        // Inspect inside each function
        for (HashMap.Entry<SimpleNode, FunctionDescriptor> function : functions.entrySet()) {
            inspectFunctionBody(function.getKey(), function.getValue());
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

    public FunctionDescriptor inspectFunctionHeader(SimpleNode functionNode) {
        SimpleNode child = (SimpleNode) functionNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            return inspectMainHeader(child);
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            return inspectMethodHeader(child);
        }

        return null;
    }

    public FunctionDescriptor inspectMainHeader(SimpleNode mainNode) {

        FunctionDescriptor functionDescriptor = new FunctionDescriptor();

        functionDescriptor.setAccessVal("public");
        functionDescriptor.makeStatic();
        functionDescriptor.setReturnValue("void");
        functionDescriptor.setName("main");

        SimpleNode child = (SimpleNode) mainNode.jjtGetChild(0);
        if (child.getId() == JavammTreeConstants.JJTMAINARGS) {

            for(int j = 0; j < child.jjtGetNumChildren(); j+=2) {
                SimpleNode grandChildType = (SimpleNode) child.jjtGetChild(j);
                if (j+1 < child.jjtGetNumChildren()) {
                    SimpleNode grandChildName = (SimpleNode) child.jjtGetChild(j+1);
                    if (grandChildType.getId() == JavammTreeConstants.JJTTYPE && grandChildName.getId() == JavammTreeConstants.JJTIDENTIFIER) {
                        TypeString typeString = new TypeString(grandChildType.jjtGetVal());
                        String name = grandChildName.jjtGetVal();
                        FunctionParameterDescriptor parameterDescriptor = new FunctionParameterDescriptor(name, typeString.parseType());
                        functionDescriptor.addParameter(parameterDescriptor);
                    }
                }
            }
        }

        return functionDescriptor;
    }

    public FunctionDescriptor inspectMethodHeader(SimpleNode methodNode) {

        FunctionDescriptor functionDescriptor = new FunctionDescriptor();

        for (int j = 0; j < methodNode.jjtGetNumChildren(); j++) {
            SimpleNode child = (SimpleNode) methodNode.jjtGetChild(j);
            switch (child.getId()) {
                case JavammTreeConstants.JJTMODIFIER: 
                {
                    functionDescriptor.setAccessVal(child.val);
                    break;
                }
                case JavammTreeConstants.JJTTYPE:
                {
                    functionDescriptor.setReturnValue(child.val);
                    break;
                }
                case JavammTreeConstants.JJTIDENTIFIER:
                {
                    functionDescriptor.setName(child.val);
                    break;
                }
                case JavammTreeConstants.JJTMETHODARGUMENTS:
                {
                    inspectMethodArguments(child,functionDescriptor.getParametersTable());
                    break;
                }
            }
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

    public void inspectFunctionBody(SimpleNode functionNode, FunctionDescriptor functionDescriptor) {
        SimpleNode child = (SimpleNode) functionNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            inspectMainBody(child, functionDescriptor);
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            inspectMethodBody(functionNode, functionDescriptor);
        }
    }

    public void inspectMainBody(SimpleNode mainNode, FunctionDescriptor functionDescriptor) {

        //Main Body Parser
        for (int i = 1; i < mainNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) mainNode.jjtGetChild(i);
            inspectVariableAndStatement(child, functionDescriptor);
        }
    }

    //Inspects all the methods except main
    public void inspectMethodBody(SimpleNode methodNode, FunctionDescriptor functionDescriptor) {

        //Cycle can have MethodHeader, LineStatement return IntegralLiteral
        for (int i = 1; i < methodNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) methodNode.jjtGetChild(i);

            //Method Body Parser 
            if (child.getId() == JavammTreeConstants.JJTRETURN){
                SimpleNode actualReturn = (SimpleNode) child.jjtGetChild(0);
                functionDescriptor.setActualReturnValue(actualReturn.val);
            }
            else inspectVariableAndStatement(child, functionDescriptor);
        }

    }

    public void inspectVariableAndStatement(SimpleNode variableAndStatementNode, FunctionDescriptor functionDescriptor) {
        if (variableAndStatementNode.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) 
        {
            VariableDescriptor variableDescriptor = inspectVariable(variableAndStatementNode);
            functionDescriptor.addBodyVariable(variableDescriptor.getName(),variableDescriptor);
        }
        else if (variableAndStatementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT )
        {
            //TODO: Analizar statement
            inspectLineStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }
        /*else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT || variableAndStatementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
            inspectBlockStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }else{
            System.err.println("Error: Unknown symbol");
        }*/
    }

    public void inspectLineStatement(SimpleNode statementNode, SymbolsTable symbolTable){
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

            //Assignment
            Type type = typeDescriptor.getType();
            String typeString;
            if(type == Type.CLASS){
                typeString = typeDescriptor.getClassName();
            }else{
                StringType strType = new StringType(type);
                typeString = strType.getString();
            }

            inspectAssignment(statementNode, symbolTable, typeString);

            VariableDescriptor variableDescriptor = (VariableDescriptor) typeDescriptor;
            variableDescriptor.setInitialized();
        }
        else{
            //Function call
            inspectFunctionCall(statementNode, symbolTable);
        }
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

    private String inspectArrayAccess(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild){
        SimpleNode idNode = (SimpleNode) statementNode.jjtGetChild(initialChild);

        List<Descriptor> descriptors = symbolsTable.getDescriptor(idNode.jjtGetVal());

        TypeDescriptor idDescriptor = null;
        for(int i = 0; i < descriptors.size(); i++){
            Descriptor d = descriptors.get(i);
            if(d.getClass() == FunctionParameterDescriptor.class || d.getClass() == VariableDescriptor.class){
                idDescriptor = (TypeDescriptor) d;
                if(idDescriptor.getType() != Type.INT_ARRAY && idDescriptor.getType() != Type.STRING_ARRAY){
                    System.err.println("ERROR: CAN ONLY ACCESS ARRAYS OF INT OR STRING");
                    return null;
                }
                break;
            }
        }

        if(idDescriptor == null)
            return null;

        SimpleNode arrayNode = (SimpleNode) statementNode.jjtGetChild(initialChild+1);
       
        String indexType = inspectExpression(arrayNode, symbolsTable);

        if(indexType == null){
            System.err.println("ERROR: Array index must be an int");
            return null;
        } else if(!indexType.equals("int")){
            System.err.println("ERROR: Array index must be an int");
            return null;
        }

        StringType stringType = new StringType(idDescriptor.getType());

        return stringType.getString();
    }

    private void inspectAssignment(SimpleNode statementNode, SymbolsTable symbolsTable, String type){
        String expType = inspectExpression(statementNode, symbolsTable, 2);
        
        if(expType == null){
            System.err.println("ERROR: Can't assign invalid type");
        } else if(!type.equals(expType)){
            System.err.println("ERROR: Can't assign " + expType + " to variable of type " + type);
        }
    }
    
    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable){
        return inspectFunctionCall(statementNode, symbolsTable, 0);
    }

    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild){

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
            System.err.println("ERROR: Unexpected node with id=" + argumentsNode.getId());
            return null;
        }

        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(((SimpleNode)statementNode.jjtGetChild(2)).jjtGetVal());

        if (descriptorsList == null) {
            System.err.println("ERROR: Function "+((SimpleNode)statementNode.jjtGetChild(2)).jjtGetVal()+" not declared.");
            return null;
        }

        for(int i = 0; i < descriptorsList.size(); i++){

            FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptorsList.get(i);
            SymbolsTable parametersTable = functionDescriptor.getParametersTable();
            HashMap<String, List<Descriptor>> functionParameters = parametersTable.getTable();

            if(functionParameters.size() != parameters.size()) {
                if (i == descriptorsList.size()-1) {
                    System.err.println("ERROR: Wrong number of arguments for function call: "+ functionDescriptor.getName());
                    return null;
                }
                continue;
            }

            int j = 0;
            for(HashMap.Entry<String, List<Descriptor>> functionParametersEntry : functionParameters.entrySet()){
                List<Descriptor> descList = functionParametersEntry.getValue();
                
                if(descList.size() != 1){
                    System.err.println("ERROR: Function can't have more than 1 parameter with the same identifier");
                    return null;
                }
                
                FunctionParameterDescriptor parameterDescriptor = (FunctionParameterDescriptor) descList.get(0);
            
                Type parameterType = parameterDescriptor.getType();
                
                if(parameterType == Type.CLASS){
                    String className = parameterDescriptor.getClassName();
                    if(!className.equals(parameters.get(j))){
                        System.err.println("ERROR: Incompatible type for argument in function "+functionDescriptor.getName());
                        return null;
                    }
                }else{
                    StringType type = new StringType(parameterType);
                    if(!type.getString().equals(parameters.get(j))){
                        System.err.println("ERROR: Incompatible type for argument in function "+functionDescriptor.getName());
                        return null;
                    }
                }
                j++;
            }

            // If the function was found, the function type is returned

            Type functionType = functionDescriptor.getType();
            if(functionType == Type.CLASS)
                return functionDescriptor.getClassName();
            
            StringType stringType = new StringType(functionType);
            return stringType.getString();
        }

        return null;
    }

    private List<String> inspectArguments(SimpleNode argumentsNode, SymbolsTable symbolsTable){
        List<String> parameters = new ArrayList<>();
        for(int i = 0; i < argumentsNode.jjtGetNumChildren(); i++){
            SimpleNode argumentNode = (SimpleNode) argumentsNode.jjtGetChild(i);
            if(argumentNode.getId() != JavammTreeConstants.JJTARGUMENT){
                System.err.println("Error: Unexpected argument node with id=" + argumentNode.getId());
                return null;
            }
            String parameterType = inspectArgument(argumentNode, symbolsTable);
            parameters.add(parameterType);
        }

        return parameters;
    }

    private String inspectArgument(SimpleNode argumentNode, SymbolsTable symbolsTable){ 
        return inspectExpression(argumentNode, symbolsTable);
    }

    private String inspectExpression(SimpleNode expressionNode, SymbolsTable symbolsTable){
        return inspectExpression(expressionNode, symbolsTable, 0);
    }
    
    
    private String inspectExpression(SimpleNode expressionNode, SymbolsTable symbolsTable, int initialChild){
        /**
         * Must return the argument type after check if it's valid
        */
        if(expressionNode.jjtGetNumChildren() - initialChild == 1) {
            return inspectExpressionSimple(expressionNode, symbolsTable, initialChild);
        }
        return inspectExpressionComplex(expressionNode, symbolsTable, initialChild);
    }

    private String inspectExpressionSimple(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) {
        SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(initialChild);

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
                if(descriptors == null){
                    System.err.println("Error: Undefined variable " + node.jjtGetVal());
                    return null;
                }

                if(descriptors.size() > 1){
                    System.err.println("Error: Argument " + node.jjtGetVal() + " is defined more than once");
                    return null;
                }
                TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                
                Type type = descriptor.getType();
                if(type == Type.CLASS){
                    return descriptor.getClassName();
                }

                if(descriptor.getClass() == VariableDescriptor.class){
                    VariableDescriptor variableDescriptor = (VariableDescriptor) descriptor;
                    if(!variableDescriptor.isInitialized()){
                        System.err.println("ERROR: Variable " + node.jjtGetVal() + " is not initialized");
                    }
                }

                return (new StringType(type)).getString();
            }
            default: {
                //TODO Adicionar mais casos
                System.out.println("Argument Node ID: " + node.getId()); //Para detetar novos casos 
            }
        }

        System.err.println("Error: Invalid Argument: " + node.jjtGetVal());
        return null;
    }

    private String inspectExpressionComplex(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) {
        String type = null;

        for(int i = initialChild; i < argumentNode.jjtGetNumChildren(); i++){
            SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(i);

            switch(node.getId()){
                case JavammTreeConstants.JJTINTEGERLITERAL: {
                    if(type == null){
                        type = "int";
                    }
                    else if(!type.equals("int")){
                        System.err.println("ERROR: INT IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                case JavammTreeConstants.JJTTRUE: 
                case JavammTreeConstants.JJTFALSE: {
                    if(type == null){
                        type = "boolean";
                    } else if(!type.equals("boolean")){
                        System.err.println("ERROR: BOOLEAN IS INCOMPATIBLE WITH " + type);
                        return null;
                    }

                    break;
                }
                case JavammTreeConstants.JJTTHIS: {
                 
                    String functionType = inspectFunctionCall(argumentNode, symbolsTable, i+2);
                    if(type == null){
                        type = functionType;
                    }else if(!type.equals(functionType)){
                        System.err.println("ERROR: " + functionType + " IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    i += 3;

                    break;
                } 
                case JavammTreeConstants.JJTIDENTIFIER: {
                    if(i+1 < argumentNode.jjtGetNumChildren()){ //CHECK IF THE IDENTIFIER BELONGS TO A FUNCTION OR ARRAY
                        SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(i+1);

                        if(nextNode.getId() == JavammTreeConstants.JJTDOT){ //IF THE IDENTIFIER IS FOLLOWED BY A DOT, IT'S A FUNCTION CALL

                            SimpleNode nextNextNode = (SimpleNode) argumentNode.jjtGetChild(i+2); //A DOT is always followed by something: length or a function call

                            if(nextNextNode.getId() == JavammTreeConstants.JJTLENGTH){
                                List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                                if(descriptors == null){
                                    System.err.println("ERROR: Undeclared variable " + node.jjtGetVal());
                                    return null;
                                }

                                for(int j = 0; j < descriptors.size(); j++){
                                    Descriptor descriptor = descriptors.get(j);
                                    if(descriptor.getClass() == FunctionParameterDescriptor.class || descriptor.getClass() == VariableDescriptor.class){
                                        TypeDescriptor typeDescriptor = (TypeDescriptor) descriptor;
                                        if(typeDescriptor.getType() != Type.INT_ARRAY && typeDescriptor.getType() != Type.STRING_ARRAY){
                                            System.err.println("ERROR: Property length only exists in arrays");
                                            return null;
                                        } 

                                        i += 2;
                                        continue;
                                    }
                                }
                                
                                System.err.println("ERROR: " + node.jjtGetVal() + " is not a variable");
                                return null;
                            }

                            //Else function call
                            String functionType = inspectFunctionCall(argumentNode, symbolsTable, i);
                            if(type == null){
                                type = functionType;
                            }else if(!type.equals(functionType)){
                                System.err.println("ERROR: " + functionType + " IS INCOMPATIBLE WITH " + type);
                                return null;
                            }
                            i += 3; // Jump function identifiers
                        
                            continue;
                        } else if(nextNode.getId() == JavammTreeConstants.JJTARRAY){    
                            String arrayType = inspectArrayAccess(argumentNode, symbolsTable, i);
                            if(type == null){
                                type = arrayType;
                            }else if(!type.equals(arrayType)){
                                System.err.println("ERROR: " + arrayType + " IS INCOMPATIBLE WITH " + type);
                                return null;
                            }

                            i += 1; // Jump array nodes
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

                    if(descriptor.getClass() == VariableDescriptor.class){
                        VariableDescriptor variableDescriptor = (VariableDescriptor) descriptor;
                        if(!variableDescriptor.isInitialized()){
                            System.err.println("ERROR: Variable " + node.jjtGetVal() + " is not initialized");
                        }
                    }

                    break;
                }
                case JavammTreeConstants.JJTAND:{
                    if(!type.equals("boolean")){
                        System.err.println("ERROR: OPERATION && IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                case JavammTreeConstants.JJTNEW: {
                    if(i != initialChild || i + 3 < argumentNode.jjtGetNumChildren()){
                        System.err.println("ERROR: CAN'T INSTANTIATE CLASS INSIDE AN EXPRESSION");
                        return null;
                    }
                    
                    SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);

                    if(nextNode.getId() == JavammTreeConstants.JJTARRAY){
                        SimpleNode idNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+1);
                        if(idNode.jjtGetVal() != "int"){
                            System.err.println("ERROR: CAN ONLY INSTANTIATE ARRAY OF INT");
                            return null;
                        }
                        
                        SimpleNode arrayNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);
                    
                        String indexType = inspectExpression(arrayNode, symbolsTable);

                        if(indexType == null){
                            System.err.println("ERROR: Array index must be an int");
                            return null;
                        } else if(!indexType.equals("int")){
                            System.err.println("ERROR: Array index must be an int");
                            return null;
                        }

                        return "int[]";
                    }
                    return inspectClassInstantiation(argumentNode, symbolsTable, initialChild);
                }
                //TODO Check this  
                case JavammTreeConstants.JJTNEGATION: {
                    if(type == null){
                        type = "boolean";
                    }else if(!type.equals("boolean")){
                        System.err.println("ERROR: OPERATION ! IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                //TODO Check this  
                case JavammTreeConstants.JJTLESS: {
                    if(!type.equals("int")){
                        System.err.println("ERROR: CAN'T COMPARE " + type + " WITH OPERATOR <");
                        return null;
                    }
                    
                    String otherType = inspectExpression(argumentNode, symbolsTable, i+1);
                    if(!otherType.equals("int")){
                        System.err.println("ERROR: CAN'T COMPARE " + otherType + " WITH OPERATOR <");
                        return null;
                    }

                    return "boolean";
                }
                case JavammTreeConstants.JJTDOT: {
                    System.err.println("ERROR: CAN'T ACCESS PROPERTY/METHOD OF VARIABLE OF TYPE " + type);
                    return null;
                }
                default:{ //Plus, Minus, ...
                    if(!type.equals("int")){
                        System.out.println("ERROR: OPERATIONS ARE INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
            }
        }

        return type;
    }

    private String inspectClassInstantiation(SimpleNode node, SymbolsTable symbolsTable){
        return inspectClassInstantiation(node, symbolsTable, 0);
    }

    private String inspectClassInstantiation(SimpleNode node, SymbolsTable symbolsTable, int newPosition){
        SimpleNode classIdentifierNode = (SimpleNode) node.jjtGetChild(newPosition+1);
                        
        // Get all the Class constructors
        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(classIdentifierNode.jjtGetVal());  
        
        SimpleNode argumentsNode = (SimpleNode) node.jjtGetChild(newPosition+2);

        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        
        for(int i = 0; i < descriptorsList.size(); i++){
            Descriptor descriptor = descriptorsList.get(i);
            if(descriptor.getClass() == ImportDescriptor.class){
                ImportDescriptor importDescriptor = (ImportDescriptor) descriptor;
                if(importDescriptor.getIdentifiers().size() != 1)
                    continue;
                
                List<Type> importParameters = importDescriptor.getParameters();
                if(importParameters.size() != parameters.size())
                    continue;
                
                //TODO Change ImportDescriptor: instead of returning a list of types, returns a list of Strings with the types 
                
            }else if(descriptor.getClass() == FunctionDescriptor.class){
                FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;
                SymbolsTable parametersTable = functionDescriptor.getParametersTable();
            
                LinkedHashMap<String, List<Descriptor>> functionParameters = parametersTable.getTable();
                if(functionParameters.size() != parameters.size())
                    continue;
                
                for(int j = 0; j < functionParameters.size(); j++){
                    FunctionParameterDescriptor parameterDescriptor = (FunctionParameterDescriptor) functionParameters.get(j);

                    Type parameterType = parameterDescriptor.getType();
                    
                    if(parameterType == Type.CLASS){
                        String className = parameterDescriptor.getClassName();
                        if(!className.equals(parameters.get(j))){
                            System.err.println("ERROR: INCOMPATIBLE TYPE");
                            return null;
                        }
                    }else{
                        StringType type = new StringType(parameterType);
                        if(!type.getString().equals(parameters.get(j))){
                            System.err.println("ERROR: INCOMPATIBLE TYPE");
                            return null;
                        }
                    }
                }

                return classIdentifierNode.jjtGetVal();
            }
        }

        return null;
    }
}