package gr.hua.dit.compilers.ast;

public class StringNode extends Expr {
    private String value;

    public StringNode(String value) {
        this.value = value; 
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "String: " + value);
    }
}
