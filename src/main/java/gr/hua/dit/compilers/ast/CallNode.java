package gr.hua.dit.compilers.ast;

import java.util.List;

public class CallNode extends Expr {
    private String functionName;
    private List<Expr> arguments;
     

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
}
