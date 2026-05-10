package gr.hua.dit.compilers.ast;

public class ReturnNode extends Stmt{

    private Expr e;

    public ReturnNode(Expr e) {
        this.e = e;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Return:");
        e.print(depth + 1);
    }
    
}
