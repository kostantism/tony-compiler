package gr.hua.dit.compilers.ast;

public class UplusNode extends Expr{

    private Expr e;

    public UplusNode(Expr e) {
        this.e = e;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Uplus:");
        e.print(depth + 1);
    }
    
}
