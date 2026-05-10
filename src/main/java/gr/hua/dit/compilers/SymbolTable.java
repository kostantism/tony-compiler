package gr.hua.dit.compilers;

import java.util.HashMap;
import java.util.Stack;

public class SymbolTable {
    
    static public Stack<HashMap<String, Symbol>> symbolTable = new Stack<>();

    public SymbolTable() {
        enter_scope();
    }


    public void enter_scope() {
        symbolTable.push(new HashMap<>());
    }

    public void exit_scope() {
        if(symbolTable.size() > 1) {
            symbolTable.pop();
        }
    }

    public void add_entry(String name, Symbol sym) {
        symbolTable.peek().put(name, sym);
    }

    public Symbol get_entry(String name) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            Symbol sym = symbolTable.get(i).get(name);
            if (sym != null) {
                return sym;
            }
        }
        return null;
    }

    // public boolean existsInCurrentScope(String name) {
    //     return symbolTχωσδωζable.peek().containsKey(name);
    // }

    
}
