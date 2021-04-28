public class Data {
    private int value;
    private DataKind kind;

    public Data(String value, DataKind kind) {
        this.value = Integer.parseInt(value);
        this.kind = kind;
    }

    public Data(int value, DataKind kind) {
        this.value = value;
        this.kind = kind;
    }

    public DataKind kind() {
        return this.kind;
    }

    public int inner() {
        return this.value;
    }
}
