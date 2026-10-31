package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class ExprStmtNode extends Stmt {
    
    public Expr expr;

    public ExprStmtNode(Expr expr) {
        this.expr = expr;
    }

    public void print(int depth) {
        expr.print(depth);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
