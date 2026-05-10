package gr.hua.dit.compilers.ast;

public class NewNode extends Expr {
    private Type type;
    private Expr size;

    public NewNode(Type type, Expr size) {
        this.type = type;
        this.size = size;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "New:");
        type.print(depth + 1);
        size.print(depth + 1);
    }
    
    
}
