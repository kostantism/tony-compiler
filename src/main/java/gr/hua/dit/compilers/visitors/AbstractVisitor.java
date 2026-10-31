package gr.hua.dit.compilers.visitors;

import gr.hua.dit.compilers.ast.*;

//abstract visitor implementation to traverse the ast nodes
public class AbstractVisitor<T> implements Visitor<T> {

    //visit function definitions
    @Override
    public T visit(FuncDefNode n) throws Exception {
        if (n.arguments != null) {
            for (FormalNode arg : n.arguments) arg.accept(this);
        }
        
        if (n.defs != null) {
            for (Decl def : n.defs) def.accept(this);
        }
        
        if (n.stmts != null) {
            for (Stmt s : n.stmts) s.accept(this);
        }
        
        return null;
    }

    //visit function declarations
    @Override
    public T visit(FuncDeclNode n) throws Exception {
        if (n.arguments != null) {
            for (FormalNode arg : n.arguments) {
                arg.accept(this);
            }
        }
        return null;
    }

    //visit variable definitions
    @Override
    public T visit(VarDefNode n) throws Exception {
        return null;
    }

    //visit function headers
    @Override
    public T visit(HeaderNode n) throws Exception {
        if (n.formals != null) {
            for (FormalNode f : n.formals) {
                f.accept(this);
            }
        }
        return null;
    }

    //visit formal parameters
    @Override
    public T visit(FormalNode n) throws Exception {
        return null;
    }

    //visit assignment statements
    @Override
    public T visit(AssignNode n) throws Exception {
        if (n.var != null) n.var.accept(this);
        
        if (n.e != null) n.e.accept(this);
        
        return null;
    }

    //visit if-then-else statements
    @Override
    public T visit(IfNode n) throws Exception {
        if (n.condition != null) n.condition.accept(this);
        
        if (n.thenStmts != null) {
            for (Stmt s : n.thenStmts) s.accept(this);
        }
        
        if (n.elsifs != null) {
            for (ElsifNode e : n.elsifs) e.accept(this);
        }
        
        if (n.elseStmts != null) {
            for (Stmt s : n.elseStmts) s.accept(this);
        }
        
        return null;
    }

    //visit elsif branches
    @Override
    public T visit(ElsifNode n) throws Exception {
        if (n.condition != null) {
            n.condition.accept(this);
        }
        
        if (n.stmts != null) {
            for (Stmt s : n.stmts) {
                s.accept(this);
            }
        }
        return null;
    }

    //visit for loops
    @Override
    public T visit(ForNode n) throws Exception {
        if (n.init != null) {
            for (Stmt s : n.init) s.accept(this);
        }
        
        if (n.condition != null) {
            n.condition.accept(this);
        }
        
        if (n.step != null) {
            for (Stmt s : n.step) s.accept(this);
        }
        
        if (n.body != null) {
            for (Stmt s : n.body) s.accept(this);
        }
        
        return null;
    }

    //visit return statements
    @Override
    public T visit(ReturnNode n) throws Exception {
        if (n.e != null) {
            n.e.accept(this);
        }
        return null;
    }

    //visit expression statements
    @Override
    public T visit(ExprStmtNode n) throws Exception {
        if (n.expr != null) {
            n.expr.accept(this);
        }
        return null;
    }

    //visit binary arithmetic and logical operations
    @Override
    public T visit(AddNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(SubNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(MultNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(DivNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(ModNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(AndNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(OrNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(EqNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(NeqNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(LtNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(GtNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(LeqNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(GeqNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public T visit(ConsNode n) throws Exception {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    //visit unary operations
    @Override
    public T visit(NotNode n) throws Exception {
        n.e.accept(this);
        return null;
    }

    @Override
    public T visit(UplusNode n) throws Exception {
        n.e.accept(this);
        return null;
    }

    @Override
    public T visit(UminusNode n) throws Exception {
        n.e.accept(this);
        return null;
    }

    //visit list operations
    @Override
    public T visit(HeadNode n) throws Exception {
        n.listName.accept(this);
        return null;
    }

    @Override
    public T visit(TailNode n) throws Exception {
        n.listName.accept(this);
        return null;
    }

    @Override
    public T visit(NilqNode n) throws Exception {
        n.listName.accept(this);
        return null;
    }

    //visit memory allocation
    @Override
    public T visit(NewNode n) throws Exception {
        n.size.accept(this);
        return null;
    }

    //visit array access
    @Override
    public T visit(ArrayAccessNode n) throws Exception {
        if (n.name != null) {
            n.name.accept(this);
        }
        
        if (n.i != null) {
            n.i.accept(this);
        }
        
        return null;
    }

    //visit function calls
    @Override
    public T visit(CallNode n) throws Exception {
        if (n.arguments != null) {
            for (Expr e : n.arguments) {
                e.accept(this);
            }
        }
        return null;
    }

    //visit leaf nodes and simple expressions
    @Override
    public T visit(ExitNode n) throws Exception { return null; }

    @Override
    public T visit(SkipNode n) throws Exception { return null; }

    @Override
    public T visit(VariableNode n) throws Exception { return null; }

    @Override
    public T visit(IntNode n) throws Exception { return null; }

    @Override
    public T visit(CharNode n) throws Exception { return null; }

    @Override
    public T visit(BoolNode n) throws Exception { return null; }

    @Override
    public T visit(StringNode n) throws Exception { return null; }
    
    @Override
    public T visit(NilNode n) throws Exception { return null; }

    //visit code block
    @Override
    public T visit(Block n) throws Exception {
        if (n.decls != null) {
            for (Decl d : n.decls) d.accept(this);
        }
        if (n.stmts != null) {
            for (Stmt s : n.stmts) s.accept(this);
        }
        return null;
    }
}