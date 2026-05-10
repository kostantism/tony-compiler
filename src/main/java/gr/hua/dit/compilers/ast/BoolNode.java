package gr.hua.dit.compilers.ast;

public class BoolNode extends Expr {
    private Boolean value;

    public BoolNode(Boolean value) {
        this.value = value;
    }
    
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Bool: " + value);
    }
    
}
