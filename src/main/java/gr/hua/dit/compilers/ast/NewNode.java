package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class NewNode extends Expr {
    public Type elementType;
    public Expr size;

    public NewNode(Type elementType, Expr size) {
        this.elementType = elementType;
        this.size = size;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "New:");
        elementType.print(depth + 1);
        size.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
    
}
