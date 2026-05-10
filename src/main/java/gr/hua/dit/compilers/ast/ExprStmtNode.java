package gr.hua.dit.compilers.ast;

public class ExprStmtNode extends Stmt {
    
    private Expr expr;

    public ExprStmtNode(Expr expr) {
        this.expr = expr;
    }

    public void print(int depth) {
        expr.print(depth);
    }
}
