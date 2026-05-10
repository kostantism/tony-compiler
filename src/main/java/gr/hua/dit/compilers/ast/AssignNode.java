package gr.hua.dit.compilers.ast;

public class AssignNode extends Stmt{

    private Expr var;
    private Expr e;

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
    
}
