package gr.hua.dit.compilers.ast;

import java.util.List;

public class HeaderNode {
    
    private String name;
    private Type returnType;
    private List<FormalNode> formals;

    public HeaderNode(String name, Type returnType, List<FormalNode> formals) {
        this.name = name;
        this.returnType = returnType;
        this.formals = formals;
    }

    public String getName() { return name; }
    public Type getReturnType() { return returnType; }
    public List<FormalNode> getFormals() { return formals; }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Header: " + name);
        if (returnType != null) {
            returnType.print(depth + 1);
        } else {
            System.out.println(indent + "  No return type");
        }
        for (FormalNode f : formals) {
            f.print(depth + 1);
        }
    }
}
