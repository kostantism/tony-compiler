package gr.hua.dit.compilers.ast;

public class NotNode extends Expr{

    private Expr e;

    public NotNode(Expr e) {
        this.e = e;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Not:");
        e.print(depth + 1);
    }
    
}
