package gr.hua.dit.compilers.ast;

public class CharNode extends Expr {
    private Character value;

    public CharNode(Character value) {
        this.value = value;
    }
    
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Char: " + value);
    }
    
}
