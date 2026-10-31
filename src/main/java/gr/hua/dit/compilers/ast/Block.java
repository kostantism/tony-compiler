package gr.hua.dit.compilers.ast;

import java.util.List;
import java.util.Map;
import gr.hua.dit.compilers.symbols.SymbolEntry;
import gr.hua.dit.compilers.visitors.Visitor;

//ast block node representing a section of code
public class Block {
    
    //list of declarations
    public List<Decl> decls; 
    //list of statements
    public List<Stmt> stmts; 

    //symbol table for the block
    public Map<String, SymbolEntry> table;

    //constructor
    public Block(List<Decl> decls, List<Stmt> stmts) {
        this.decls = decls;
        this.stmts = stmts;
    }

    //accept method for visitor pattern
    public <T> T accept(Visitor<T> visitor) throws Exception { 
        return visitor.visit(this);
    }

    //print method for debugging and tree visualization
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Block:");
        
        //print declarations
        if (decls != null) {
            System.out.println(indent + "  Declarations:");
            for (Decl d : decls) {
                if (d != null) d.print(depth + 2);
            }
        }
        
        //print statements
        if (stmts != null) {
            System.out.println(indent + "  Statements:");
            for (Stmt s : stmts) {
                if (s != null) s.print(depth + 2);
            }
        }
    }
}