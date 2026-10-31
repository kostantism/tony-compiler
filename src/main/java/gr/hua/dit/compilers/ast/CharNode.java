package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class CharNode extends Expr {
    public Character value;

    public CharNode(Character value) {
        this.value = value;
    }
    
    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Char: " + value);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
