package gr.hua.dit.compilers.ast;

import java.util.List;

import gr.hua.dit.compilers.visitors.Visitor;

public class VarDefNode extends Decl{

    public Type type;
    public List<String> varNames;

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
    
    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
