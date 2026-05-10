package gr.hua.dit.compilers.ast;

public class ConsNode extends Expr{

    private Expr e1;
    private Expr e2;

    public ConsNode(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Cons:");
        e1.print(depth + 1);
        e2.print(depth + 1);
    }
    
}
