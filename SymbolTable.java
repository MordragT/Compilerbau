import java.util.HashMap;

import java.lang.Exception;
import java.lang.Integer;

public class SymbolTable {
	// id, value
	private HashMap<String, Data> table;
	// addr, value
	private HashMap<Integer, Integer> addressTable;

	private HashMap<String, Method> methods;

	// Faengt bei 1 an um nicht mit String[] args in Konflikt zu kommen
	private int addrCounter = 0;

	public SymbolTable() {
		this.table = new HashMap<String, Data>();
		this.addressTable = new HashMap<Integer, Integer>();
		this.methods = new HashMap<String, Method>();
	}

	public HashMap<String, Data> dataTable() {
		return this.table;
	}

	public HashMap<Integer, Integer> addrTable() {
		return this.addressTable;
	}

	public HashMap<String, Method> methodTable() {
		return this.methods;
	}

	public void addMethod(Method method) {
		this.methods.put(method.name(), method);
	}

	public void addConstant(String id, String wert) throws SymbolAlreadyDefinedException {
		if (this.table.containsKey(id)) {
			throw new SymbolAlreadyDefinedException(id);
		} else {
			this.table.put(id, new Data(wert, DataKind.Constant));
		}
	}

	public void addVariable(String id) throws SymbolAlreadyDefinedException {
		if (this.table.containsKey(id)) {
			throw new SymbolAlreadyDefinedException(id);
		} else {
			this.table.put(id, new Data(this.addrCounter, DataKind.Variable));
			this.addrCounter += 1;
		}
	}

	public void addParameter(String id) throws SymbolAlreadyDefinedException {
		if (this.table.containsKey(id)) {
			throw new SymbolAlreadyDefinedException(id);
		} else {
			this.table.put(id, new Data(this.addrCounter, DataKind.Parameter));
			this.addrCounter += 1;
		}
	}

	// Returns true if address table contained this address already
	// Returns false if address had to be putin the address table
	public boolean setVariable(String addrStr, String valueStr) {
		int addr = Integer.parseInt(addrStr);
		int value = Integer.parseInt(valueStr);
		if (this.addressTable.containsKey(addr)) {
			this.addressTable.replace(addr, value);
			return true;
		} else {
			this.addressTable.put(addr, value);
			return false;
		}
	}

	// Returns true if address table contained this address already
	// Returns false if address had to be putin the address table
	public boolean setVariable(int addr, int value) {
		if (this.addressTable.containsKey(addr)) {
			this.addressTable.replace(addr, value);
			return true;
		} else {
			this.addressTable.put(addr, value);
			return false;
		}
	}

	public Data getData(String id) throws UnknownSymbolException {
		if (this.table.containsKey(id)) {
			return this.table.get(id);
		} else {
			throw new UnknownSymbolException(id);
		}
	}

	// public Data getData(String id, Method main) throws UnknownSymbolException {
	// if (this.table.containsKey(id)) {
	// return this.table.get(id);
	// } else {
	// return main.symbolTable().getData(id);
	// }
	// }

	public Method getMethod(String name) throws UnknownSymbolException {
		if (this.methods.containsKey(name)) {
			return this.methods.get(name);
		} else {
			throw new UnknownSymbolException(name);
		}
	}
}
