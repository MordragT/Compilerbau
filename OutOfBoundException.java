public class OutOfBoundException extends Exception {
    public OutOfBoundException(String message) {
        super("Out of bound: Value was not in range: 0x" + message);
    }
}
