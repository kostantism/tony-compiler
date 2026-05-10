package gr.hua.dit.compilers.ast;

public class GeqNode extends Expr{

    private Expr e1;
    private Expr e2;

    public GeqNode(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Greater Equal:");
        e1.print(depth + 1);
        e2.print(depth + 1);
    }
    
}
