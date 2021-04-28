## Compilerbau

- dieses Projekt wurde im Rahmen des Moduls Compilerbau erstellt
- Ziel des Projektes war es einen Compiler für die Sprache Minijava FunProc zu erstellen
- der `JavaClassFileGenerator` wurde unabhängig von diesem Projekt geschrieben

### Compilation

- Abhängigkeiten: javacc, javac, java

```
javacc parser.jj
javac *.java
```

### Usage

```
java Parser < source_code.minijava
```

- der jvm bytecode befindet sich nun in der `byteCode.class` Datei
- dieser kann wie gehabt ausgeführt werden

```
java byteCode
```