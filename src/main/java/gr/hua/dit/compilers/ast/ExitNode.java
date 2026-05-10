package gr.hua.dit.compilers.ast;

public class ExitNode extends Stmt{

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Exit");
    }
    
}
