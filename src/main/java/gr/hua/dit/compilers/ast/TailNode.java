package gr.hua.dit.compilers.ast;

public class TailNode extends Expr{

    private Expr listName;

    public TailNode(Expr listName) {
        this.listName = listName;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Tail:");
        listName.print(depth + 1);
    }
    
}
