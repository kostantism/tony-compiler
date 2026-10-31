package gr.hua.dit.compilers.ast;

import java.util.List;

import gr.hua.dit.compilers.visitors.Visitor;

public class FuncDeclNode extends Decl{
    
    public String name;
    public Type returnType;
    public List<FormalNode> arguments;

    public FuncDeclNode(String name, Type returnType, List<FormalNode> arguments){
        this.name = name;
        this.returnType = returnType;
        this. arguments = arguments;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "FuncDecl: " + name);
        if (returnType != null) {
            returnType.print(depth + 1);
        } else {
            System.out.println(indent + "  No return type");
        }
        for (FormalNode arg : arguments) arg.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
