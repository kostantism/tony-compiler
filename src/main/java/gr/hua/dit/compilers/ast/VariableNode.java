package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

public class VariableNode extends Expr {
    public String varName;

    public VariableNode(String varName) {
        this.varName = varName; 
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Variable: " + varName);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
    
}
