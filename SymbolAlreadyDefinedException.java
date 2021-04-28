public class SymbolAlreadyDefinedException extends Exception {
    public SymbolAlreadyDefinedException(String symbol) {
        super("This symbol was already defined: " + symbol);
    }
}
