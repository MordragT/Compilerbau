public class WrongParametersException extends Exception {
    public WrongParametersException(String name, int parameterCount) {
        super("Tried to call method(" + name + ") with wrong amount of parameters: " + parameterCount);
    }
}