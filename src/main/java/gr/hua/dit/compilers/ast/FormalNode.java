package gr.hua.dit.compilers.ast;

import java.util.List;

import gr.hua.dit.compilers.visitors.Visitor;

public class FormalNode {
    public boolean isRef;
    public Type type;
    public List<String> names;

    public FormalNode(boolean isRef, Type type, List<String> names) {
        this.isRef = isRef;
        this.type = type;
        this.names = names;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Formal: " + (isRef ? "ref " : "") + names);
        type.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
