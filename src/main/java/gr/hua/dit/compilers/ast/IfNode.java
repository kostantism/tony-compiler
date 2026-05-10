package gr.hua.dit.compilers.ast;

import java.util.List;

public class IfNode extends Stmt{
    
    private Expr condition;
    private List<Stmt> thenStmts;
    private List<ElsifNode> elsifs;
    private List<Stmt> elseStmts;

    public IfNode(Expr condition, List<Stmt> thenStmts, List<ElsifNode> elsifs, List<Stmt> elseStmts) {
        this.condition = condition;
        this.thenStmts = thenStmts;
        this.elsifs = elsifs;
        this.elseStmts = elseStmts;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "If:");
        condition.print(depth + 1);
        System.out.println(indent + "Then:");
        for (Stmt s : thenStmts) s.print(depth + 1);
        for (ElsifNode elsif : elsifs) elsif.print(depth);
        if (elseStmts != null) {
            System.out.println(indent + "Else:");
            for (Stmt s : elseStmts) s.print(depth + 1);
        }
    }
}
