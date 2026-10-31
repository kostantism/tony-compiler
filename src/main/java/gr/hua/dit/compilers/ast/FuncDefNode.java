package gr.hua.dit.compilers.ast;

import java.util.List;
import java.util.Map;

import gr.hua.dit.compilers.symbols.SymbolEntry;
import gr.hua.dit.compilers.visitors.Visitor;

public class FuncDefNode extends Decl{

    public String name;
    public Type returnType;
    public List<FormalNode> arguments;
    public List<Decl> defs;
    public List<Stmt> stmts;

    public Map<String, SymbolEntry> table;

    public FuncDefNode(String name, Type returnType, List<FormalNode> arguments, List<Decl> defs, List<Stmt> stmts){
        this.name = name;
        this.returnType = returnType;
        this. arguments = arguments;
        this.defs = defs;
        this.stmts = stmts;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "FuncDef: " + name);
        if (returnType != null) {
            returnType.print(depth + 1);
        } else {
            System.out.println(indent + "  No return type");
        }
        for (FormalNode arg : arguments) arg.print(depth + 1);
        for (Decl def : defs) {
            if (def instanceof VarDefNode) ((VarDefNode)def).print(depth + 1);
            else if (def instanceof FuncDefNode) ((FuncDefNode)def).print(depth + 1);
        }
        for (Stmt stmt : stmts) stmt.print(depth + 1);
    }

    public <T>  T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
