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

### Example

- Beispiel Ausgabe für `examples/1_gültig.minijava`

```
Main: Const declaration succesfull
Main: Variable declaration succesfull

Main: Statement evaluation successful

MAIN:
Byte-Code: 
1059b3[heute]1000b3[woche]b2[heute]1007b8(calcWoche)b8(print)b1

Mnenomics: 
0:      BIPUSH 0x59
2:      PUTSTATIC [heute]
5:      BIPUSH 0x00
7:      PUTSTATIC [woche]
10:     GETSTATIC [heute]
13:     BIPUSH 0x07
15:     INVOKESTATIC (calcWoche)
18:     INVOKESTATIC (print)
21:     RETURN

calcWoche
Byte-Code: 
10003602150015016c10016036021502ac

Mnenomics: 
0:      BIPUSH 0x00
2:      ISTORE 0x02
4:      ILOAD 0x00
6:      ILOAD 0x01
8:      IDIV
9:      BIPUSH 0x01
11:     IADD
12:     ISTORE 0x02
14:     ILOAD 0x02
16:     IRETURN



Ausdruck syntaktisch ok!
Binary file "byteCode.class" created.
Verbose file "byteCodeV.txt" created.
```