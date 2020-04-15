import symbols.*;

public class TableGenerator {
    SimpleNode rootNode;
    SymbolsTable symbolsTable;

    public TableGenerator(SimpleNode rootNode) {
        this.rootNode = rootNode;
        this.symbolsTable = new SymbolsTable();
    }

    /*
    public void build() {
        for(int i = 0; i < rootNode.children.length; i++) {
            SimpleNode currentNode = (SimpleNode) rootNode.children[i];

            switch(currentNode.id) {
                case JavammTreeConstants.JJTIMPORTDECLARATION:
                    symbolsTable.addSymbol(currentNode.val, new );
                    break;

                case JavammTreeConstants.JJTCLASSDECLARATION:
                    symbolsTable.addSymbol(currentNode.val, new ClassDescriptor());
                    recursiveBuild(currentNode);
                    break;
            }

        }
    }

    public void recursiveBuild(SimpleNode node) {
        
    }
 
     */

}