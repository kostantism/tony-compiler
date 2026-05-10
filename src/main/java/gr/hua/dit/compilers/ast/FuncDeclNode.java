package gr.hua.dit.compilers.ast;

import java.util.List;

public class FuncDeclNode extends Stmt{
    
    private String name;
    private Type returnType;
    private List<FormalNode> arguments;

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
}
