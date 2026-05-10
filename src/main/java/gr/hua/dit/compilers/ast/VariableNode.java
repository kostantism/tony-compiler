package gr.hua.dit.compilers.ast;

public class VariableNode extends Expr {
    private String varName;

    public VariableNode(String varName) {
        this.varName = varName; 
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Variable: " + varName);
    }
    
}
