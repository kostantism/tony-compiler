package gr.hua.dit.compilers.ast;

public class SkipNode extends Stmt{

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Skip");
    }
}
