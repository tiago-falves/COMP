package llir;

public enum ArithmeticOperation {
    SUM("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    LESS_THAN("<"),
    AND("&&");

    private final String operation;

    ArithmeticOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return  this.operation;
    }


};
