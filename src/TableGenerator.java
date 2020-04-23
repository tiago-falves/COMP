import javax.management.remote.JMXConnectorServerFactory;

import symbols.*;

public class TableGenerator {
    SimpleNode rootNode;
    SymbolsTable symbolsTable;

    public TableGenerator(SimpleNode rootNode) {
        this.rootNode = rootNode;
        this.symbolsTable = new SymbolsTable();
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
                    ClassDescriptor classDescriptor = inspectClass(currentNode);
                    symbolsTable.addSymbol(currentNode.jjtGetVal(), classDescriptor);
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
            } 
            else if (child.getId() == JavammTreeConstants.JJTVARIABLENAME) {
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
            parametersTable.addSymbol(name,parameter);

        }


    }

    public void inspectVariableAndStatement(SimpleNode variableAndStatementNode, FunctionDescriptor functionDescriptor) {
        if (variableAndStatementNode.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
            VariableDescriptor variableDescriptor = inspectVariable(variableAndStatementNode);
            functionDescriptor.addBodyVariable(variableDescriptor.getName(),variableDescriptor);
        }
        else if(variableAndStatementNode.getId() == JavammTreeConstants.JJTWHILESTATEMENT || variableAndStatementNode.getId() == JavammTreeConstants.JJTIFSTATEMENT){
            inspectBlockStatement(variableAndStatementNode, functionDescriptor.getBodyTable());
        }else if (variableAndStatementNode.getId() == JavammTreeConstants.JJTLINESTATEMENT ){
            //TODO: Analizar statement
            System.out.println("OLA");
            //analyseOperation(statementNode, blockDescriptor.getLocalTable());
        }else{
            System.err.println("Error: Unknown symbol");
        }
    }

    public void inspectBlockStatement(SimpleNode statementNode, SymbolsTable statementParent){

        BlockDescriptor blockDescriptor = new BlockDescriptor(statementParent);
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
                analyseOperation(statementNode,blockDescriptor.getLocalTable());
            }else{
                System.err.println("Error: Unknown symbol");
            }
        }

    }

    public void analyseOperation(SimpleNode statementNode, SymbolsTable symbolTable ){

        if(statementNode.jjtGetNumChildren() < 3){
            System.err.println("Error: Invalid Line Statement");
            return;
        }

        SimpleNode firstChild = (SimpleNode) statementNode.jjtGetChild(0);
        
        if(firstChild.getId() != JavammTreeConstants.JJTIDENTIFIER){
            System.err.println("Error: First Node of Line Statement must be an Identifier");
            return;
        }

        TypeDescriptor typeDescriptor = (TypeDescriptor) symbolTable.getDescriptor(firstChild.jjtGetVal());
        if(typeDescriptor == null){
            System.out.println("Error: Variable "+firstChild.jjtGetVal()+" not declared");
            return;
        }

        SimpleNode secondChild = (SimpleNode) statementNode.jjtGetChild(1);

        if(secondChild.getId() == JavammTreeConstants.JJTEQUAL){
            //Assignement
            Type type = typeDescriptor.getType();
            analyzeAssignement(statementNode, symbolTable, type);
        }
        else{
            //Function call
            analyzeFunctionCall(statementNode, symbolTable);
        }

    }

    public void analyzeAssignement(SimpleNode statementNode, SymbolsTable symbolsTable, Type type){

    }

    public void analyzeFunctionCall(statementNode, symbolTable){

    }
}