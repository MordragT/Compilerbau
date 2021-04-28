public class Method {
    private SymbolTable symbolTable = new SymbolTable();
    private CodeGenerator codeGenerator = new CodeGenerator();
    private MethodKind kind;
    private String name;
    private int parameterCount;

    public Method(MethodKind kind, String name) {
        this.kind = kind;
        this.name = name;
        this.parameterCount = 0;
    }

    public int parameterCount() {
        return this.parameterCount;
    }

    public CodeGenerator codeGenerator() {
        return this.codeGenerator;
    }

    public SymbolTable symbolTable() {
        return this.symbolTable;
    }

    public String name() {
        return this.name;
    }

    public MethodKind kind() {
        return this.kind;
    }

    public void update() throws OutOfBoundException {
        this.codeGenerator.update(this.symbolTable);
    }

    public void updateStatic() throws OutOfBoundException {
        this.codeGenerator.updateStatic(this.symbolTable);
    }

    public void addParameter(String name) throws SymbolAlreadyDefinedException {
        this.parameterCount += 1;
        this.symbolTable.addParameter(name);
    }

    public void dereferenceStatic(String id) throws UnknownSymbolException, OutOfBoundException {
        Data val = this.symbolTable.getData(id);
        this.codeGenerator.dereferenceStatic(val, id);
    }

    public void dereference(Method main, String id) throws UnknownSymbolException, OutOfBoundException {
        try {
            Data val = this.symbolTable.getData(id);
            this.codeGenerator().dereference(val);
        } catch (UnknownSymbolException e1) {
            Data val = main.symbolTable().getData(id);
            this.codeGenerator().dereferenceStatic(val, id);
        }
    }

    public void callFunc(Method main, String name, int parameterCount)
            throws WrongParametersException, UnknownSymbolException, RWertException {
        Method method = main.symbolTable().getMethod(name);
        if (method.kind() != MethodKind.Function) {
            throw new RWertException(name);
        }
        if (method.parameterCount() != parameterCount) {
            throw new WrongParametersException(name, parameterCount);
        } else {
            this.codeGenerator.call(name);
        }
    }

    public void call(Method main, String name, int parameterCount)
            throws WrongParametersException, UnknownSymbolException {
        Method method = main.symbolTable().getMethod(name);
        if (method.parameterCount() != parameterCount) {
            throw new WrongParametersException(name, parameterCount);
        } else {
            this.codeGenerator.call(name);
        }
    }

    public void assignment(Method main, String id) throws LWertException, OutOfBoundException, UnknownSymbolException {
        try {
            Data val = this.symbolTable.getData(id);
            switch (val.kind()) {
                case Variable:
                case Parameter:
                    this.codeGenerator().assignment(val.inner());
                    break;
                default:
                    throw new LWertException(id);
            }
        } catch (UnknownSymbolException e1) {
            Data val = main.symbolTable().getData(id);
            switch (val.kind()) {
                case Variable:
                    this.codeGenerator().assignmentStatic(id);
                    break;
                default:
                    throw new LWertException(id);
            }
        }
    }

    public void assignmentStatic(String id) throws UnknownSymbolException, LWertException {
        Data val = this.symbolTable.getData(id);
        switch (val.kind()) {
            case Variable:
                this.codeGenerator.assignmentStatic(id);
                break;
            default:
                throw new LWertException(id);
        }
    }

    public MethodObject finalise() {
        return new MethodObject(this.name, this.parameterCount, this.codeGenerator.getByteCode());
    }
}