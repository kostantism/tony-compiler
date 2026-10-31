package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class NilqNode extends Expr{

    public Expr listName;

    public NilqNode(Expr listName) {
        this.listName = listName;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Nil?:");
        listName.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
