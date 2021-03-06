import llir.*;
import symbols.*;

import java.util.*;

import codeGeneration.CodeWriter.ArithmeticTransformer;

public class TableGenerator {
    SimpleNode rootNode;
    SymbolsTable symbolsTable;
    SemanticError semanticError;
    FunctionDescriptor currentFunctionDescriptor;
    LLIRPopulator llirPopulator;
    private String className;
    private boolean initializedWarning;
    private HashSet<VariableDescriptor> initializedIfVars = new HashSet<>();
    private HashSet<VariableDescriptor> initializedElseVars = new HashSet<>();
    private HashSet<VariableDescriptor> possibleWarningVars = new HashSet<>();
    private boolean insideStaticMethod;

    public TableGenerator(SimpleNode rootNode, boolean initializedWarning){
        this.rootNode = rootNode;
        this.symbolsTable = new SymbolsTable();
        this.semanticError = new SemanticError();
        this.llirPopulator = new LLIRPopulator();
        this.className = null;
        this.initializedWarning = initializedWarning;
        this.insideStaticMethod = false;
    }

    public SymbolsTable getTable() {
        return this.symbolsTable;
    }

    public int getNumErrors() {
        return this.semanticError.getNumErrors();
    }

    public void build() throws SemanticErrorException {
        int i = 0;
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

    //Inspects Imports and correponding expressions
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
            //Checks Import arguments
            else if (child.getId() == JavammTreeConstants.JJTIMPORTARGS) {
                
                for(int j = 0; j < child.jjtGetNumChildren(); j++) {
                    SimpleNode grandChild = (SimpleNode) child.jjtGetChild(j);
                    if(grandChild.jjtGetVal().equals("void"))
                        continue;
                    TypeString typeString = new TypeString(grandChild.jjtGetVal());
                    importDescriptor.addParameter(typeString.parseType());
                }
            }
            //Checks the return from the import
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

    //Inspects the class of the file
    public void inspectClass(SimpleNode classNode) throws SemanticErrorException {
        ClassDescriptor classDescriptor = new ClassDescriptor();
        
        SimpleNode childNode = (SimpleNode) classNode.jjtGetChild(0);
        this.className = childNode.jjtGetVal();
        classDescriptor.setName(this.className);
        this.symbolsTable.addSymbol(classDescriptor.getName(), classDescriptor, false);

        SymbolsTable classVariablesTable = classDescriptor.getVariablesTable();
        classVariablesTable.setParent(this.symbolsTable);

        List<SimpleNode> functionsNodes = new ArrayList<>();
        List<SimpleNode> variablesNodes = new ArrayList<>();
        List<FunctionDescriptor> functions = new ArrayList<>(); 
        List<VariableDescriptor> variables = new ArrayList<>();

        // Collect attributes and methods access, return type and name 
        for (int i = 0; i < classNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) classNode.jjtGetChild(i);

            //Checks all the variable declarations
            if (child.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
                VariableDescriptor variableDescriptor = inspectVariable(child);
                classDescriptor.addVariable(variableDescriptor);
                variables.add(variableDescriptor);
                variablesNodes.add(child);
            }

            //Checks all the Method Declaration
            else if (child.getId() == JavammTreeConstants.JJTMETHODDECLARATION) {
                FunctionDescriptor functionDescriptor = inspectFunctionHeader(child);
                functionDescriptor.getParametersTable().setParent(classDescriptor.getFunctionsTable());
                classDescriptor.addFunction(functionDescriptor.getName(),functionDescriptor);
                functionsNodes.add(child);
                functions.add(functionDescriptor);
            }

            //Checks The extended class
            else if (child.getId() == JavammTreeConstants.JJTEXTENDS) {
                String parentClassName = ((SimpleNode) (classNode.jjtGetChild(i+1))).jjtGetVal();
                classDescriptor.setParentClass(parentClassName);
                extendClass(classDescriptor);
            }
        }

        SymbolsTable symbolsTable = classDescriptor.getVariablesTable();
        for(int i = 0; i < variablesNodes.size(); i++){
            inspectVariableType(variablesNodes.get(i), symbolsTable, variables.get(i));
        } 

        for(int i = 0; i < functionsNodes.size(); i++){
            inspectFunctionBody(functionsNodes.get(i), functions.get(i));
        } 
    }

    //Function to examine the extended class
    private void extendClass(ClassDescriptor classDescriptor){
        // Get imported methods from the extedended class
        LinkedHashMap<String, List<Descriptor>> importsTable = this.symbolsTable.getTable();
        for(HashMap.Entry<String, List<Descriptor>> imports : importsTable.entrySet()){
            for(int j = 0; j < imports.getValue().size(); j++){
                Descriptor descriptor = imports.getValue().get(j);
                
                // Found an import descriptor
                if(descriptor.getClass() == ImportDescriptor.class){
                    ImportDescriptor importDescriptor = (ImportDescriptor) descriptor;
                    ArrayList<String> identifiers = importDescriptor.getIdentifiers();
                    
                    // Found method from the extended class
                    if(identifiers.size() > 1 && identifiers.get(0).equals(classDescriptor.getParentClass())){
                        FunctionDescriptor functionDescriptor = new FunctionDescriptor();

                        // Set function name
                        functionDescriptor.setName(identifiers.get(identifiers.size()-1));
                        
                        // Make function static
                        if(importDescriptor.isStatic()){
                            functionDescriptor.makeStatic();
                        }
                        // Add function return
                        StringType returnType = new StringType(importDescriptor.getReturn());
                        functionDescriptor.setReturnValue(returnType.getString());

                        // Add function parameters
                        ArrayList<Type> parameters = importDescriptor.getParameters();
                        for(int k = 0; k < parameters.size(); k++){
                            functionDescriptor.addParameter(new FunctionParameterDescriptor(new String(new char[1]).replace('\0', (char)('a'+k)), parameters.get(k)));
                        }

                        functionDescriptor.setExtended();

                        classDescriptor.addFunction(functionDescriptor.getName(), functionDescriptor);
                    }
                }
            }
        }
    }


    //Inspects a single variable
    public VariableDescriptor inspectVariable(SimpleNode variableNode)  {
        VariableDescriptor variableDescriptor = new VariableDescriptor();

        for (int i = 0; i < variableNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) variableNode.jjtGetChild(i);

            if (child.getId() == JavammTreeConstants.JJTTYPE) {
                TypeString typeString = new TypeString(child.jjtGetVal());
                variableDescriptor.setType(typeString.parseType());
                if (typeString.parseType() == Type.CLASS)
                    variableDescriptor.setClassName(child.jjtGetVal());
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

    //Inspects the Variable Type
    public void inspectVariableType(SimpleNode varNode, SymbolsTable symbolsTable, VariableDescriptor variableDescriptor) throws SemanticErrorException {
        if (variableDescriptor.getType() == Type.CLASS) {
            String className = variableDescriptor.getClassName();

            List<Descriptor> descriptors = symbolsTable.getDescriptor(className);
            if (descriptors == null) {
                this.semanticError.printError(varNode, "Wrong type in variable declaration.");
            }
        }
    }


    //Deals with the function considering if it is the main function or not
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

    //Inspects the Main Header
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

    //Inspect normal function heafer
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

    //Inspects Funtion arguments
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

    //Inspect function body, depending if it is the main function or not
    public void inspectFunctionBody(SimpleNode functionNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        SimpleNode child = (SimpleNode) functionNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            insideStaticMethod = true;
            inspectMainBody(child, functionDescriptor);
            insideStaticMethod = false;
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            insideStaticMethod = functionDescriptor.isStatic();
            inspectMethodBody(functionNode, functionDescriptor);
            insideStaticMethod = false;
        }
    }

    //Inspects the body of the main function
    public void inspectMainBody(SimpleNode mainNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        this.currentFunctionDescriptor = functionDescriptor;

        //Main Body Parser
        for (int i = 1; i < mainNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) mainNode.jjtGetChild(i);
            inspectVariableAndStatement(child, functionDescriptor);
        }
    }

    //Inspects the Return of a function
    public void inspectReturn(SimpleNode node, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        if(node.jjtGetNumChildren() == 0){
            if(functionDescriptor.getReturnValue() != Type.VOID){
                this.semanticError.printError(node, "Function of type void can't return anything");
            }
        }else{
            String returnType = inspectExpression(node, functionDescriptor.getBodyTable()); 
            if(returnType == null){
                this.semanticError.printError(node, "Can't evalute return expression");
                return;
            }

            if(functionDescriptor.getReturnValue() == Type.CLASS){
                if(!returnType.equals(functionDescriptor.getClassName())){
                    this.semanticError.printError(node, "Function of type " + functionDescriptor.getClassName() + " can't return type " + returnType);
                }   
            }else{
                StringType stringType = new StringType(functionDescriptor.getType());
                if(!returnType.equals(stringType.getString())){
                    this.semanticError.printError(node, "Function of type " + stringType.getString() + " can't return type " + returnType);
                }
            }
        }
    }

    //Inspects all the methods except main
    public void inspectMethodBody(SimpleNode methodNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        this.currentFunctionDescriptor = functionDescriptor;
        //Cycle can have MethodHeader, LineStatement return IntegralLiteral
        for (int i = 1; i < methodNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) methodNode.jjtGetChild(i);

            //Method Body Parser 
            if (child.getId() == JavammTreeConstants.JJTRETURN){
                LLIRReturn llirReturn = new LLIRReturn();
                llirPopulator.addLLIR(llirReturn);
                llirReturn.setReturnType(functionDescriptor.getReturnValue());
                inspectReturn(child, functionDescriptor);
                llirPopulator.popReturn();
                this.currentFunctionDescriptor.addLLIRNode(this.llirPopulator.popLLIR());
            }
            else inspectVariableAndStatement(child, functionDescriptor);
        }
    }

    //Inspects Variable declarations and statements
    public void inspectVariableAndStatement(SimpleNode variableAndStatementNode, FunctionDescriptor functionDescriptor) throws SemanticErrorException {
        if (variableAndStatementNode.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
            VariableDescriptor variableDescriptor = inspectVariable(variableAndStatementNode);
            functionDescriptor.addBodyVariable(variableDescriptor.getName(),variableDescriptor);
            inspectVariableType(variableAndStatementNode, functionDescriptor.getBodyTable(), variableDescriptor);
        } else if (variableAndStatementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
            inspectLineStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        } else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
            inspectWhileStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        } else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
            inspectIfStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
            setVarsInitialized(functionDescriptor.getBodyTable());
            setVarsNonInitialized(functionDescriptor.getBodyTable());
            this.initializedIfVars.clear();
            this.initializedElseVars.clear();
        }else{
            this.semanticError.printError(variableAndStatementNode, "Unknown symbol");
        }
    }

    //Inspects a line of the file
    public void inspectLineStatement(SimpleNode statementNode, SymbolsTable symbolsTable) throws SemanticErrorException {
        inspectLineStatement(statementNode, symbolsTable, false, false);
    }

    //Inspects a line of the file
    public void inspectLineStatement(SimpleNode statementNode, SymbolsTable symbolTable, boolean isIf, boolean isElse) throws SemanticErrorException {
        if(statementNode.jjtGetNumChildren() < 3){
            this.semanticError.printError(statementNode, "Invalid Line Statement");
            return;
        }

        SimpleNode firstChild = (SimpleNode) statementNode.jjtGetChild(0);
        
        if(firstChild.getId() != JavammTreeConstants.JJTIDENTIFIER && firstChild.getId() != JavammTreeConstants.JJTTHIS){
            this.semanticError.printError(firstChild, "First Node of Line Statement must be an Identifier");
            return;
        }

        if(firstChild.getId() == JavammTreeConstants.JJTTHIS && this.insideStaticMethod){
            this.semanticError.printError(firstChild, "Non-static variable this cannot be referenced from a static context");
            return;
        }

        SimpleNode secondChild = (SimpleNode) statementNode.jjtGetChild(1);

        //If it is an assignment
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
            // Add a new LLIR Node (Assignment) to the function
            this.llirPopulator.addAssignment(new LLIRAssignment());

            Type type = typeDescriptor.getType();
            String typeString;
            if(type == Type.CLASS){
                typeString = typeDescriptor.getClassName();
            }else{
                StringType strType = new StringType(type);
                typeString = strType.getString();

                //Sets assignment variable if it is a Named type descriptor
                NamedTypeDescriptor variableDescriptor = (NamedTypeDescriptor) typeDescriptor;
                this.llirPopulator.setAssignmentVariable(new LLIRVariable(variableDescriptor));
            }

            inspectAssignment(statementNode, symbolTable, typeString);

            this.llirPopulator.popBeforeAssignment();


            if(typeDescriptor.getClass() == VariableDescriptor.class){
                VariableDescriptor variableDescriptor = (VariableDescriptor) typeDescriptor;
                if (!variableDescriptor.isInitialized() || !variableDescriptor.wasInitializedPreviously()) {
                    if (isElse) {
                        this.initializedElseVars.add(variableDescriptor);
                        variableDescriptor.setInitializedInIf();
                    }
                    else if (isIf) {
                        this.initializedIfVars.add(variableDescriptor);
                        variableDescriptor.setInitializedInIf();
                    }
                    if (isIf)
                        variableDescriptor.setInitializedPreviously();
                }
                variableDescriptor.setInitialized();
                this.llirPopulator.setAssignmentVariable(new LLIRVariable(variableDescriptor));
            }

            this.llirPopulator.addStatement(currentFunctionDescriptor);
        }

        //If it is an array
        else if(secondChild.getId() == JavammTreeConstants.JJTARRAY) {
            List<Descriptor> firstDescriptorList = symbolTable.getDescriptor(firstChild.jjtGetVal());
            if(firstDescriptorList == null){
                this.semanticError.printError(firstChild, "Variable "+firstChild.jjtGetVal()+" not declared");
                return;
            }

            NamedTypeDescriptor typeDescriptor = null;
            if(firstDescriptorList.size() == 1){
                typeDescriptor = (NamedTypeDescriptor) firstDescriptorList.get(0);
            }else{
                for(int i = 0; i < firstDescriptorList.size(); i++){
                    if(firstDescriptorList.get(i).getClass() == VariableDescriptor.class){
                        typeDescriptor = (NamedTypeDescriptor) firstDescriptorList.get(i);
                        break;
                    }
                }
                if(typeDescriptor == null){
                    this.semanticError.printError(firstChild, "Variable " + firstChild.jjtGetVal()+" doesn't have a type");
                    return;
                }
            }

            if(typeDescriptor.getClass() == VariableDescriptor.class){
                VariableDescriptor varDescriptor = (VariableDescriptor)typeDescriptor;
                if (!varDescriptor.isInitialized() && !varDescriptor.isInitializedInIf())
                    this.semanticError.printError(firstChild, "Array " + firstChild.jjtGetVal()+" was not initialized");
            }

            //Assignment of an array
            this.llirPopulator.addAssignment(new LLIRAssignment());
            Type type = typeDescriptor.getType();
            if(type != Type.STRING_ARRAY && type != Type.INT_ARRAY){
                this.semanticError.printError(firstChild, "Variable " + firstChild.jjtGetVal()+" is not an array");
                return;   
            }

            //Populates LLIR
            LLIRArrayAccess arrayAccess = new LLIRArrayAccess();
            arrayAccess.setVariable(typeDescriptor);
            arrayAccess.setArray(new LLIRVariable(typeDescriptor));
            this.llirPopulator.addExpression(arrayAccess);


            SimpleNode arrayNode =  (SimpleNode) statementNode.jjtGetChild(1);
            String indexType = inspectExpression(arrayNode, symbolTable);
            if(!indexType.equals("int")){
                this.semanticError.printError(statementNode, "Array index must be an int");
                return;
            }

            this.llirPopulator.popArrayAcessExpression();

            this.llirPopulator.setAssignmentVariable((LLIRArrayAccess) this.llirPopulator.popLLIR());


            String typeString;
            if(type == Type.STRING_ARRAY){
                typeString = "String";
            }else{
                typeString = "int";
            }

            inspectAssignment(statementNode, symbolTable, typeString, 3);
            this.llirPopulator.popBeforeAssignment();

            //If it is a variable Descriptor
            if(typeDescriptor.getClass() == VariableDescriptor.class){
                VariableDescriptor variableDescriptor = (VariableDescriptor) typeDescriptor;
                if (!variableDescriptor.isInitialized() || !variableDescriptor.wasInitializedPreviously()) {
                    if (isElse) {
                        this.initializedElseVars.add(variableDescriptor);
                        variableDescriptor.setInitializedInIf();
                    }
                    else if (isIf) {
                        this.initializedIfVars.add(variableDescriptor);
                        variableDescriptor.setInitializedInIf();
                    }
                    if (isIf)
                        variableDescriptor.setInitializedPreviously();
                }
                variableDescriptor.setInitialized();
            }
            this.llirPopulator.addStatement(currentFunctionDescriptor);
        }
        else{
            //Function call
            this.llirPopulator.addMethodCall(new LLIRMethodCall(true));
            inspectFunctionCall(statementNode, symbolTable);
            llirPopulator.popFunctionCallFunction();

            //If empty add to function Descriptor
            if (llirPopulator.getLlirStack().size() == 1 && !(llirPopulator.peek() instanceof LLIRIfElseBlock) && !(llirPopulator.peek() instanceof LLIRWhileBlock)){
                this.llirPopulator.addStatement(currentFunctionDescriptor);
            }

        }
    }

    public void inspectWhileStatement(SimpleNode whileNode, SymbolsTable statementParentTable) throws SemanticErrorException {
        inspectWhileStatement(whileNode, statementParentTable, false);
    }


    //Inspect Body of a while block
    public void inspectWhileStatement(SimpleNode whileNode, SymbolsTable statementParentTable, boolean insideIf) throws SemanticErrorException {
        if(whileNode.jjtGetNumChildren() == 0){
            this.semanticError.printError(whileNode, "While needs to have an expression.");
            return;
        }
        
        SimpleNode whileExpression = (SimpleNode) whileNode.jjtGetChild(0);
        if(whileExpression.getId() != JavammTreeConstants.JJTWHILEEXPRESSION){
            this.semanticError.printError(whileExpression, "While needs to have an expression.");
            return;
        }

        //Adds to the LLIR Populator
        this.llirPopulator.addLLIR(new LLIRWhileBlock());

        String expressionType = inspectExpression(whileExpression, statementParentTable);
        if(expressionType == null){
            this.semanticError.printError(whileExpression, "Can't process while statement due to invalid expression");
            return;
        }else if(!expressionType.equals("boolean")){
            this.semanticError.printError(whileExpression, "While expression must evaluate to a boolean");
            return;
        }
        this.llirPopulator.popBlockExpression();

        BlockDescriptor blockDescriptor = new BlockDescriptor(statementParentTable);

        //Iterates through all the statements of the block
        for(int i = 1; i < whileNode.jjtGetNumChildren(); i++){
            SimpleNode statementNode = (SimpleNode) whileNode.jjtGetChild(i);

            if (statementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
                inspectLineStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
                inspectWhileStatement(statementNode, blockDescriptor.getLocalTable());
            } else if(statementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
                inspectIfStatement(statementNode, blockDescriptor.getLocalTable());
                if (!insideIf) {
                    setVarsInitialized(blockDescriptor.getLocalTable());
                    setVarsNonInitialized(blockDescriptor.getLocalTable());
                    this.initializedIfVars.clear();
                    this.initializedElseVars.clear();
                }
            }else{
                this.semanticError.printError(statementNode, "Unknown symbol");
            }
        }
        this.llirPopulator.addStatement(currentFunctionDescriptor);
    }

    public void inspectIfStatement(SimpleNode ifNode, SymbolsTable statementParentTable) throws SemanticErrorException {
        inspectIfStatement(ifNode, statementParentTable, true);
    }

    //Inspect If Statement
    public void inspectIfStatement(SimpleNode ifNode, SymbolsTable statementParentTable, boolean previousFlow) throws SemanticErrorException {
        LLIRIfElseBlock ifElseBlock = new LLIRIfElseBlock();
        this.llirPopulator.addLLIR(ifElseBlock);

        if(ifNode.jjtGetNumChildren() == 0){
            this.semanticError.printError(ifNode, "If needs to have an expression.");
            return;
        }
        SimpleNode ifExpression = (SimpleNode) ifNode.jjtGetChild(0);
        if(ifExpression.getId() != JavammTreeConstants.JJTIFEXPRESSION){
            this.semanticError.printError(ifExpression, "If needs to have an expression.");
            return;
        }

        //Inspects if expression
        String expressionType = inspectExpression(ifExpression, statementParentTable);

        this.llirPopulator.popBlockExpression();

        if(expressionType == null){
            this.semanticError.printError(ifExpression, "Can't process if statement due to invalid expression");
            return;
        } else if(!expressionType.equals("boolean")){
            this.semanticError.printError(ifExpression, "If expression must evaluate to a boolean");
            return;
        }

        BlockDescriptor blockDescriptor = new BlockDescriptor(statementParentTable);

        boolean found_else = false;

        //Iterates through all the statements of the block
        for(int i = 1; i < ifNode.jjtGetNumChildren(); i++){
            SimpleNode statementNode = (SimpleNode) ifNode.jjtGetChild(i);

            if (statementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
                inspectLineStatement(statementNode, blockDescriptor.getLocalTable(), true, found_else);
            } else if(statementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT){
                inspectWhileStatement(statementNode, blockDescriptor.getLocalTable(), true);
                //Checks if it is an If statement
            } else if(statementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
                
                HashSet<VariableDescriptor> initializedElseVarsLocal = new HashSet<>(this.initializedElseVars);
                this.initializedElseVars.clear();
                HashSet<VariableDescriptor> initializedIfVarsLocal = new HashSet<>(this.initializedIfVars);
                this.initializedIfVars.clear();
                inspectIfStatement(statementNode, blockDescriptor.getLocalTable(), found_else);
                this.initializedElseVars.addAll(initializedElseVarsLocal);
                Iterator<VariableDescriptor> it = this.initializedElseVars.iterator();
                while (it.hasNext()) {
                    VariableDescriptor var = it.next();
                    if (var.isInitializedInIf() || var.wasInitializedPreviously()) {
                        var.setNonInitialized();
                    }
                }
                this.initializedIfVars.addAll(initializedIfVarsLocal);
                it = this.initializedIfVars.iterator();
                while (it.hasNext()) {
                    VariableDescriptor var = it.next();
                    if (var.isInitializedInIf() || var.wasInitializedPreviously()) {
                        var.setNonInitialized();
                    }
                }
            //Checks if it is a Else Block
            } else if(statementNode.getId() == JavammTreeConstants.JJTELSE && !found_else){
                ifElseBlock.setFoundElse(true);
                found_else = true;

                Iterator<VariableDescriptor> it = this.initializedIfVars.iterator();
                while (it.hasNext()) {
                    VariableDescriptor var = it.next();
                    if (var.isInitializedInIf() || var.wasInitializedPreviously())
                        var.setNonInitialized();
                }
                continue;
            } else{
                this.semanticError.printError(statementNode, "Unknown symbol "+statementNode.getId());
            }
        }

        handleIfVarsInitializations(previousFlow);
    }

    //Handles Variables initializations
    private void handleIfVarsInitializations(boolean isElse) throws SemanticErrorException {
        HashSet<VariableDescriptor> initializedIfVarsLocal = new HashSet<>();
        HashSet<VariableDescriptor> initializedElseVarsLocal = new HashSet<>();

        Iterator<VariableDescriptor> i = this.initializedIfVars.iterator();
        while (i.hasNext()) {
            VariableDescriptor ifVar = i.next();
            if (this.initializedElseVars.contains(ifVar)) {
                i.remove();
                this.initializedElseVars.remove(ifVar);
                initializedIfVarsLocal.add(ifVar);
                initializedElseVarsLocal.add(ifVar);
            }
        }
        
        Iterator<VariableDescriptor> k = this.initializedIfVars.iterator();
        Iterator<VariableDescriptor> l = this.initializedElseVars.iterator();
        while (k.hasNext()) {
            VariableDescriptor ifVar = k.next();
            this.possibleWarningVars.add(ifVar);
        }
        while (l.hasNext()) {
            VariableDescriptor elseVar = l.next();
            this.possibleWarningVars.add(elseVar);
        }

        if (isElse)
            this.initializedElseVars = initializedElseVarsLocal;
        else this.initializedIfVars = initializedIfVarsLocal;
        this.llirPopulator.popBlock();


        this.llirPopulator.addStatement(currentFunctionDescriptor);

    }

    //Inspects array access
    private String inspectArrayAccess(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SimpleNode idNode = (SimpleNode) statementNode.jjtGetChild(initialChild);
        List<Descriptor> descriptors = symbolsTable.getDescriptor(idNode.jjtGetVal());

        NamedTypeDescriptor idDescriptor = null;
        for(int i = 0; i < descriptors.size(); i++){
            Descriptor d = descriptors.get(i);
            if(d.getClass() == FunctionParameterDescriptor.class || d.getClass() == VariableDescriptor.class){
                idDescriptor = (NamedTypeDescriptor) d;
                if(idDescriptor.getType() != Type.INT_ARRAY && idDescriptor.getType() != Type.STRING_ARRAY){
                    this.semanticError.printError(idNode, "Can only access arrays of int or string");
                    return null;
                }
                if (d.getClass() == VariableDescriptor.class) {
                    VariableDescriptor variableDescriptor = (VariableDescriptor)d;
                    if (!variableDescriptor.isInitializedInFunction()) {
                        System.err.println("Warning: Variable " + idNode.jjtGetVal() + " might not have been initialized\n");
                    }
                }
                this.llirPopulator.addVariable(new LLIRVariable(idDescriptor));
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

        if(idDescriptor.getType() == Type.INT_ARRAY)
            return "int";
        return "String";
    }


    //Inspects assignment
    private void inspectAssignment(SimpleNode statementNode, SymbolsTable symbolsTable, String type) throws SemanticErrorException {
        inspectAssignment(statementNode, symbolsTable, type, 2);
    }
    //Inspects assignment
    private void inspectAssignment(SimpleNode statementNode, SymbolsTable symbolsTable, String type, int initialChild) throws SemanticErrorException {
        String expType = inspectExpression(statementNode, symbolsTable, initialChild);

        if(expType == null){
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(2), "Can't assign invalid type");
        } else if(!type.equals(expType)){
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(2), "Can't assign " + expType + " to variable of type " + type);
        }
    }

    //Checks return type of a method call
    private String checkFunctionCallVariableType(String identifierName, SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild)  throws SemanticErrorException {
        List<Descriptor> nodeDescriptors = symbolsTable.getDescriptor(identifierName);
        if(nodeDescriptors != null){
            for(int i = 0; i < nodeDescriptors.size(); i++){
                if(nodeDescriptors.get(i).getClass() == VariableDescriptor.class || nodeDescriptors.get(i).getClass() == FunctionParameterDescriptor.class){
                    TypeDescriptor typeDescriptor = (TypeDescriptor) nodeDescriptors.get(i);
                    if(typeDescriptor.getType() != Type.CLASS){
                        this.semanticError.printError(statementNode, "Can only call functions from classes");
                        return null;
                    }
                    if (typeDescriptor.getClass() == VariableDescriptor.class) {
                        VariableDescriptor variableDescriptor = (VariableDescriptor)typeDescriptor;
                        if (!variableDescriptor.isInitializedInFunction()) {
                            System.err.println("Warning: Variable " + identifierName + " might not have been initialized\n");
                        }
                    }
                    if(this.llirPopulator.peek() instanceof LLIRMethodCall){
                        LLIRMethodCall methodCall = (LLIRMethodCall) this.llirPopulator.peek();
                        methodCall.setClassType(typeDescriptor.getClassName());
                        if(typeDescriptor instanceof VariableDescriptor)
                            methodCall.setClassName(((VariableDescriptor)typeDescriptor).getName());
                        else if(typeDescriptor instanceof FunctionParameterDescriptor)
                            methodCall.setClassName(((FunctionParameterDescriptor)typeDescriptor).getName());
                    }
                    return typeDescriptor.getClassName();
                }
            }
        }
        return identifierName;
    }

    //Inspects function call
    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable) throws SemanticErrorException {
        return inspectFunctionCall(statementNode, symbolsTable, 0);
    }

    //Inspects function call
    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        return inspectFunctionCall(statementNode, symbolsTable, initialChild, false);
    }

    //Inspects function call
    private String inspectFunctionCall(SimpleNode statementNode, SymbolsTable symbolsTable, int initialChild, boolean imported) throws SemanticErrorException {
        
        List<String> identifiers = new ArrayList<>(); 
        SimpleNode node = (SimpleNode) statementNode.jjtGetChild(initialChild);

        if(node.getId() == JavammTreeConstants.JJTTHIS){
            ((LLIRMethodCall) this.llirPopulator.peek()).setClassType(this.className);
        }

        SimpleNode child = (SimpleNode) statementNode.jjtGetChild(initialChild+1);
        int nextChild = initialChild+1;

        //If the function is Imported
        if (imported){
            if (child.getId() == JavammTreeConstants.JJTARGUMENTS) {
                child = (SimpleNode) statementNode.jjtGetChild(initialChild+2);
                nextChild++;
                initialChild++;
            }
        }


        if(child.getId() == JavammTreeConstants.JJTDOT){
            String identifierName = checkFunctionCallVariableType(node.jjtGetVal(), statementNode, symbolsTable, initialChild);

            if(!this.className.equals(identifierName)){
                identifiers.add(identifierName); 
            }
            node = (SimpleNode) statementNode.jjtGetChild(initialChild+2);
            identifiers.add(node.jjtGetVal()); 
            nextChild = initialChild+3;
        }else{
            identifiers.add(node.jjtGetVal());
        }

        SimpleNode argumentsNode = (SimpleNode) statementNode.jjtGetChild(nextChild);
        if(argumentsNode.getId() != JavammTreeConstants.JJTARGUMENTS){
            this.semanticError.printError(argumentsNode, "Unexpected node with id=" + argumentsNode.getId());
            return null;
        }

        //Inspects the arguments of the function
        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        llirPopulator.popArguments(parameters.size());

        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(((SimpleNode)statementNode.jjtGetChild(nextChild-1)).jjtGetVal());
        if (descriptorsList == null) {
            this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Function "+((SimpleNode)statementNode.jjtGetChild(nextChild-1)).jjtGetVal()+" not declared.");
            return null;
        }

        //Iterates through all the descriptors
        for(int i = 0; i < descriptorsList.size(); i++){

            //If it is an import
            if(descriptorsList.get(i).getClass()  == ImportDescriptor.class){

                ImportDescriptor importDescriptor = (ImportDescriptor) descriptorsList.get(i);

                boolean empty = false;

                if(!llirPopulator.getLlirStack().empty()){
                    empty = true;
                }

                ArrayList<String> importIdentifiers = importDescriptor.getIdentifiers();

                if(importIdentifiers.size() != identifiers.size()) {
                    continue;
                }
                for(int j = 0; j < importIdentifiers.size(); j++){
                    if(!identifiers.get(j).equals(importIdentifiers.get(j))){
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Incompatible type");
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
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Incompatible type");
                        return null;
                    }
                } 
                
                // If the import was found, the import type is returned
                Type importType = importDescriptor.getType();
                
                if(importType == Type.CLASS) 
                    return importDescriptor.getClassName();

                StringType stringType = new StringType(importType);
                this.llirPopulator.addImport(new LLIRImport(importDescriptor));


                return stringType.getString();                
            }
            //When we are sure it is a function
            else if(descriptorsList.get(i).getClass() == FunctionDescriptor.class){

                FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptorsList.get(i);
                SymbolsTable parametersTable = functionDescriptor.getParametersTable();

                if(this.llirPopulator.lastIsFunctionCall()){
                    LLIRMethodCall methodCall= ((LLIRMethodCall) this.llirPopulator.getLlirStack().peek());
                    methodCall.setMethodName(functionDescriptor.getName());
                    methodCall.setParametersTable(parametersTable);
                    methodCall.setReturnType(functionDescriptor.getType());
                }

                HashMap<String, List<Descriptor>> functionParameters = parametersTable.getTable();

                if(functionParameters.size() != parameters.size()) {
                    if (i == descriptorsList.size()-1) {
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Wrong number of arguments for function call");
                        return null;
                    }
                    continue;
                }

                int j = 0;

                //Iterates through all the parameters
                for(HashMap.Entry<String, List<Descriptor>> functionParametersEntry : functionParameters.entrySet()){
                    List<Descriptor> descList = functionParametersEntry.getValue();
                    
                    if(descList.size() != 1){
                        this.semanticError.printError((SimpleNode)statementNode.jjtGetChild(nextChild-1), "Function can't have more than 1 parameter with the same identifier");
                        return null;
                    }
                    FunctionParameterDescriptor parameterDescriptor = (FunctionParameterDescriptor) descList.get(0);
                
                    Type parameterType = parameterDescriptor.getType();

                    //If parameter is a class variable
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

                if(functionType == Type.CLASS) return functionDescriptor.getClassName();
                
                StringType stringType = new StringType(functionType);
                return stringType.getString();
            }
        }
        
        return null;
    }

    //Inspect Function and Import arguments
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

    //Inspects Expression if it simple
    private String inspectExpressionSimple(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(initialChild);

        switch(node.getId()){
            //Inspects if it is an Integer
            case JavammTreeConstants.JJTINTEGERLITERAL: {

                LLIRInteger llirInteger = new LLIRInteger(Integer.parseInt( node.jjtGetVal()));
                this.llirPopulator.addExpression(llirInteger);

                return "int";
            }
            //Inspects expression it has the value "true"
            case JavammTreeConstants.JJTTRUE:
            {
                this.llirPopulator.addExpression(new llir.LLIRBoolean(true));
                return "boolean";
            }
            //Inspects expression if it is "false"
            case JavammTreeConstants.JJTFALSE: {
                this.llirPopulator.addExpression(new llir.LLIRBoolean(false));
                return "boolean";
            }
            case JavammTreeConstants.JJTIDENTIFIER: {
                List<Descriptor> descriptors = symbolsTable.getDescriptor(node.jjtGetVal());
                if(descriptors == null){
                    this.semanticError.printError(node, "Undefined variable " + node.jjtGetVal());
                    return null;
                }
                TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                
                Type type = descriptor.getType();
                if(type == Type.CLASS){
                    return descriptor.getClassName();
                }
                //If it is a variable
                if(descriptor.getClass() == VariableDescriptor.class){
                    VariableDescriptor variableDescriptor = (VariableDescriptor) descriptor;
                    if(!variableDescriptor.isInitialized() && !variableDescriptor.isInitializedInIf()){
                        if(this.initializedWarning){
                            System.err.println("Warning: Variable " + node.jjtGetVal() + " is not initialized\n");
                        }else{
                            this.semanticError.printError(node, "Variable " + node.jjtGetVal() + " is not initialized");
                        }
                    }
                    else if (!variableDescriptor.isInitializedInFunction()) {
                        System.err.println("Warning: Variable " + node.jjtGetVal() + " might not have been initialized\n");
                    }
                    this.llirPopulator.addExpression(new LLIRVariable(variableDescriptor));
                }
                //If it is a function parameter
                if(descriptor.getClass() == FunctionParameterDescriptor.class){
                    NamedTypeDescriptor variableDescriptor = (NamedTypeDescriptor) descriptor;
                    this.llirPopulator.addExpression(new LLIRVariable(variableDescriptor));
                }

                return (new StringType(type)).getString();
            }
            //If it is a Parenthesis expression
            case JavammTreeConstants.JJTPARENTHESESEXPRESSION: {
                return inspectExpression(node, symbolsTable);
            }
            case JavammTreeConstants.JJTTHIS: {
                this.llirPopulator.addExpression(new LLIRThis());
                return this.className;
            }
            default: {
                System.out.println("Warning: Unknown Argument Node ID: " + node.getId()); //Para detetar novos casos 
            }
        }

        this.semanticError.printError(node, "Invalid Argument: " + node.jjtGetVal());
        return null;
    }

    //Deals with complex expressions, like arithmetic operations, conditionals, function calls and arrays
    private String inspectExpressionComplex(SimpleNode argumentNode, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {

        String type = null;

        //Iterates through nodes of the statement
        for(int i = initialChild; i < argumentNode.jjtGetNumChildren(); i++){
            SimpleNode node = (SimpleNode) argumentNode.jjtGetChild(i);
            switch(node.getId()){
                case JavammTreeConstants.JJTINTEGERLITERAL: {
                    if(type == null){
                        type = "int";
                    }
                    else if(!type.equals("int")){
                        this.semanticError.printError(node, "Int is incompatible with " + type);
                        return null;
                    }
                    //ARITHMETIC
                    this.llirPopulator.addExpression(new llir.LLIRInteger(Integer.parseInt(node.jjtGetVal())));

                    break;
                }
                //Inspects if it is a boolean
                case JavammTreeConstants.JJTTRUE:
                case JavammTreeConstants.JJTFALSE: {
                    if(type == null){
                        type = "boolean";
                    } else if(!type.equals("boolean")){
                        this.semanticError.printError(node, "Boolean is imcompatible with " + type);
                        return null;
                    }
                    //CONDITIONAL Expression
                    this.llirPopulator.addExpression(new llir.LLIRBoolean(node.getId() == JavammTreeConstants.JJTTRUE ? true : false));

                    break;
                }
                //If it has a this then it has to be a function
                case JavammTreeConstants.JJTTHIS: {
                    if(this.insideStaticMethod){
                        this.semanticError.printError(node, "Non-static variable this cannot be referenced from a static context");
                        return null;
                    }
                    
                    LLIRMethodCall methodCall = new LLIRMethodCall();
                    methodCall.setClassType(this.className);
                    this.llirPopulator.addMethodCall(methodCall);                    
                    String functionType = inspectFunctionCall(argumentNode, symbolsTable, i+2);
                    if(type == null){
                        type = functionType;
                    }else if(!type.equals(functionType)){
                        this.semanticError.printError(node, functionType + " is incompatible with " + type);
                        return null;
                    }
                    i += 3;
                    break;
                } 
                case JavammTreeConstants.JJTIDENTIFIER: {
                    if(i+1 < argumentNode.jjtGetNumChildren()){ //CHECK IF THE IDENTIFIER BELONGS TO A FUNCTION OR ARRAY
                        SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(i+1);
                        //Class calls a function
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

                                        if (descriptor.getClass() == VariableDescriptor.class) {
                                            VariableDescriptor variableDescriptor = (VariableDescriptor)descriptor;
                                            if (!variableDescriptor.isInitializedInFunction()) {
                                                System.err.println("Warning: Variable " + node.jjtGetVal() + " might not have been initialized\n");
                                            }
                                        }
                                        this.llirPopulator.addExpression(new LLIRArrayLength(new LLIRVariable((NamedTypeDescriptor) descriptor)));
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
                            LLIRMethodCall methodCall = new LLIRMethodCall();
                            this.llirPopulator.addMethodCall(methodCall);
                            methodCall.setClassName(node.val);
                            String functionType = inspectFunctionCall(argumentNode, symbolsTable, i);
                    
                            TypeString typeString = new TypeString(functionType);

                            if(typeString.parseType() == Type.INT_ARRAY || typeString.parseType() == Type.STRING_ARRAY){
                                functionType = inspectArrayNodeAfterFunctionCall(argumentNode, symbolsTable, functionType, i+4);
                                if(functionType == null){
                                    this.semanticError.printError(nextNextNode, "Can't evaluate null expression");
                                    return null;
                                }
                                if(!functionType.equals("int[]") && !functionType.equals("String[]")){
                                    i++;
                                    if(((SimpleNode)argumentNode.jjtGetChild(initialChild+4)).getId() == JavammTreeConstants.JJTDOT)
                                        i++;
                                    
                                    this.llirPopulator.popArrayAfterFunction();  
                                }
                            }

                            if(type == null){
                                type = functionType;
                            }else if(!type.equals(functionType)){
                                this.semanticError.printError(nextNextNode, functionType + " is incompatible with " + type);
                                return null;
                            }
                            i += 3; // Jump function identifiers
                        
                            continue;
                        } else if(nextNode.getId() == JavammTreeConstants.JJTARRAY){


                            this.llirPopulator.addLLIR(new LLIRArrayAccess());
                            String arrayType = inspectArrayAccess(argumentNode, symbolsTable, i);
                            this.llirPopulator.popArrayAcessExpression();
                            if(type == null){
                                type = arrayType;
                            }else if(!type.equals(arrayType)){
                                this.semanticError.printError(nextNode, arrayType + " is incompatible with " + type);
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
                    }
                    TypeDescriptor descriptor = (TypeDescriptor) descriptors.get(0);
                    
                    Type descriptorType = descriptor.getType();
                    if(descriptorType == Type.CLASS){
                        this.semanticError.printError(node, "Operation can't involve a class type");
                        return null;
                    }

                    String descType = (new StringType(descriptorType)).getString();
                    if(type == null){
                        type = descType;
                    }else if(!type.equals(descType)){
                        this.semanticError.printError(node, descType + " is incompatible with " + type);
                        return null;
                    }

                    //Is Variable
                    if(descriptor.getClass() == VariableDescriptor.class){
                        VariableDescriptor variableDescriptor = (VariableDescriptor) descriptor;
                        if(!variableDescriptor.isInitialized() && !variableDescriptor.isInitializedInIf()){
                            if(this.initializedWarning && !variableDescriptor.isInitializedInIf()){
                                System.err.println("Warning: Variable " + node.jjtGetVal() + " is not initialized\n");
                            }else{
                                this.semanticError.printError(node, "Variable " + node.jjtGetVal() + " is not initialized");
                            }
                        }
                        else if (!variableDescriptor.isInitializedInFunction()) {
                            System.err.println("Warning: Variable " + node.jjtGetVal() + " might not have been initialized\n");
                        }
                        this.llirPopulator.addExpression(new llir.LLIRVariable(variableDescriptor));
                    }
                    //Is a function parameter
                    if(descriptor.getClass() == FunctionParameterDescriptor.class){
                        NamedTypeDescriptor variableDescriptor = (NamedTypeDescriptor) descriptor;
                        this.llirPopulator.addExpression(new LLIRVariable(variableDescriptor));
                    }

                    break;
                }
                case JavammTreeConstants.JJTAND:{
                    if(!type.equals("boolean")){
                        this.semanticError.printError(node, "Operation && is incompatible with " + type);
                        return null;
                    }
                    //CONDITIONAL &&
                    if(llirPopulator.lastIsLLIRExpression()) this.llirPopulator.addConditional(new LLIRConditional());
                    this.llirPopulator.addOperator(node.getId());

                    break;
                }
                case JavammTreeConstants.JJTNEW: {
                    if(i != initialChild){
                        this.semanticError.printError(node, "Can't instantiate class inside an expression");
                        return null;
                    }
                    SimpleNode nextNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);

                    if(nextNode.getId() == JavammTreeConstants.JJTARRAY){
                        SimpleNode idNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+1);
                        if(idNode.jjtGetVal() != "int"){
                            this.semanticError.printError(idNode, "Can only instantiate array of int");
                            return null;
                        }
                        
                        SimpleNode arrayNode = (SimpleNode) argumentNode.jjtGetChild(initialChild+2);

                        //Popular array instantiation
                        this.llirPopulator.addLLIR(new LLIRArrayInstantiation());
                        String indexType = inspectExpression(arrayNode, symbolsTable);

                        this.llirPopulator.popArrayInstantiation();

                        if(indexType == null){
                            this.semanticError.printError(arrayNode, "Array index must be an int");
                            return null;
                        } else if(!indexType.equals("int")){
                            this.semanticError.printError(arrayNode, "Array index must be an int");
                            return null;
                        }

                        return "int[]";
                    }
                    //Class
                    else if (initialChild+3 < argumentNode.jjtGetNumChildren()) {
                        if (argumentNode.jjtGetChild(initialChild+3).getId() == JavammTreeConstants.JJTDOT) {
                            String classType = inspectClassInstantiation(argumentNode, symbolsTable, initialChild);
                            if (classType == null) {
                                this.semanticError.printError(nextNode,"Undefined class type");
                                return null;
                            }
                            return inspectFunctionOnClass(argumentNode, classType, symbolsTable, initialChild);
                        }
                    }
                    String returnValue = inspectClassInstantiation(argumentNode, symbolsTable, initialChild);
                    return returnValue;
                }
                //Inspects expression if it is a negation "!"
                case JavammTreeConstants.JJTNEGATION: {
                    if(type == null){
                        type = "boolean";
                    }else if(!type.equals("boolean")){
                        this.semanticError.printError(node, "Operation ! is incompatible with " + type);
                        return null;
                    }

                    //NEGATION !
                    this.llirPopulator.addExpression(new LLIRNegation());
                    break;
                }
                //Inspects if it finds "<"
                case JavammTreeConstants.JJTLESS: {
                    if(!type.equals("int")){
                        this.semanticError.printError(node, "Can't compare " + type + " with operator <");
                        return null;
                    }
                    this.llirPopulator.fixConditional();

                    //CONDITIONAL <
                    if(llirPopulator.lastIsLLIRExpression()) 
                        this.llirPopulator.addConditional(new LLIRConditional());
                    this.llirPopulator.addOperator(node.getId());

                    String otherType = inspectExpression(argumentNode, symbolsTable, i+1);
                    if(!otherType.equals("int")){
                        this.semanticError.printError(node, "Can't compare " + otherType + " with operator <");
                        return null;
                    }

                    if(!(llirPopulator.peek() instanceof LLIRConditional))
                        this.llirPopulator.popExpression();


                    if(this.llirPopulator.peek() instanceof LLIRConditional){
                        LLIRConditional conditional = (LLIRConditional)this.llirPopulator.peek();
                                        
                        if(conditional.getLeftExpression() instanceof LLIRArithmetic){
                            ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) conditional.getLeftExpression());
                            conditional.setLeftExpression(transformer.transform());
                        }else if(conditional.getLeftExpression() instanceof LLIRParenthesis){
                            LLIRParenthesis llirParenthesis = (LLIRParenthesis)conditional.getLeftExpression();
                            if(llirParenthesis.getExpression() instanceof LLIRArithmetic){
                                ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) llirParenthesis.getExpression());
                                llirParenthesis.setExpression(transformer.transform());
                            }
                            conditional.setLeftExpression(llirParenthesis);
                        }

                        if(conditional.getRightExpression() instanceof LLIRArithmetic){
                            ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) conditional.getRightExpression());
                            conditional.setRightExpression(transformer.transform());
                        }else if(conditional.getRightExpression() instanceof LLIRParenthesis){
                            LLIRParenthesis llirParenthesis = (LLIRParenthesis)conditional.getRightExpression();
                            if(llirParenthesis.getExpression() instanceof LLIRArithmetic){
                                ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) llirParenthesis.getExpression());
                                llirParenthesis.setExpression(transformer.transform());
                            }
                            conditional.setRightExpression(llirParenthesis);
                        }
                    }
                    return "boolean";
                }
                case JavammTreeConstants.JJTDOT: {
                    this.semanticError.printError(node, "Can't access property/method of variable of type " + type);
                    return null;
                }
                case JavammTreeConstants.JJTPARENTHESESEXPRESSION: {
                    this.llirPopulator.addExpression(new LLIRParenthesis());
                    String expressionType = inspectExpression(node, symbolsTable);
                    if(type == null){
                        type = expressionType;
                    }else if(!type.equals(expressionType)){
                        this.semanticError.printError(node, "Can't evaluate expression inside parentheses");
                    }
                    this.llirPopulator.popParenthesis();

                    break;
                }


                default:{ //Plus, Minus, ...
                    if(!type.equals("int")){
                        this.semanticError.printError(node, "Operations are incompatible with " + type);
                        return null;
                    }
                    //ARITHMETIC
                    if(llirPopulator.lastIsLLIRExpression()) this.llirPopulator.addArithmetic(new LLIRArithmetic());
                    this.llirPopulator.addOperator(node.getId());
                    break;
                }
            }
            this.llirPopulator.popNegation();
        }
        this.llirPopulator.popExpression();


        return type;
    }

    //Inspects if it is a class instantiation
    private String inspectClassInstantiation(SimpleNode node, SymbolsTable symbolsTable, int newPosition) throws SemanticErrorException {
        SimpleNode classIdentifierNode = (SimpleNode) node.jjtGetChild(newPosition+1);
                      
        // Get all the Class constructors
        List<Descriptor> descriptorsList = symbolsTable.getDescriptor(classIdentifierNode.jjtGetVal());  
        SimpleNode argumentsNode = (SimpleNode) node.jjtGetChild(newPosition+2);

        List<String> parameters = inspectArguments(argumentsNode, symbolsTable);
        
        if(parameters.size() == 0 && descriptorsList.size() == 1){
            Descriptor descriptor = descriptorsList.get(0);
            //Example simple = new Simple
            if(descriptor.getClass() == ClassDescriptor.class){
                this.llirPopulator.addLLIR(new LLIRClassVariableInstantiation(((ClassDescriptor) descriptor).getName()));
                return classIdentifierNode.jjtGetVal();
            }
        }

        //Iterates through all the descriptors
        for(int i = 0; i < descriptorsList.size(); i++){
            Descriptor descriptor = descriptorsList.get(i);

            //If it is an Import
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
                        this.semanticError.printError(argumentsNode, "Incompatible type");
                        return null;
                    }
                } 

                LLIRClassVariableInstantiation llirClassVariableInstantiation = new LLIRClassVariableInstantiation(importDescriptor.getIdentifiers().get(0));
                llirClassVariableInstantiation.addParameterTypes(importParameters);
                this.llirPopulator.addLLIR(llirClassVariableInstantiation);
                this.llirPopulator.popArgumentsClassInstantiation(parameters.size());

                return classIdentifierNode.jjtGetVal();

                //If it is a simple function
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
                            this.semanticError.printError(argumentsNode, "Incompatible type");
                            return null;
                        }
                    }else{
                        StringType type = new StringType(parameterType);
                        if(!type.getString().equals(parameters.get(j))){
                            this.semanticError.printError(argumentsNode, "Incompatible type");
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


    private String inspectArrayAccessAfterFunctionCall(SimpleNode node, TypeString arrayType, int initialChild, SymbolsTable symbolsTable)  throws SemanticErrorException {
        SimpleNode arrayNode = (SimpleNode) node.jjtGetChild(initialChild);
        this.llirPopulator.addExpression(new LLIRArrayAccess());
        String indexType = inspectExpression(arrayNode, symbolsTable);

        if(indexType == null){
            this.semanticError.printError(arrayNode, "Array index must be an int");
            return null;
        } else if(!indexType.equals("int")){
            this.semanticError.printError(arrayNode, "Array index must be an int");
            return null;
        }

        this.llirPopulator.popArrayAcessExpression();

        if(arrayType.parseType() == Type.INT_ARRAY)
            return "int";
        if(arrayType.parseType() == Type.STRING_ARRAY)
            return "String";

        return null;
    }

    private String inspectArrayNodeAfterFunctionCall(SimpleNode node, SymbolsTable symbolsTable, String type, int initialChild) throws SemanticErrorException{
        TypeString typeString = new TypeString(type);

        if(initialChild < node.jjtGetNumChildren()){
            if(((SimpleNode)node.jjtGetChild(initialChild)).getId() == JavammTreeConstants.JJTARRAY){
                return inspectArrayAccessAfterFunctionCall(node, typeString, initialChild, symbolsTable);
            }else if(((SimpleNode)node.jjtGetChild(initialChild)).getId() == JavammTreeConstants.JJTDOT){
                if(initialChild+1 < node.jjtGetNumChildren()){
                    if(((SimpleNode)node.jjtGetChild(initialChild+1)).getId() == JavammTreeConstants.JJTLENGTH){
        
                        this.llirPopulator.addExpression(new LLIRArrayLength());
                        return "int"; // array.length always returns an int
                    }else{
                        this.semanticError.printError((SimpleNode)node.jjtGetChild(initialChild+1), "Operation not suported by arrays");
                        return null;
                    }
                }else{
                    this.semanticError.printError((SimpleNode)node.jjtGetChild(initialChild), "Parser error - dot must be followed by an identifier");
                    return null;
                }
            }
        }

        return type;
    }
    //Inspects function on class instantiation
    private String inspectFunctionOnClass(SimpleNode node, String classType, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        List<Descriptor> descriptors = symbolsTable.getDescriptor(classType);
        Descriptor descriptor = descriptors.get(0);
        
        if (descriptor.getClass() == ClassDescriptor.class) {
            if (isInClass(node, (ClassDescriptor)descriptor, symbolsTable, initialChild+4)){
                this.llirPopulator.addMethodCall(new LLIRMethodCall());

                String type = inspectFunctionCall(node, symbolsTable, initialChild+4);
                this.llirPopulator.popArgumentsClassInstantiation();
                TypeString typeString = new TypeString(type);

                if(typeString.parseType() == Type.INT_ARRAY || typeString.parseType() == Type.STRING_ARRAY){
                    type = inspectArrayNodeAfterFunctionCall(node, symbolsTable, type, initialChild+6);
                    this.llirPopulator.popArrayAfterFunction(); 
                }

                return type;
            }
        }
        if (descriptor.getClass() == ImportDescriptor.class) {
            if (isInImport(node, classType, symbolsTable, initialChild)){
                String type = inspectFunctionCall(node, symbolsTable, initialChild+1, true);
                TypeString typeString = new TypeString(type);
                if(typeString.parseType() == Type.INT_ARRAY || typeString.parseType() == Type.STRING_ARRAY){
                    type = inspectArrayNodeAfterFunctionCall(node, symbolsTable, type, initialChild+3);
                    this.llirPopulator.popArrayAfterFunction();
                }
                return type;
            }
        }

        SimpleNode errorNode = (SimpleNode)node.jjtGetChild(initialChild);          
        this.semanticError.printError(errorNode,"Function not declared on class.");
        return null;
    }

    private boolean isInClass(SimpleNode node, ClassDescriptor classDescriptor, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SymbolsTable functionsTable = classDescriptor.getFunctionsTable();

        SimpleNode functionNode = (SimpleNode)node.jjtGetChild(initialChild);
        List<Descriptor> functionDescriptors = functionsTable.getDescriptor(functionNode.jjtGetVal());
        if (functionDescriptors == null)
            return false;

        List<String> parametersTypes = new ArrayList<>();
        for (int i = initialChild; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode)node.jjtGetChild(i);
            if (child.getId() == JavammTreeConstants.JJTARGUMENTS) {
                parametersTypes = inspectArguments(child, symbolsTable);
                break;
            }
        }
        for (int i = 0; i < functionDescriptors.size(); i++) {
            FunctionDescriptor functionDescriptor = (FunctionDescriptor) functionDescriptors.get(i);
            HashMap<String, List<Descriptor>> parametersTable = functionDescriptor.getParametersTable().getTable();
            
            if(parametersTable.size() == 0 && parametersTable.size() == parametersTypes.size()){
                return true;
            }
            int j = 0;
            for(HashMap.Entry<String, List<Descriptor>> parameters : parametersTable.entrySet()){
                Descriptor desc = parameters.getValue().get(0);
                StringType strType = new StringType(((FunctionParameterDescriptor)desc).getType());
                if (strType.getString() == parametersTypes.get(j)) {
                    j++;
                    if (j == parametersTypes.size())
                        return true;
                }
            }
        }
        return false;
    }

    private boolean isInImport(SimpleNode node, String classType, SymbolsTable symbolsTable, int initialChild) throws SemanticErrorException {
        SimpleNode functionNode = (SimpleNode)node.jjtGetChild(initialChild+4);
        List<Descriptor> importDescriptors = symbolsTable.getDescriptor(functionNode.jjtGetVal());

        if (importDescriptors == null)
            return false;

        for (int i = 0; i < importDescriptors.size(); i++) {
            ImportDescriptor importD = (ImportDescriptor)importDescriptors.get(i);
            ArrayList<String> identifiers = importD.getIdentifiers();

            if (identifiers.get(0).equals(classType)) 
                return true;
        }

        return false;
    }

    private void setVarsInitialized(SymbolsTable symbolsTable) throws SemanticErrorException {
        Iterator<VariableDescriptor> it = this.initializedIfVars.iterator();
        while(it.hasNext()) {
            VariableDescriptor var = it.next();
            List<Descriptor> descriptors = symbolsTable.getDescriptor(var.getName());
            if (descriptors == null)
                return;
            for (int i = 0; i < descriptors.size(); i++) {
                Descriptor desc = descriptors.get(i);
                if (desc.getClass() == VariableDescriptor.class) {
                    VariableDescriptor variable = (VariableDescriptor)desc;
                    variable.setInitialized();
                }
            }
        } 
    }

    private void setVarsNonInitialized(SymbolsTable symbolsTable) throws SemanticErrorException {
        Iterator<VariableDescriptor> it = this.possibleWarningVars.iterator();
        while(it.hasNext()) {
            VariableDescriptor var = it.next();
            List<Descriptor> descriptors = symbolsTable.getDescriptor(var.getName());
            if (descriptors == null)
                return;
            for (int i = 0; i < descriptors.size(); i++) {
                Descriptor desc = descriptors.get(i);
                if (desc.getClass() == VariableDescriptor.class) {
                    VariableDescriptor variable = (VariableDescriptor)desc;
                    variable.setNonInitializedInFunction();
                }
            }
        } 
    }

}