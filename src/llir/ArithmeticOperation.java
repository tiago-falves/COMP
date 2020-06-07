package llir;


public enum ArithmeticOperation {
    SUM("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    private final String operation;

    ArithmeticOperation(String operation) {
        this.operation = operation;
    }
};
