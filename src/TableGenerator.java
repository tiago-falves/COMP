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
        while(i < rootNode.jjtGetNumChildren()) {
            SimpleNode currentNode = (SimpleNode) rootNode.jjtGetChild(i);

            switch(currentNode.getId()) {
                case JavammTreeConstants.JJTIMPORTDECLARATION:
                    ImportDescriptor importDescriptor = inspectImport(currentNode);
                    symbolsTable.addSymbol(importDescriptor.getLastIdentifier(), importDescriptor);
                    break;

                case JavammTreeConstants.JJTCLASSDECLARATION:
                    ClassDescriptor classDescriptor = inspectClass(currentNode);
                    //TODO
                    //symbolsTable.addSymbol(classDescriptor.jjtGetVal(), classDescriptor);
                    break;
            }

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
                importDescriptor.setReturn(typeString.parseType());
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
                FunctionDescriptor functionDescriptor = inspectMethod(child);
                classDescriptor.addMethod(functionDescriptor);
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

    public FunctionDescriptor inspectMethod(SimpleNode methodNode) {
        SimpleNode child = (SimpleNode) methodNode.jjtGetChild(0);

        //check if is main or usual method
        if (child.getId() == JavammTreeConstants.JJTMAINDECLARATION) {
            return inspectMainDeclaration(child);
        }
        else if (child.getId() == JavammTreeConstants.JJTMETHODHEADER) {
            return inspectMethod(methodNode);
        }
        else return null;
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
            else if (child.getId() == JavammTreeConstants.JJTVARIABLEDECLARATION) {
                VariableDescriptor variableDescriptor = inspectVariable(child);
                functionDescriptor.addBodyVariable(variableDescriptor);
            }
            //TODO statements inside main
        }

        return functionDescriptor;
    }

    /*public FunctionParametersDescriptor inspectMethodHeader(SimpleNode headerNode) {

    }

    public FunctionBodyDescriptor inspectMethodBody(SimpleNode bodyNode) {

    }*/
}