package gr.hua.dit.compilers.ast;

import java.util.List;

import gr.hua.dit.compilers.visitors.Visitor; 

public class CallNode extends Expr {
    public String functionName;
    public List<Expr> arguments;
     

    public CallNode(String functionName, List<Expr> arguments){
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Call: " + functionName);
        for (Expr arg : arguments) {
            arg.print(depth + 1);
        }
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
