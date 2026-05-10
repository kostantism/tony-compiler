package gr.hua.dit.compilers.ast;

public class ArrayAccessNode extends Expr {

    private Expr name;
    private Expr i;

    public ArrayAccessNode(Expr name, Expr i) {
        this.name = name;
        this.i = i;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "ArrayAccess:");
        name.print(depth + 1);
        System.out.println(indent + "  Index:");
        i.print(depth + 2);
    }
    
}
