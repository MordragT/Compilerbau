import java.util.HashMap;
import java.lang.Integer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CodeGenerator {
    private final String BIPUSH = "10";
    private final String SIPUSH = "11";
    private final String ILOAD = "15";
    private final String ISTORE = "36";
    private final String IADD = "60";
    private final String ISUB = "64";
    private final String IMUL = "68";
    private final String IDIV = "6c";
    private final String IF_ICMPEQ = "9f";
    private final String IF_ICMPNE = "a0";
    private final String IF_ICMPLT = "a1";
    private final String IF_ICMPGE = "a2";
    private final String IF_ICMPGT = "a3";
    private final String IF_ICMPLE = "a4";
    private final String GOTO = "a7";
    private final String IRETURN = "ac";
    private final String RETURN = "b1";
    private final String GETSTATIC = "b2";
    private final String PUTSTATIC = "b3";
    private final String INVOKEVIRTUAL = "b6";
    private final String INVOKESTATIC = "b8";

    private String mnenomicCode = "";
    private String byteCode = "";
    private int offset = 0;
    // private HashMap<Integer, Integer> jumpAddressTable = new HashMap<>();

    public void printCodes() {
        System.out.println("Byte-Code: ");
        System.out.println(byteCode);
        System.out.println("\nMnenomics: ");
        System.out.println(mnenomicCode);
    }

    public String getByteCode() {
        return byteCode;
    }

    public int offset() {
        return offset;
    }

    // TODO: reicht es über die länge zu schauen ?
    public void pushNum(int val) throws OutOfBoundException {
        if (val < -32768 || val > 32768) {
            throw new OutOfBoundException("Value is to big for SIPUSH");
        }
        String value = Integer.toHexString(val);
        if (val < 0) {
            value = value.substring(value.length() - 4, value.length());
        }
        switch (value.length()) {
            case 1:
                byteCode += BIPUSH + "0" + value;
                mnenomicCode += offset + ":\tBIPUSH 0x0" + value + '\n';
                offset += 2;
                break;
            case 2:
                byteCode += BIPUSH + value;
                mnenomicCode += offset + ":\tBIPUSH 0x" + value + '\n';
                offset += 2;
                break;
            case 3:
                byteCode += SIPUSH + "0" + value;
                mnenomicCode += offset + ":\tSIPUSH 0x0" + value + '\n';
                offset += 3;
                break;
            case 4:
                byteCode += SIPUSH + value;
                mnenomicCode += offset + ":\tSIPUSH 0x" + value + '\n';
                offset += 3;
                break;
            default:
                throw new OutOfBoundException(value);
        }
    }

    public void return_() {
        byteCode += RETURN;
        mnenomicCode += offset + ":\tRETURN\n";
        offset += 1;
    }

    public void ireturn() {
        byteCode += IRETURN;
        mnenomicCode += offset + ":\tIRETURN\n";
        offset += 1;
    }

    public void add() {
        byteCode += IADD;
        mnenomicCode += offset + ":\tIADD\n";
        offset += 1;
    }

    public void sub() {
        byteCode += ISUB;
        mnenomicCode += offset + ":\tISUB\n";
        offset += 1;
    }

    public void mul() {
        byteCode += IMUL;
        mnenomicCode += offset + ":\tIMUL\n";
        offset += 1;
    }

    public void div() {
        byteCode += IDIV;
        mnenomicCode += offset + ":\tIDIV\n";
        offset += 1;
    }

    public void print() {
        byteCode += INVOKESTATIC + "(print)";
        mnenomicCode += offset + ":\tINVOKESTATIC (print)\n";
        offset += 3;
    }

    public void call(String name) {
        byteCode += INVOKESTATIC + "(" + name + ")";
        mnenomicCode += offset + ":\tINVOKESTATIC (" + name + ")\n";
        offset += 3;
    }

    public int if_() {
        int strIndex = byteCode.length();
        byteCode += "<__>";
        mnenomicCode += " <jmp-addr>\n";
        offset += 2;
        return strIndex;
    }

    public void noElse(int ifStrIndex, int ifOffset) throws OutOfBoundException {
        String val = twoByteStr(offset - ifOffset + 3);
        for (char c : val.toCharArray()) {
            byteCode = replaceChar(byteCode, c, ifStrIndex);
            ifStrIndex += 1;
        }
    }

    public int else_(int ifStrIndex, int ifOffset) throws OutOfBoundException {
        byteCode += GOTO;
        int strIndex = byteCode.length();
        byteCode += "<__>";
        mnenomicCode += offset + ":\tGOTO <jmp-addr>\n";
        offset += 3;
        String val = twoByteStr(offset - ifOffset + 3);
        for (char c : val.toCharArray()) {
            byteCode = replaceChar(byteCode, c, ifStrIndex);
            ifStrIndex += 1;
        }
        return strIndex;
    }

    public void afterElse(int elseStrIndex, int elseOffset) throws OutOfBoundException {
        String val = twoByteStr(offset - elseOffset + 3);
        for (char c : val.toCharArray()) {
            byteCode = replaceChar(byteCode, c, elseStrIndex);
            elseStrIndex += 1;
        }
    }

    public int while_() {
        int strIndex = byteCode.length();
        byteCode += "<__>";
        mnenomicCode += " <jmp-addr>\n";
        offset += 2;
        return strIndex;
    }

    public void afterWhile(int whileStrIndex, int whileOffset) throws OutOfBoundException {
        byteCode += GOTO + twoByteStr(whileOffset - offset);
        mnenomicCode += offset + ":\tGOTO 0x" + twoByteStr(whileOffset - offset) + "\n";
        offset += 3;
        // System.out.println("Whileoffset" + whileOffset);
        String val = twoByteStr(offset - (whileOffset + 4));
        for (char c : val.toCharArray()) {
            byteCode = replaceChar(byteCode, c, whileStrIndex);
            whileStrIndex += 1;
        }
    }

    public void lessThan() throws OutOfBoundException {
        byteCode += IF_ICMPGE;
        mnenomicCode += offset + ":\tIF_ICMPGE";
        offset += 1;
    }

    public void greaterThan() throws OutOfBoundException {
        byteCode += IF_ICMPLE;
        mnenomicCode += offset + ":\tIF_ICMPLE";
        offset += 1;
    }

    public void lessEqual() throws OutOfBoundException {
        byteCode += IF_ICMPGT;
        mnenomicCode += offset + ":\tIF_ICMPGT";
        offset += 1;
    }

    public void greaterEqual() throws OutOfBoundException {
        byteCode += IF_ICMPLT;
        mnenomicCode += offset + ":\tIF_ICMPLT";
        offset += 1;
    }

    public void equal() throws OutOfBoundException {
        byteCode += IF_ICMPNE;
        mnenomicCode += offset + ":\tIF_ICMPNE";
        offset += 1;
    }

    public void unequal() throws OutOfBoundException {
        byteCode += IF_ICMPEQ;
        mnenomicCode += offset + ":\tIF_ICMPEQ";
        offset += 1;
    }

    public void dereference(Data val) throws OutOfBoundException {
        switch (val.kind()) {
            case Parameter:
            case Variable:
                String valueStr = byteStr(val.inner());
                byteCode += ILOAD + valueStr;
                mnenomicCode += offset + ":\tILOAD 0x" + valueStr + '\n';
                offset += 2;
                break;
            case Constant:
                pushNum(val.inner());
                break;
        }
    }

    public void dereferenceStatic(Data val, String id) throws OutOfBoundException, UnknownSymbolException {
        switch (val.kind()) {
            case Parameter:
                throw new UnknownSymbolException("Expected no parameter.");
            case Variable:
                byteCode += GETSTATIC + "[" + id + "]";
                mnenomicCode += offset + ":\tGETSTATIC [" + id + "]\n";
                offset += 3;
                break;
            case Constant:
                pushNum(val.inner());
                break;
        }
    }

    public void assignment(int addr) throws OutOfBoundException {
        String index = byteStr(addr);
        byteCode += ISTORE + index;
        mnenomicCode += offset + ":\tISTORE 0x" + index + "\n";
        offset += 2;
    }

    public void assignmentStatic(String id) {
        byteCode += PUTSTATIC + "[" + id + "]";
        mnenomicCode += offset + ":\tPUTSTATIC [" + id + "]\n";
        offset += 3;
    }

    public void update(SymbolTable st) throws OutOfBoundException {
        for (Data value : st.dataTable().values()) {
            if (value.kind() == DataKind.Variable) {
                int addr = value.inner();
                HashMap<Integer, Integer> addressTable = st.addrTable();
                if (addressTable.containsKey(addr)) {
                    int val = addressTable.get(addr);
                    pushNum(val);
                } else {
                    pushNum(0);
                }
                String addrStr = byteStr(addr);
                byteCode += ISTORE + addrStr;
                mnenomicCode += offset + ":\tISTORE 0x" + addrStr + '\n';
                offset += 2;
            }
        }
    }

    public void updateStatic(SymbolTable st) throws OutOfBoundException {
        for (Map.Entry<String, Data> entry : st.dataTable().entrySet()) {
            Data value = entry.getValue();
            if (value.kind() == DataKind.Variable) {
                int addr = value.inner();
                HashMap<Integer, Integer> addressTable = st.addrTable();
                if (addressTable.containsKey(addr)) {
                    int val = addressTable.get(addr);
                    pushNum(val);
                } else {
                    pushNum(0);
                }
                String key = entry.getKey();
                byteCode += PUTSTATIC + "[" + key + "]";
                mnenomicCode += offset + ":\tPUTSTATIC [" + key + "]\n";
                offset += 3;
            }
        }
    }

    private String hexStr(int val) {
        String value = Integer.toHexString(val);
        if (value.length() % 2 != 0) {
            return "0" + value;
        } else {
            return value;
        }
    }

    private String byteStr(int val) throws OutOfBoundException {
        String value = Integer.toHexString(val);
        if (value.length() == 1) {
            return "0" + value;
        } else if (value.length() == 2) {
            return value;
        } else {
            throw new OutOfBoundException("Value has to long hex value");
        }
    }

    // TODO: negative values ?
    private String twoByteStr(int val) throws OutOfBoundException {
        if (val < -32768 || val > 32768) {
            throw new OutOfBoundException("Value is to big for SIPUSH");
        }
        String value = Integer.toHexString(val);
        if (val < 0) {
            value = value.substring(value.length() - 4, value.length());
        }
        switch (value.length()) {
            case 1:
                return "000" + value;
            case 2:
                return "00" + value;
            case 3:
                return "0" + value;
            case 4:
                return value;
            default:
                throw new OutOfBoundException("Value has to long hex value");
        }
    }

    private String replaceChar(String str, char ch, int index) {
        return str.substring(0, index) + ch + str.substring(index + 1);
    }
}