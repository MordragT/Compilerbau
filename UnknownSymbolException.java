public class UnknownSymbolException extends Exception {
    public UnknownSymbolException(String symbol) {
        super("Tried to access unknown symbol: " + symbol);
    }
}