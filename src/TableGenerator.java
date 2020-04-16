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
                    symbolsTable.addSymbol(importDescriptor.getLastIdentifier(), importDescriptor );
                    break;

                case JavammTreeConstants.JJTCLASSDECLARATION:
                    //ClassDescriptor classDescriptor = inspectClass(currentNode);
                    //symbolsTable.addSymbol(currentNode.jjtGetVal(), classDescriptor);
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

    public void inspectClass(SimpleNode classNode) {
        
    }

}