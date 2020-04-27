import symbols.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Function;

import llir.LLIRArithmetic;
import llir.LLIRAssignment;
import llir.LLIRInteger;
import llir.LLIRNode;
import llir.LLIRVariable;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TableGenerator {
    SimpleNode rootNode;
    SymbolsTable symbolsTable;
    SemanticError semanticError;
    FunctionDescriptor currentFunctionDescriptor;
    LLIRNode currentLLIRNode;

    public TableGenerator(SimpleNode rootNode) {
        this.rootNode = rootNode;
        this.symbolsTable = new SymbolsTable();
        this.semanticError = new SemanticError();
    }

    public SymbolsTable getTable() {
        return this.symbolsTable;
    }

    public int getNumErrors() {
        return this.semanticError.getNumErrors();
    }

    public void build() throws SemanticErrorException {
        int i = 0;
        // System.out.println(rootNode.jjtGetNumChildren());
        while(i < rootNode.jjtGetNumChildren()) {
            SimpleNode currentNode = (SimpleNode) rootNode.jjtGetChild(i);

            switch(currentNode.getId()) {
                case JavammTreeConstants.JJTIMPORTDECLARATION:
                    ImportDescriptor importDescriptor = inspectImport(currentNode);
                    symbolsTable.addSymbol(importDescriptor.getLastIdentifier(), importDescriptor);
                    currentNode.setDescriptor(importDescriptor);
                    break;

                case JavammTreeConstants.JJTCLASSDECLARATION:
                    inspectClass(currentNode);
                    break;
            }

            i++;
        }
    }

    public ImportDescriptor inspectImport(SimpleNode importNode) throws SemanticErrorException {
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
                    if(grandChild.jjtGetVal().equals("void"))
                        continue;
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

                if(typeString.parseType() == Type.CLASS){
                    importDescriptor.setClassName(grandChild.jjtGetVal());
                }
            }
        }

        return importDescriptor;
    }

    public void inspectClass(SimpleNode classNode) throws SemanticErrorException {
        ClassDescriptor classDescriptor = new ClassDescriptor();
        
        SimpleNode childNode = (SimpleNode) classNode.jjtGetChild(0);
        classDescriptor.setName(childNode.jjtGetVal());
        this.symbolsTable.addSymbol(classDescriptor.getName(), classDescriptor, false);

        SymbolsTable classVariablesTable = classDescriptor.getVariablesTable();
        classVariablesTable.setParent(this.symbolsTable);

        List<SimpleNode> simpleNodes = new ArrayList<>();
        List<FunctionDescriptor> functions = new ArrayList<>(); 



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
                simpleNodes.add(child);
                functions.add(functionDescriptor);
            }
            else if (child.getId() == JavammTreeConstants.JJTEXTENDS) {
                classDescriptor.setParentClass(((SimpleNode) (classNode.jjtGetChild(i+1))).jjtGetVal());
            }

        }

        for(int i = 0; i < simpleNodes.size(); i++){
            inspectFunctionBody(simpleNodes.get(i), functions.get(i));
        } 
    }

    public VariableDescriptor inspectVariable(SimpleNode variableNode) throws SemanticErrorException {
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

    public FunctionDescriptor inspectFunctionHeader(SimpleNode functionNode) throws SemanticErrorException {
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

    public FunctionDescriptor inspectMainHeader(SimpleNode mainNode) throws SemanticErrorException {

        FunctionDescriptor functionDescriptor = new FunctionDescriptor();
        this.currentFunctionDescriptor = functionDescriptor;
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

    public FunctionDescriptor inspectMethodHeader(SimpleNode methodNode) throws SemanticErrorException {

        FunctionDescriptor functionDescriptor = new FunctionDescriptor();
        this.currentFunctionDescriptor = functionDescriptor;

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

    public void inspectMethodArguments(SimpleNode methodArgumentsNode,SymbolsTable parametersTable) throws SemanticErrorException {

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

    public void inspectFunctionBody(SimpleNode functionNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        SimpleNode child = (SimpleNode) functionNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            inspectMainBody(child, functionDescriptor);
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            inspectMethodBody(functionNode, functionDescriptor);
        }
    }

    public void inspectMainBody(SimpleNode mainNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {

        //Main Body Parser
        for (int i = 1; i < mainNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) mainNode.jjtGetChild(i);
            inspectVariableAndStatement(child, functionDescriptor);
        }
    }

    //Inspects all the methods except main
    public void inspectMethodBody(SimpleNode methodNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {

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

    public void inspectVariableAndStatement(SimpleNode variableAndStatementNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        if (variableAndStatementNode.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
            VariableDescriptor variableDescriptor = inspectVariable(variableAndStatementNode);
            functionDescriptor.addBodyVariable(variableDescriptor.getName(),variableDescriptor);
        } else if (variableAndStatementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
            inspectLineStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        } else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
            inspectWhileStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        } else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
            inspectIfStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }else{
            this.semanticError.printError(variableAndStatementNode, "Unknown symbol");
        }
    }

    public void inspectLineStatement(SimpleNode statementNode, SymbolsTable symbolTable) throws SemanticErrorException {
        if(statementNode.jjtGetNumChildren() < 3){
            this.semanticError.printError(statementNode, "Invalid Line Statement");
            return;
        }

        SimpleNode firstChild = (SimpleNode) statementNode.jjtGetChild(0);
        
        if(firstChild.getId() != JavammTreeConstants.JJTIDENTIFIER && firstChild.getId() != JavammTreeConstants.JJTTHIS){
            this.semanticError.printError(firstChild, "First Node of Line Statement must be an Identifier");
            return;
        }

        SimpleNode secondChild = (SimpleNode) statementNode.jjtGetChild(1);

        if(secondChild.getId() == JavammTreeConstants.JJTEQUAL){
            List<Descriptor> firstDescriptorList = symbolTable.getDescriptor(firstChild.jjtGetVal());
            if(firstDescriptorList == null){
                this.semanticError.printError(firstChild, "Variable "+firstChild.jjtGetVal()+" not declared");
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
                    this.semanticError.printError(firstChild, "Variable " + firstChild.jjtGetVal()+" doesn't have a type");
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

            // Add a new LLIR Node (Assignment) to the function
            this.currentLLIRNode = new LLIRAssignment();

            inspectAssignment(statementNode, symbolTable, typeString);

            if(typeDescriptor.getClass() == VariableDescriptor.class){
                VariableDescriptor variableDescriptor = (VariableDescriptor) typeDescriptor;
                variableDescriptor.setInitialized();

                // Sets the variable of the assignment
                LLIRAssignment llir = (LLIRAssignment) this.currentLLIRNode;
                LLIRVariable variable = new LLIRVariable(variableDescriptor);
                llir.setVariable(variable);
            }

            this.currentFunctionDescriptor.addLLIRNode(this.currentLLIRNode);

        }
        else if(secondChild.getId() == JavammTreeConstants.JJTARRAY) {
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
            if(type != Type.STRING_ARRAY && type != Type.INT_ARRAY){
                System.err.println("Error: Variable " + firstChild.jjtGetVal()+" is not an array");
                return;   
            }

            SimpleNode arrayNode =  (SimpleNode) statementNode.jjtGetChild(1);
            String indexType = inspectExpression(arrayNode, symbolTable);
            if(!indexType.equals("int")){
                System.err.println("Error: Array index must be an int");
                return;
            }
           
            String typeString;
            if(type == Type.STRING_ARRAY){
                typeString = "String";
            }else{
                typeString = "int";
            }

            inspectAssignment(statementNode, symbolTable, typeString, 3);

            if(typeDescriptor.getClass() == VariableDescriptor.class){
                VariableDescriptor variableDescriptor = (VariableDescriptor) typeDescriptor;
                variableDescriptor.setInitialized();
            }
            //TODO Add case where variable is a FunctionParameterDescriptor -> useful in the code generation
        }
        else{
            //Function call
            inspectFunctionCall(statementNode, symbolTable);
        }
    }

    public void inspectWhileStatement(SimpleNode whileNode, SymbolsTable statementParentTable) throws SemanticErrorException {
        if(whileNode.jjtGetNumChildren() == 0){
            this.semanticError.printError(whileNode, "While needs to have an expression.");
            return;
        }
        
        SimpleNode whileExpression = (SimpleNode) whileNode.jjtGetChild(0);
        if(whileExpression.getId() != JavammTreeConstants.JJTWHILEEXPRESSION){
            this.semanticError.printError(whileExpression, "While needs to have an expression.");
            return;
        }

        String expressionType = inspectExpression(whileExpression, statementParentTable); //TODO Store this expression in the block descriptor
        if(!expressionType.equals("boolean")){
            this.semanticError.printError(whileExpression, "While expression must evaluate to a boolean");
            return;
        }

        BlockDescriptor blockDescriptor = new BlockDescriptor(statementParentTable);
        
        statementParentTable.addSymbol("while", blockDescriptor);
        
        for(int i = 1; i < whileNode.jjtGetNumChildren(); i++){
            SimpleNode statementNode = (SimpleNode) whileNode.jjtGetChild(i);

            if (statementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
                inspectLineStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
                inspectWhileStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
                inspectIfStatement(statementNode, blockDescriptor.getLocalTable());
            }else{
                this.semanticError.printError(statementNode, "Unknown symbol");
            }
        }
    }

    public void inspectIfStatement(SimpleNode ifNode, SymbolsTable statementParentTable) throws SemanticErrorException {
        //TODO CHECK this
        if(ifNode.jjtGetNumChildren() == 0){
            this.semanticError.printError(ifNode, "If needs to have an expression.");
            return;
        }

        SimpleNode ifExpression = (SimpleNode) ifNode.jjtGetChild(0);
        if(ifExpression.getId() != JavammTreeConstants.JJTIFEXPRESSION){
            this.semanticError.printError(ifExpression, "If needs to have an expression.");
            return;
        }

        String expressionType = inspectExpression(ifExpression, statementParentTable); //TODO Store this expression in the block descriptor
        if(!expressionType.equals("boolean")){
            this.semanticError.printError(ifExpression, "If expression must evaluate to a boolean");
            return;
        }

        BlockDescriptor blockDescriptor = new BlockDescriptor(statementParentTable);
        
        statementParentTable.addSymbol("if", blockDescriptor);
        
        boolean found_else = false;
        for(int i = 1; i < ifNode.jjtGetNumChildren(); i++){
            SimpleNode statementNode = (SimpleNode) ifNode.jjtGetChild(i);

            if (statementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
                inspectLineStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
                inspectWhileStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
                inspectIfStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTELSE && !found_else){
                found_else = true;
                continue;
            } else{
                this.semanticError.printError(statementNode, "Unknown symbol "+statementNode.getId());
            }
        }
    }

    private String inspectArrayAccess(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SimpleNode idNode = (SimpleNode) statementNode.jjtGetChild(initialChild);

        List<Descriptor> descriptors = symbolsTable.getDescriptor(idNode.jjtGetVal());

        TypeDescriptor idDescriptor = null;
        for(int i = 0; i < descriptors.size(); i++){
            Descriptor d = descriptors.get(i);
            if(d.getClass() == FunctionParameterDescriptor.class || d.getClass() == VariableDescriptor.class){
                idDescriptor = (TypeDescriptor) d;
                if(idDescriptor.getType() != Type.INT_ARRAY && idDescriptor.getType() != Type.STRING_ARRAY){
                    this.semanticError.printError(idNode, "CAN ONLY ACCESS ARRAYS OF INT OR STRING");
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
            this.semanticError.printError(arrayNode, "Array index must be an int");
            return null;
        } else if(!indexType.equals("int")){
            this.semanticError.printError(arrayNode, "Array index must be an int");
            return null;
        }

        //StringType stringType = new StringType(idDescriptor.getType());
        if(idDescriptor.getType() == Type.INT_ARRAY)
            return "int";
        return "String";
        //return stringType.getString();
    }

    private void inspectAssignment(SimpleNode statementNode, SymbolsTable symbolsTable, String type) throws SemanticErrorException {
        inspectAssignment(statementNode, symbolsTable, type, 2);
    }

    private void inspectAssignment(SimpleNode statementNode, SymbolsTable symbolsTable, String type, int initialChild) throws SemanticErrorException {
        String expType = inspectExpression(statementNode, symbolsTable, initialChild);

        if(expType == null){
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(2), "Can't assign invalid type");
        } else if(!type.equals(expType)){
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(2), "Can't assign " + expType + " to variable of type " + type);
        }
    }
    
    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable) throws SemanticErrorException {
        return inspectFunctionCall(statementNode, symbolsTable, 0);
    }

    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        
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
            this.semanticError.printError(argumentsNode, "Unexpected node with id=" + argumentsNode.getId());
            return null;
        }

        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(((SimpleNode)statementNode.jjtGetChild(nextChild-1)).jjtGetVal());
        if (descriptorsList == null) {
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Function "+((SimpleNode)statementNode.jjtGetChild(nextChild-1)).jjtGetVal()+" not declared.");
            return null;
        }

        for(int i = 0; i < descriptorsList.size(); i++){
            if(descriptorsList.get(i).getClass()  == ImportDescriptor.class){
                ImportDescriptor importDescriptor = (ImportDescriptor) descriptorsList.get(i);

                ArrayList<String> importIdentifiers = importDescriptor.getIdentifiers();
                if(importIdentifiers.size() != identifiers.size()) 
                    continue;

                for(int j = 0; j < importIdentifiers.size(); j++){
                    if(!identifiers.get(j).equals(importIdentifiers.get(j))){
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "INCOMPATIBLE TYPE");
                        return null;
                    }
                } 
                
                List<Type> importParameters = importDescriptor.getParameters();
                if(importParameters.size() != parameters.size()) {
                    if (i == descriptorsList.size()-1) {
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Wrong number of arguments for import call");
                        return null;
                    }
                    continue;
                }
                
                for(int j = 0; j < importParameters.size(); j++){
                    StringType stringType = new StringType(importParameters.get(j));
                    if(!parameters.get(j).equals(stringType.getString())){
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "INCOMPATIBLE TYPE");
                        return null;
                    }
                } 
                
                // If the import was found, the import type is returned
                Type importType = importDescriptor.getType();
                if(importType == Type.CLASS)
                    return importDescriptor.getClassName();
                
                StringType stringType = new StringType(importType);
                
                return stringType.getString();
                
            }else if(descriptorsList.get(i).getClass() == FunctionDescriptor.class){

                FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptorsList.get(i);
                SymbolsTable parametersTable = functionDescriptor.getParametersTable();
                HashMap<String, List<Descriptor>> functionParameters = parametersTable.getTable();

                if(functionParameters.size() != parameters.size()) {
                    if (i == descriptorsList.size()-1) {
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Wrong number of arguments for function call");
                        return null;
                    }
                    continue;
                }

                int j = 0;
                for(HashMap.Entry<String, List<Descriptor>> functionParametersEntry : functionParameters.entrySet()){
                    List<Descriptor> descList = functionParametersEntry.getValue();
                    
                    if(descList.size() != 1){
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Function can't have more than 1 parameter with the same identifier");
                        return null;
                    }
                    
                    FunctionParameterDescriptor parameterDescriptor = (FunctionParameterDescriptor) descList.get(0);
                
                    Type parameterType = parameterDescriptor.getType();
                    
                    if(parameterType == Type.CLASS){
                        String className = parameterDescriptor.getClassName();
                        if(!className.equals(parameters.get(j))){
                            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Incompatible type for argument in function "+functionDescriptor.getName());
                            return null;
                        }
                    }else{
                        StringType type = new StringType(parameterType);
                        if(!type.getString().equals(parameters.get(j))){
                            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Incompatible type for argument in function "+functionDescriptor.getName());
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
        }
        
        return null;
    }

    private List<String> inspectArguments(SimpleNode argumentsNode, SymbolsTable symbolsTable) throws SemanticErrorException {
        List<String> parameters = new ArrayList<>();
        for(int i = 0; i < argumentsNode.jjtGetNumChildren(); i++){
            SimpleNode argumentNode = (SimpleNode) argumentsNode.jjtGetChild(i);
            if(argumentNode.getId() != JavammTreeConstants.JJTARGUMENT){
                this.semanticError.printError(argumentNode, "Unexpected argument node with id=" + argumentNode.getId());
                return null;
            }
            String parameterType = inspectArgument(argumentNode, symbolsTable);
            parameters.add(parameterType);
        }

        return parameters;
    }

    private String inspectArgument(SimpleNode argumentNode, SymbolsTable symbolsTable) throws SemanticErrorException { 
        return inspectExpression(argumentNode, symbolsTable);
    }

    private String inspectExpression(SimpleNode expressionNode, SymbolsTable symbolsTable) throws SemanticErrorException {
        return inspectExpression(expressionNode, symbolsTable, 0);
    }
    
    private String inspectExpression(SimpleNode expressionNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        /**
         * Must return the argument type after check if it's valid
        */
        if(expressionNode.jjtGetNumChildren() - initialChild == 1) {
            return inspectExpressionSimple(expressionNode, symbolsTable, initialChild);
        }
        return inspectExpressionComplex(expressionNode, symbolsTable, initialChild);
    }

    private String inspectExpressionSimple(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(initialChild);

        switch(node.getId()){
            case JavammTreeConstants.JJTINTEGERLITERAL: {

                // Adding integer to the LLIR Assignment node, if applicable
                if(this.currentLLIRNode instanceof LLIRAssignment) {
                    LLIRAssignment llir = (LLIRAssignment) this.currentLLIRNode;
                    llir.setExpression(new llir.LLIRInteger(Integer.parseInt( node.jjtGetVal() )));
                }

                return "int";
            }
            case JavammTreeConstants.JJTTRUE: 
            case JavammTreeConstants.JJTFALSE: {

                // Adding boolean to the LLIR Assignment node, if applicable
                if(this.currentLLIRNode instanceof LLIRAssignment) {
                    LLIRAssignment llir = (LLIRAssignment) this.currentLLIRNode;
                    llir.setExpression(new llir.LLIRBoolean(Boolean.parseBoolean( node.jjtGetVal() )));
                }

                return "boolean";
            }
            case JavammTreeConstants.JJTIDENTIFIER: {
                List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                if(descriptors == null){
                    this.semanticError.printError(node, "Undefined variable " + node.jjtGetVal());
                    return null;
                }

                if(descriptors.size() > 1){
                    this.semanticError.printError(node, "Argument " + node.jjtGetVal() + " is defined more than once");
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
                        this.semanticError.printError(node, "Variable " + node.jjtGetVal() + " is not initialized");
                    }
                }

                return (new StringType(type)).getString();
            }
            default: {
                //TODO Adicionar mais casos
                this.semanticError.printError(node,"Argument Node ID: " + node.getId()); //Para detetar novos casos 
            }
        }

        this.semanticError.printError(node, "Invalid Argument: " + node.jjtGetVal());
        return null;
    }

    private String inspectExpressionComplex(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        String type = null;

        for(int i = initialChild; i < argumentNode.jjtGetNumChildren(); i++){
            SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(i);
            
            switch(node.getId()){
                case JavammTreeConstants.JJTINTEGERLITERAL: {
                    if(type == null){
                        type = "int";
                    }
                    else if(!type.equals("int")){
                        this.semanticError.printError(node, "INT IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                case JavammTreeConstants.JJTTRUE: 
                case JavammTreeConstants.JJTFALSE: {
                    if(type == null){
                        type = "boolean";
                    } else if(!type.equals("boolean")){
                        this.semanticError.printError(node, "BOOLEAN IS INCOMPATIBLE WITH " + type);
                        return null;
                    }

                    break;
                }
                case JavammTreeConstants.JJTTHIS: {
                    String functionType = inspectFunctionCall(argumentNode, symbolsTable, i+2);
                    if(type == null){
                        type = functionType;
                    }else if(!type.equals(functionType)){
                        this.semanticError.printError(node, functionType + " IS INCOMPATIBLE WITH " + type);
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
                                    this.semanticError.printError(nextNextNode, "Undeclared variable " + node.jjtGetVal());
                                    return null;
                                }
                                boolean foundDescriptor = false;
                                for(int j = 0; j < descriptors.size(); j++){
                                    Descriptor descriptor = descriptors.get(j);
                                    if(descriptor.getClass() == FunctionParameterDescriptor.class || descriptor.getClass() == VariableDescriptor.class){
                                        
                                        TypeDescriptor typeDescriptor = (TypeDescriptor) descriptor;
                                        if(typeDescriptor.getType() != Type.INT_ARRAY && typeDescriptor.getType() != Type.STRING_ARRAY){
                                            this.semanticError.printError(nextNextNode, "Property length only exists in arrays");
                                            return null;
                                        } 
                                        
                                        i += 2;
                                        foundDescriptor = true; // found the function we're looking for
                                        type = "int";
                                        break;
                                    }
                                }
                                
                                if (foundDescriptor)
                                    continue;
                                this.semanticError.printError(node, node.jjtGetVal() + " is not a variable");
                                return null;
                            }

                            //Else function call
                            String functionType = inspectFunctionCall(argumentNode, symbolsTable, i);
                            if(type == null){
                                type = functionType;
                            }else if(!type.equals(functionType)){
                                this.semanticError.printError(nextNextNode, functionType + " IS INCOMPATIBLE WITH " + type);
                                return null;
                            }
                            i += 3; // Jump function identifiers
                        
                            continue;
                        } else if(nextNode.getId() == JavammTreeConstants.JJTARRAY){    
                            String arrayType = inspectArrayAccess(argumentNode, symbolsTable, i);
                            if(type == null){
                                type = arrayType;
                            }else if(!type.equals(arrayType)){
                                this.semanticError.printError(nextNode, arrayType + " IS INCOMPATIBLE WITH " + type);
                                return null;
                            }

                            i += 1; // Jump array nodes
                            continue;
                        }
                    }

                    List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                    if(descriptors == null ){
                        this.semanticError.printError(node, "Argument " + node.jjtGetVal() + " is not defined");
                        return null;
                    }else if(descriptors.size() > 1){
                        this.semanticError.printError(node, "Argument " + node.jjtGetVal() + " is defined more than once");
                        return null;
                    }
                    TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                    
                    Type descriptorType = descriptor.getType();
                    if(descriptorType == Type.CLASS){
                        this.semanticError.printError(node, "OPERATION CAN'T INVOLVE A CLASS TYPE");
                        return null;
                    }

                    String descType = (new StringType(descriptorType)).getString();
                    if(type == null){
                        type = descType;
                    }else if(!type.equals(descType)){
                        this.semanticError.printError(node, descType + " IS INCOMPATIBLE WITH " + type);
                        return null;
                    }

                    if(descriptor.getClass() == VariableDescriptor.class){
                        VariableDescriptor variableDescriptor = (VariableDescriptor) descriptor;
                        if(!variableDescriptor.isInitialized()){
                            this.semanticError.printError(node, "Variable " + node.jjtGetVal() + " is not initialized");
                        }
                    }

                    break;
                }
                case JavammTreeConstants.JJTAND:{
                    if(!type.equals("boolean")){
                        this.semanticError.printError(node, "OPERATION && IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                case JavammTreeConstants.JJTNEW: {
                    if(i != initialChild){
                        this.semanticError.printError(node, "CAN'T INSTANTIATE CLASS INSIDE AN EXPRESSION");
                        return null;
                    }
                    
                    SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);

                    if(nextNode.getId() == JavammTreeConstants.JJTARRAY){
                        SimpleNode idNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+1);
                        if(idNode.jjtGetVal() != "int"){
                            this.semanticError.printError(idNode, "CAN ONLY INSTANTIATE ARRAY OF INT");
                            return null;
                        }
                        
                        SimpleNode arrayNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);
                    
                        String indexType = inspectExpression(arrayNode, symbolsTable);

                        if(indexType == null){
                            this.semanticError.printError(arrayNode, "Array index must be an int");
                            return null;
                        } else if(!indexType.equals("int")){
                            this.semanticError.printError(arrayNode, "Array index must be an int");
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
                        this.semanticError.printError(node, "OPERATION ! IS INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
                //TODO Check this  
                case JavammTreeConstants.JJTLESS: {
                    if(!type.equals("int")){
                        this.semanticError.printError(node, "CAN'T COMPARE " + type + " WITH OPERATOR <");
                        return null;
                    }
                    
                    String otherType = inspectExpression(argumentNode, symbolsTable, i+1);
                    if(!otherType.equals("int")){
                        this.semanticError.printError(node, "CAN'T COMPARE " + otherType + " WITH OPERATOR <");
                        return null;
                    }

                    return "boolean";
                }
                case JavammTreeConstants.JJTDOT: {
                    this.semanticError.printError(node, "CAN'T ACCESS PROPERTY/METHOD OF VARIABLE OF TYPE " + type);
                    return null;
                }
                case JavammTreeConstants.JJTPARENTHESESEXPRESSION: {
                    String expressionType = inspectExpression(node, symbolsTable);
                    if(type == null){
                        type = expressionType;
                    }else if(!type.equals(expressionType)){
                        this.semanticError.printError(node, ""); //TODO Add print message
                    }
                    break;
                }
                default:{ //Plus, Minus, ...
                    if(!type.equals("int")){
                        this.semanticError.printError(node, "OPERATIONS ARE INCOMPATIBLE WITH " + type);
                        return null;
                    }
                    break;
                }
            }
        }
        
        return type;
    }

    private String inspectClassInstantiation(SimpleNode node, SymbolsTable symbolsTable) throws SemanticErrorException {
        return inspectClassInstantiation(node, symbolsTable, 0);
    }

    private String inspectClassInstantiation(SimpleNode node, SymbolsTable symbolsTable, int newPosition) throws SemanticErrorException {
        SimpleNode classIdentifierNode = (SimpleNode) node.jjtGetChild(newPosition+1);
                        
        // Get all the Class constructors
        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(classIdentifierNode.jjtGetVal());  
        SimpleNode argumentsNode = (SimpleNode) node.jjtGetChild(newPosition+2);

        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        
        if(parameters.size() == 0 && descriptorsList.size() == 1){
            Descriptor descriptor = descriptorsList.get(0);
            if(descriptor.getClass() == ClassDescriptor.class){
                return classIdentifierNode.jjtGetVal();
            }
        }

        for(int i = 0; i < descriptorsList.size(); i++){
            Descriptor descriptor = descriptorsList.get(i);
            if(descriptor.getClass() == ImportDescriptor.class){
                ImportDescriptor importDescriptor = (ImportDescriptor) descriptor;
                if(importDescriptor.getIdentifiers().size() != 1)
                    continue;
                
                List<Type> importParameters = importDescriptor.getParameters();
                if(importParameters.size() != parameters.size())
                    continue;
                
                for(int j = 0; j < importParameters.size(); j++){
                    StringType stringType = new StringType(importParameters.get(j));
                    if(!parameters.get(j).equals(stringType.getString())){
                        this.semanticError.printError(argumentsNode, "INCOMPATIBLE TYPE");
                        return null;
                    }
                } 
                
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
                            this.semanticError.printError(argumentsNode, "INCOMPATIBLE TYPE");
                            return null;
                        }
                    }else{
                        StringType type = new StringType(parameterType);
                        if(!type.getString().equals(parameters.get(j))){
                            this.semanticError.printError(argumentsNode, "INCOMPATIBLE TYPE");
                            return null;
                        }
                    }
                }

                return classIdentifierNode.jjtGetVal();
            }
        }

        this.semanticError.printError(classIdentifierNode, "Class " + classIdentifierNode.jjtGetVal() + " doesn't exist");
        return null;
    }
}