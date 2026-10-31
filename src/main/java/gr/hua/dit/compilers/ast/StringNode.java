package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class StringNode extends Expr {
    public String value;

    public StringNode(String value) {
        this.value = value; 
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "String: " + value);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
