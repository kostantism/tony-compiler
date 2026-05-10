package gr.hua.dit.compilers.ast;

public class HeadNode extends Expr{

    private Expr listName;

    public HeadNode(Expr listName) {
        this.listName = listName;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Head:");
        listName.print(depth + 1);
    }
    
}
