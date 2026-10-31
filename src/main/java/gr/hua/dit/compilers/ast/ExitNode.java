package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class ExitNode extends Stmt{

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Exit");
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
