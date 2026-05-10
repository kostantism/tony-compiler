package gr.hua.dit.compilers.ast;

public class NilqNode extends Expr{

    private Expr listName;

    public NilqNode(Expr listName) {
        this.listName = listName;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Nil?:");
        listName.print(depth + 1);
    }
    
}
