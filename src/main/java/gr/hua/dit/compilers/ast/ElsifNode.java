package gr.hua.dit.compilers.ast;

import java.util.List;

import gr.hua.dit.compilers.visitors.Visitor; 

public class ElsifNode {

    public Expr condition;
    public List<Stmt> stmts;

    public ElsifNode(Expr condition, List<Stmt> stmts) {
        this.condition = condition;
        this.stmts = stmts;
    }

    public void print(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Elsif:");
        condition.print(depth + 1);
        for (Stmt s : stmts) s.print(depth + 1);
    }

    public <T> T accept(Visitor<T> visitor) throws Exception {
        return visitor.visit(this);
    }
}
