package gr.hua.dit.compilers.ast;

public class NilNode extends Expr{
    
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Nil");
    }
}
