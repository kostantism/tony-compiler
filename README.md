# Tony Language Compiler (Work in Progress)

This repository contains the ongoing development of a compiler for the **Tony programming language**, a statically typed language. Currently, the project implements the front-end phases of the compiler, including lexical analysis, syntax analysis, and Abstract Syntax Tree (AST) generation.

## Project Status: Front-End Completed
The compiler is currently in its early stages. The following components have been fully implemented:
- **Lexical Analysis (Lexer):** Tokenizes source code using JFlex.
- **Syntax Analysis (Parser):** Validates language grammar using LALR parsing (CUP).
- **AST Generation:** Constructs a complete Abstract Syntax Tree representing the program structure.

## Features implemented
* **Lexer (`lexer.flex`):** Handles keywords, identifiers, constants (integers, characters, strings), operators, and comments.
* **Parser (`parser.cup`):** Implements the formal grammar of Tony, including function definitions, variable declarations, control flow (if, for, exit), and complex expressions.
* **AST Nodes:** A robust object-oriented structure to represent program elements like `Stmt`, `Expr`, `Type`, and `Decl`.

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

## Key Files
* `lexer.flex`: JFlex specification for lexical analysis.
* `parser.cup`: CUP specification for syntax analysis and AST construction.
* `src/ast/`: Directory containing Java classes for the Abstract Syntax Tree nodes.

## Building the Project
To generate the executable JAR file, use the provided Maven wrapper from the project's root directory:

**Windows:**
```bash
./mvnw.cmd clean package
```

**Linux/Mac:**
```bash
./mvnw clean package
```

## Execution & Testing
Running the Lexical/Syntax Analyzer:

**Windows - Linux/Mac:**
```bash
java -jar target/compiler-0.1.jar examples/bubblesort.tony
```

(You can replace bubblesort.tony with any other file located in the examples folder).

You can also run the compiler without providing a file argument. In this case, the analyzer will read input directly from the standard input:

```bash
java -jar target/compiler-0.1.jar
```