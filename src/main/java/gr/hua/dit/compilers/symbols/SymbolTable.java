package gr.hua.dit.compilers.symbols;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import gr.hua.dit.compilers.DuplicateDeclException;
import gr.hua.dit.compilers.SemanticError;
import gr.hua.dit.compilers.UndefinedVarException;
import gr.hua.dit.compilers.ast.Type;

//symbol table class to manage scope and variable/function declarations
public class SymbolTable {
    
    //stack of maps representing nested scopes
    private Stack<Map<String,SymbolEntry>> scopes;

    //constructor initializing the global scope and built in functions
    public SymbolTable() {
        scopes = new Stack<>();
        enterScope(); 

        try {
            loadBuiltinFunc();
        } catch (SemanticError e) {
            throw new RuntimeException("Failed to load builtin functions: " + e.getMessage());
        }
    }

    public SymbolTable getSymbolTable() {
        return this;
    }

    //create a new scope
    public void enterScope() { scopes.add(new HashMap<>()); }
    //enter a specific existing scope
    public void enterScope(Map<String,SymbolEntry> table) { scopes.add(table); }
    //exit the current scope
    public void exitScope() { scopes.pop(); }
    //get the current scope level
    public int getCurrentScopeLevel() { return scopes.size() - 1; }

    //get the current scope map
    public Map<String,SymbolEntry> getScope() { return scopes.peek(); }

    //add a variable to the current scope
    public void addVariable(String name, Type type) throws SemanticError {
        if (existsInCurrentScope(name)) {
            throw new DuplicateDeclException("Variable " + name + " already defined in this scope");
        }
        SymbolEntry entry = new SymbolEntry(name, Kind.VARIABLE, type, getCurrentScopeLevel(), null);
        scopes.peek().put(name, entry);
    }

    //add a parameter to the current scope
    public void addParameter(String name, Type type, boolean isReference) throws SemanticError {
        if (existsInCurrentScope(name)) {
            throw new DuplicateDeclException("Parameter " + name + " already defined");
        }
        SymbolEntry entry = new SymbolEntry(name, Kind.PARAMETER, type, getCurrentScopeLevel(), null, isReference);
        scopes.peek().put(name, entry);
    }

    //add a function to the current scope
    public void addFunction(String name, Type returnType, List<Type> paramTypes) throws SemanticError {
        if (existsInCurrentScope(name)) {
            SymbolEntry existing = scopes.peek().get(name);
            if (existing.getKind() == Kind.FUNC_DECL) {
                scopes.peek().remove(name); 
            } else {
                throw new DuplicateDeclException("Function " + name + " already defined");
            }
        }
        SymbolEntry entry = new SymbolEntry(name, Kind.FUNCTION, returnType, getCurrentScopeLevel(), null, paramTypes);
        scopes.peek().put(name, entry);
    }

    //add a function declaration to the current scope
    public void addFunctionDecl(String name, Type returnType, List<Type> paramTypes) throws SemanticError {
        if (existsInCurrentScope(name)) {
            throw new DuplicateDeclException("Declaration of " + name + " already exists");
        }
        SymbolEntry entry = new SymbolEntry(name, Kind.FUNC_DECL, returnType, getCurrentScopeLevel(), null, paramTypes);
        scopes.peek().put(name, entry);
    }
    
    //search for an entry across all active scopes
    public SymbolEntry getEntry(String name) throws SemanticError {
        for ( Map<String, SymbolEntry> m : scopes.reversed()) {
            SymbolEntry e = m.get(name);
            if (e != null)
                return e;
        }
        throw new UndefinedVarException("Variable " + name + " is undefined");
    }

    //check if a symbol exists in the current scope
    public boolean existsInCurrentScope(String name) {
        return !scopes.isEmpty() && scopes.peek().containsKey(name);
    }

    //initialize built-in language functions
    private void loadBuiltinFunc() throws SemanticError {
        addFunction("puti", null, List.of(Type.INT));
        addFunction("putb", null, List.of(Type.BOOL));
        addFunction("putc", null, List.of(Type.CHAR));
        addFunction("puts", null, List.of(new Type.ArrayType(Type.CHAR)));
        addFunction("geti", Type.INT, List.of());
        addFunction("getb", Type.BOOL, List.of());
        addFunction("getc", Type.CHAR, List.of());
        addFunction("gets", null, List.of(Type.INT, new Type.ArrayType(Type.CHAR)));

        addFunction("abs", Type.INT, List.of(Type.INT));
        addFunction("ord", Type.INT, List.of(Type.CHAR));
        addFunction("chr", Type.CHAR, List.of(Type.INT));

        addFunction("strlen", Type.INT, List.of(new Type.ArrayType(Type.CHAR)));
        addFunction("strcmp", Type.INT, List.of(new Type.ArrayType(Type.CHAR), new Type.ArrayType(Type.CHAR)));
        addFunction("strcpy", null, List.of(new Type.ArrayType(Type.CHAR), new Type.ArrayType(Type.CHAR)));
        addFunction("strcat", null, List.of(new Type.ArrayType(Type.CHAR), new Type.ArrayType(Type.CHAR)));
    }
    
}