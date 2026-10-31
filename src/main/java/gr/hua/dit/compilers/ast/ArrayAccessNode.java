package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class ArrayAccessNode extends Expr {

    public Expr name;
    public Expr i;

    public ArrayAccessNode(Expr name, Expr i) {
        this.name = name;
        this.i = i;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "ArrayAccess:");
        name.print(depth + 1);
        System.out.println(indent + "  Index:");
        i.print(depth + 2);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }

    
    
}
