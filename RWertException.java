public class RWertException extends Exception {
    public RWertException(String symbol) {
        super("Right side is no variable, constant or function: " + symbol);
    }
}