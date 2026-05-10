package gr.hua.dit.compilers.ast;

import java.util.List;

public class ForNode extends Stmt{

    private List<Stmt> init;
    private Expr condition;
    private List<Stmt> step;
    private List<Stmt> body;

    public ForNode(List<Stmt> init, Expr condition, List<Stmt> step, List<Stmt> body) {
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "For:");
        System.out.println(indent + "  Init:");
        for (Stmt s : init) s.print(depth + 2);
        System.out.println(indent + "  Condition:");
        condition.print(depth + 2);
        System.out.println(indent + "  Step:");
        for (Stmt s : step) s.print(depth + 2);
        System.out.println(indent + "  Body:");
        for (Stmt s : body) s.print(depth + 2);
    }
    
}
