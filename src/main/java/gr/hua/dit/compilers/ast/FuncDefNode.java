package gr.hua.dit.compilers.ast;

import java.util.List;

public class FuncDefNode extends Stmt{

    private String name;
    private Type returnType;
    private List<FormalNode> arguments;
    private List<Stmt> defs;
    private List<Stmt> stmts;

    public FuncDefNode(String name, Type returnType, List<FormalNode> arguments, List<Stmt> defs, List<Stmt> stmts){
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
        for (Stmt def : defs) def.print(depth + 1);
        for (Stmt stmt : stmts) stmt.print(depth + 1);
    }
    
}
