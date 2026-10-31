# Tony Language Compiler

This repository contains the development of a compiler for the **Tony programming language**, a statically typed language. Τhe project implements the front-end phases of the compiler, including lexical analysis, syntax analysis, Abstract Syntax Tree (AST) generation, Semantic Analysis & Symbol Table and the Code Generation.

## Compiler Pipeline
### 1. Lexical Analysis (`lexer.flex`)
* Built with **JFlex**. Reads input character-by-character and generates `Symbol` tokens for **JavaCUP**.
* **Keywords & Identifiers:** Supports all keywords (`and`, `bool`, `char`, `decl`, `def`, `elsif`, `nil?`, `ref`, etc.) and matching identifiers `[a-zA-Z][a-zA-Z0-9_?]*`.
* **String & Character Literals:** Full escape sequence support (`\n`, `\t`, `\r`, `\0`, `\\`, `\'`, `\"`, `\xNN`).
* **Multi-line Comments:** Implements state-based nested comment tracking (`<* ... *>`) using a dedicated `COMMENT` lexical state and nesting depth counter `c`.
* **Error Handling:** Unmatched characters trigger an `Illegal character` error; unclosed multi-line comments at EOF trigger an error.

### 2. Syntax Analysis & AST Construction (`parser.cup`)
* Built with **JavaCUP** (LALR(1) parser). Enforces Tony's formal grammar and builds the AST nodes upon rule reduction.
* **Operator Precedence:**
  1. `OR`, `AND`, `NOT` (Lowest)
  2. Comparison operators (`=`, `<>`, `<`, `>`, `<=`, `>=`) and `CONS` (`#`)
  3. Additive (`+`, `-`) and Multiplicative (`*`, `/`, `MOD`)
  4. Unary operations (`UPLUS`, `UMINUS`) (Highest)
* Resolves multi-character operator token conflicts (e.g., matching `:=` before `:` and `<=` before `<`).

### 3. Abstract Syntax Tree (AST) (`ast/`)
* **`Expr.java` & `Stmt.java`:** Base abstract classes implementing sub-tree printing and the Visitor pattern (`accept(Visitor v)`).
* **`Decl.java`:** Handles function definitions (`FuncDefNode`), forward declarations (`FuncDeclNode`), and variable declarations (`VarDefNode`).
* **Control Flow Nodes:** `IfNode` (with `ElsifNode` branches), `ForNode`, `ReturnNode`, `ExitNode`, and `SkipNode`.
* **Data Structure Nodes:** `ConsNode`, `HeadNode`, `TailNode`, `NilNode`, `NilqNode`, `ArrayAccessNode`, and `NewNode`.

### 4. Semantic Analysis & Symbol Management (`visitors/`, `symbols/`)
Uses the Visitor Pattern to decouple tree traversal from validation logic:
* **Symbol Table (`SymbolTable.java`):** Stack of hash maps managing nested lexical scopes (Pascal-style scoping). Supports variable shadow detection (`DuplicateDeclException`), resolution (`UndefinedVarException`), and forward declarations (`decl`).
* **Scope Checker (`ScopeChecker.java`):** Validates variable/function visibility and populates scope tables.
* **Type Checker (`TypeChecker.java`):** Validates static types across expressions, array dimensions, function argument lists, and value vs. reference (`ref`) parameter passing semantics.

### 5. Bytecode Generation (`JavaASMGenerator.java`)
* Uses **OW2 ASM** (`ClassWriter`, `MethodVisitor`) to generate JVM instruction sequences (`ILOAD`, `ASTORE`, `INVOKESTATIC`, `IF_ICMPNE`, etc.).
* Outputs standalone `.class` bytecode files targeting JVM environments.

## Examples
The repository includes a folder with sample programs written in Tony to demonstrate the language's syntax and test the parser's capabilities:

* **`hello.tony`**: A classic "Hello World" program.
* **`bubblesort.tony`**: Implementation of the Bubble Sort algorithm using arrays and nested functions.
* **`quicksort.tony`**: A more complex sorting example utilizing lists and recursion.
* **`hanoi.tony`**: The Tower of Hanoi puzzle, demonstrating recursive function calls.
* **`primes.tony`**: An algorithm to find and count prime numbers, showcasing logical operators and loops.
* **`reverse.tony`**: Reverses a string using dynamic memory allocation (`new char[l+1]`).

## Tools & Technologies
* **Java:** Primary implementation language.
* **JFlex:** Lexical analyzer generator for Java.
* **CUP (Construction of Useful Parsers):** LALR parser generator for Java.
* **Apache Ant/Maven:** (Optional - mention if you use any build tool).

## Runtime Library & Capabilities

The custom runtime support is defined in `gr.hua.dit.compilers.runtime.TonyRuntime` and `TonyList`.

* **Implemented I/O Functions:**
  * `puti(int x)`: Prints an integer to standard output.
  * `puts(String s)`: Prints a string to standard output.
* *Note:* Other library functions (`geti`, `abs`, `strlen`, etc.) are recognized by the Symbol Table during semantic analysis but currently throw runtime exceptions if invoked during execution.

## Building the Compiler

Build the project and generate the executable compiler JAR using the Maven wrapper:

**Linux / macOS:**
```bash
./mvnw clean package
```

**Windows (PowerShell / CMD):**
```cmd
.\mvnw.cmd clean package
```

This generates `target/compiler-0.1.jar`.

---

## Execution & Usage

### 1. Compiling a Tony Source File
Pass a `.tony` source file as the primary argument:

```bash
java -jar target/compiler-0.1.jar examples/hello.tony
```

### 2. Interactive Input (`stdin`)
If no file argument is provided, the compiler reads input directly from standard input:

```bash
java -jar target/compiler-0.1.jar
```

### 3. Running Generated Bytecode (`.class`)
To run the generated bytecode file, include the target class directory in your classpath:

**Linux / macOS:**
```bash
java -cp ".:target/classes" hello
```

**Windows:**
```cmd
java -cp ".;target/classes" hello
```

---
