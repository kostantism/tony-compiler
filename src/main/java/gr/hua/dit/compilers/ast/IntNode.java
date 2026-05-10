package gr.hua.dit.compilers.ast;

public class IntNode extends Expr {
    private Integer value;

    public IntNode(Integer value) {
        this.value = value;
    }
    
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Int: " + value);
    }
}
