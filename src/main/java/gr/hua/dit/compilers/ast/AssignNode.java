package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class AssignNode extends Stmt{

    public Expr var;
    public Expr e;

    public AssignNode(Expr var, Expr e) {
        this.var = var;
        this.e = e;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Assign:");
        var.print(depth + 1);
        e.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
