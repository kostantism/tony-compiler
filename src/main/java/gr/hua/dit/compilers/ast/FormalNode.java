package gr.hua.dit.compilers.ast;

import java.util.List;

public class FormalNode {
    private boolean isRef;
    private Type type;
    private List<String> names;

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
}
