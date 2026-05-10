package gr.hua.dit.compilers.ast;

import java.util.List;

public class VarDefNode extends Stmt{

    private Type type;
    private List<String> varNames;

    public VarDefNode(Type type, List<String> varNames) {
        this.type = type;
        this.varNames = varNames;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "VarDef:");
        type.print(depth + 1);
        for (String var : varNames) {
            System.out.println(indent + "  " + var);
        }
    }
    
}
