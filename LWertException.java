public class LWertException extends Exception {
    public LWertException(String symbol) {
        super("Left side is no variable: " + symbol);
    }
}