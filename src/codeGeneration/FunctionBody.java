package codeGeneration;

import java.util.*;

import codeGeneration.CodeWriter.*;
import llir.*;
import optimizations.OptimizationManager;
import optimizations.RegisterReducer;
import symbols.*;


public class FunctionBody {
    
    // Static variables
    public static LinkedHashMap<String, Integer> variableToIndex;
    public static LinkedHashMap<String, Integer> fieldsToIndex;
    public static int currentVariableIndex;
    public static int maxStack = 0;
    public static int totalStack = 0;
    private static SymbolsTable fieldsTable;
    private static ClassDescriptor classDescriptor;

    // Non-static variables
    private final String LOCALS_LIMIT;
    private FunctionDescriptor functionDescriptor;
    private String STACK_LIMIT = "\t.limit stack ";

    public FunctionBody(FunctionDescriptor functionDescriptor, LinkedHashMap<String, Integer> variableToIndex, ClassDescriptor classDescriptor) {
        this.functionDescriptor = functionDescriptor;
        FunctionBody.variableToIndex = variableToIndex;
        FunctionBody.currentVariableIndex = variableToIndex.size();

        if(functionDescriptor.getName().equals("main")){
            FunctionBody.currentVariableIndex--;
        }

        this.LOCALS_LIMIT = "\t.limit locals " + ((functionDescriptor.isStatic()?0:1) + functionDescriptor.getParametersTable().getSize() + functionDescriptor.getBodyTable().getSize());
        FunctionBody.totalStack = 0;
        FunctionBody.maxStack = 0;
        FunctionBody.fieldsTable = classDescriptor.getVariablesTable();
        FunctionBody.classDescriptor = classDescriptor;
    }

    public static void incStack(){
        totalStack++;
        if(totalStack > maxStack){
            maxStack = totalStack;
        }
    }
    
    public static void decStack(int value){
        totalStack -= value;
        if (totalStack < 0)
            totalStack = 0;
    }

    public static void resetStack(){
        totalStack = 0;
    }

    private void pushVariables(){

        //Push function parameters
        LinkedHashMap<String, List<Descriptor>> parametersTable = functionDescriptor.getParametersTable().getTable();
        for (Map.Entry<String, List<Descriptor>> entry : parametersTable.entrySet()) {
            String variableName = entry.getKey();
            int variableIndex = getVariableIndex(variableName);
            variableToIndex.put(variableName,variableIndex);
        }

        //Push body variables
        LinkedHashMap<String, List<Descriptor>> bodyTable = functionDescriptor.getBodyTable().getTable();

        for (Map.Entry<String, List<Descriptor>> entry : bodyTable.entrySet()) {
            String variableName = entry.getKey();
            int variableIndex = getVariableIndex(variableName);
            variableToIndex.put(variableName,variableIndex);

            //Determine variables that are only assigned once (constant propagation)
            if(OptimizationManager.constantPropagation && (entry.getValue().get(0) instanceof VariableDescriptor)) {

                VariableDescriptor varDes = (VariableDescriptor) entry.getValue().get(0);
                if(varDes.getType() != Type.INT && varDes.getType() != Type.BOOLEAN) continue;

                if(searchForAssignments(varDes, functionDescriptor.getFunctionBody()) == 1) {
                    if(varDes.getConstantDescriptor().isSimple()) {
                        varDes.getConstantDescriptor().setConstant(true);
                        System.out.println("\nConstant variable: " + varDes.getName());
                    }
                }
            }

        }

    }

    // Optimization -o
    public int searchForAssignments(VariableDescriptor varDes, List<LLIRNode> body) {
        int assignments = 0;

        for(LLIRNode node : body) {
            if(assignments > 1) break;

            if (node instanceof LLIRAssignment) {
                LLIRAssignment assignmentNode = (LLIRAssignment) node;
                LLIRVariableAndArray variableAndArray = (LLIRVariableAndArray) assignmentNode.getVariable();
                NamedTypeDescriptor namedTypeDescriptor = (NamedTypeDescriptor) variableAndArray.getVariable();
            
                if(namedTypeDescriptor.getName() == varDes.getName()) {
                    assignments++;

                    // Simple integer
                    if((assignmentNode.getExpression() instanceof LLIRInteger) && !varDes.getConstantDescriptor().isSimple()) {
                        LLIRInteger assignementInteger = (LLIRInteger) assignmentNode.getExpression();
                        varDes.setConstantDescriptor(new ConstantInt(assignementInteger.getValue()));
                    }
                    // Simple boolean
                    else if((assignmentNode.getExpression() instanceof LLIRBoolean) && !varDes.getConstantDescriptor().isSimple()) {
                        LLIRBoolean assignementBoolean = (LLIRBoolean) assignmentNode.getExpression();
                        varDes.setConstantDescriptor(new ConstantBoolean(assignementBoolean.getValue()));
                    }
                    // Another constant variable
                    else if((assignmentNode.getExpression() instanceof LLIRVariable) && !varDes.getConstantDescriptor().isSimple()) {
                        LLIRVariable constantVariable = (LLIRVariable) assignmentNode.getExpression();

                        if(constantVariable.getVariable().getName() != varDes.getName()) {

                            LinkedHashMap<String, List<Descriptor>> bodyTable = functionDescriptor.getBodyTable().getTable();
                            if(bodyTable.containsKey(constantVariable.getVariable().getName())) {

                                if(bodyTable.get(constantVariable.getVariable().getName()).get(0) instanceof VariableDescriptor) {

                                    VariableDescriptor constVar = (VariableDescriptor) bodyTable.get(constantVariable.getVariable().getName()).get(0);

                                    if(constVar.getConstantDescriptor() instanceof ConstantBoolean) {

                                        ConstantBoolean b = (ConstantBoolean) constVar.getConstantDescriptor();
                                        if(b.isConstant()) {
                                            varDes.setConstantDescriptor(new ConstantBoolean(b.getValue()));
                                        }
                                    }
                                    else if (constVar.getConstantDescriptor() instanceof ConstantInt) {

                                        ConstantInt i = (ConstantInt) constVar.getConstantDescriptor();
                                        if(i.isConstant()) {
                                            varDes.setConstantDescriptor(new ConstantInt(i.getValue()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (node instanceof LLIRIfElseBlock) {
                LLIRIfElseBlock block = (LLIRIfElseBlock) node;

                assignments += searchForAssignments(varDes, block.getIfNodes());
                assignments += searchForAssignments(varDes, block.getElseNodes());
            }
            else if(node instanceof LLIRWhileBlock) {
                LLIRWhileBlock block = (LLIRWhileBlock) node;

                int found = searchForAssignments(varDes, block.getNodes());
                if(found > 0) assignments += 2;
            }
        }

        return assignments;
    }


    public String generate(){

        // Add variables to hash map
        pushVariables();

        if(OptimizationManager.reducedLocals){

            RegisterReducer.firstPass = true;

            for(LLIRNode node : this.functionDescriptor.getFunctionBody()) {
                RegisterReducer.incrementLine();
                RegisterReducer.addPredSucc();

                FunctionBody.resetStack();
                
                if (node instanceof LLIRAssignment) new AssignmentWriter((LLIRAssignment) node);
                else if (node instanceof LLIRMethodCall) new MethodCallWriter((LLIRMethodCall) node);
                else if (node instanceof LLIRImport) new ImportWriter((LLIRImport) node);
                else if (node instanceof LLIRIfElseBlock) new IfElseWriter((LLIRIfElseBlock) node, "");
                else if (node instanceof LLIRWhileBlock) new WhileWriter((LLIRWhileBlock) node, "");            
                else if (node instanceof LLIRReturn) new ReturnWriter((LLIRReturn) node);
            }
        }

        boolean foundReturn = false;
        String generatedCode = new String();


        if(OptimizationManager.reducedLocals) {
            String functionName = functionDescriptor.getName();

            Set<String> parameters = functionDescriptor.getParametersTable().getTable().keySet();
            boolean isMain = (functionDescriptor.getName() == "main");

            RegisterReducer.firstPass = false;
            RegisterReducer.calculateInOut();
            if(RegisterReducer.allocateRegisters(parameters, isMain)) {
                System.out.println("Register allocation for " + functionName + " was successful");
                System.out.println("\tUsed " + RegisterReducer.usedRegisters + " registers:");
                RegisterReducer.printAllocation("\t  ");
            } else {
                System.out.println("Impossible to allocate registers for " + functionName);
                System.out.println("\tNeeded " + RegisterReducer.usedRegisters + " registers");
            }

        }

        for(LLIRNode node : this.functionDescriptor.getFunctionBody()) {

            FunctionBody.resetStack();
            if (node instanceof LLIRAssignment) {
                AssignmentWriter assignmentWriter = new AssignmentWriter((LLIRAssignment) node);
                generatedCode += assignmentWriter.getCode();
            }
            else if (node instanceof LLIRMethodCall) {
                MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) node);
                generatedCode += methodCallWriter.getCode();
            }else if (node instanceof LLIRImport) {
                ImportWriter importWriter = new ImportWriter((LLIRImport) node);
                generatedCode += importWriter.getCode();
            }
            else if (node instanceof LLIRIfElseBlock){
                IfElseWriter ifElseWriter = new IfElseWriter((LLIRIfElseBlock) node, "");
                generatedCode += ifElseWriter.getCode();
            }
            else if (node instanceof LLIRWhileBlock){
                WhileWriter whileWriter = new WhileWriter((LLIRWhileBlock) node, "");
                generatedCode += whileWriter.getCode();
            }
            else if (node instanceof LLIRReturn) {
                ReturnWriter returnWriter = new ReturnWriter((LLIRReturn) node);
                generatedCode += returnWriter.getCode();
                foundReturn = true;
            }
        }
        if(!foundReturn) generatedCode += "\treturn\n";

        if(OptimizationManager.reducedLocals) {
            //RegisterReducer.print();
            //OptimizationsR.printInOut();
            RegisterReducer.reset();
        }
        
        return STACK_LIMIT + maxStack + "\n" + LOCALS_LIMIT + "\n" + generatedCode;
    }

    public static int getVariableIndex(String name){
        int variableIndex = variableToIndex.computeIfAbsent(
                name,
                val -> {
                    currentVariableIndex++;
                    return currentVariableIndex;
                }
        );
        return variableIndex;
    }

    public static String getVariableIndexOptimized(int index) {
        if(index <= 3) return "_" + index;
        else return "\t" + index;
    }

    public static String getVariableIndexExists(String name){
        Integer variableIndex = variableToIndex.get(name);
        if(variableIndex == null) {
            return "";
        }else if(variableIndex <= 3) return "_" + variableIndex;
        else return "\t" + variableIndex;

    }

    public static String getField(String name,Type type){
        return FunctionBody.classDescriptor.getName() + "/" + name + " " + CGConst.types.get(type) + "\n";
    }

    public static String getVariableIndexString(String name){
        int number = getVariableIndex(name);
        if(number <= 3) return "_" + number;
        else return "\t" + number;
    }

    public LinkedHashMap<String, Integer> getTable() {
        int currentIndex = 1;

        for (HashMap.Entry<String, List<Descriptor>> tableEntry : fieldsTable.getTable().entrySet()) {
            if(tableEntry.getValue().size() > 0) {
                VariableDescriptor field = (VariableDescriptor) tableEntry.getValue().get(0);
                fieldsToIndex.put(field.getName(),currentIndex);
                currentIndex++;
            }
        }

        return fieldsToIndex;
    }

}
