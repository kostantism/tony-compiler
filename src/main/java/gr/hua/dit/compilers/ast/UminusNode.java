package gr.hua.dit.compilers.ast;

public class UminusNode extends Expr{

    private Expr e;

    public UminusNode(Expr e) {
        this.e = e;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Uminus:");
        e.print(depth + 1);
    }
    
}
