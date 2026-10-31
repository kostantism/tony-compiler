package gr.hua.dit.compilers.visitors;

import gr.hua.dit.compilers.ast.*;
import gr.hua.dit.compilers.symbols.SymbolTable;
import java_cup.runtime.Symbol;
import gr.hua.dit.compilers.symbols.Kind;
import java.util.ArrayList;
import java.util.List;

//visitor class to perform scope checking and populate symbol table
public class ScopeChecker extends AbstractVisitor<Void> {
    
    private SymbolTable t;

    public ScopeChecker() {
        t = new SymbolTable();
    }

    //visit block and manage scope lifecycle
    @Override
    public Void visit(Block b) throws Exception {
        t.enterScope();
        super.visit(b); 
        b.table = t.getScope(); 
        t.exitScope();
        return null;
    }

    //visit variable definition
    @Override
    public Void visit(VarDefNode n) throws Exception {
        for (String varName : n.varNames) {
            t.addVariable(varName, n.type);
        }
        return null;
    }

    //visit function definition and handle parameters
    @Override
    public Void visit(FuncDefNode n) throws Exception {
        List<Type> paramTypes = new ArrayList<>();
        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                for (int i = 0; i < formal.names.size(); i++) {
                    paramTypes.add(formal.type);
                }
            }
        }

        t.addFunction(n.name, n.returnType, paramTypes);

        t.enterScope();

        if (n.arguments != null) { 
            for (FormalNode formal : n.arguments) {
                for (String argName : formal.names) {
                    t.addParameter(argName, formal.type, formal.isRef);
                }
            }
        }

        if (n.defs != null) {
            for (Decl d : n.defs) d.accept(this);
        }
        if (n.stmts != null) {
            for (Stmt s : n.stmts) s.accept(this);
        }

        n.table = t.getScope();

        t.exitScope();
        return null;
    }

    //visit function declaration
    @Override
    public Void visit(FuncDeclNode n) throws Exception {
        List<Type> paramTypes = new ArrayList<>();
        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                for (int i = 0; i < formal.names.size(); i++) {
                    paramTypes.add(formal.type);
                }
            }
        }
        t.addFunctionDecl(n.name, n.returnType, paramTypes);
        return null;
    }

    //visit variable usage
    @Override
    public Void visit(VariableNode n) throws Exception {
        t.getEntry(n.varName); 
        return null;
    }

    //visit function call
    @Override
    public Void visit(CallNode n) throws Exception {
        t.getEntry(n.functionName);
        
        return super.visit(n);
    }

    //visit assignment statement
    @Override
    public Void visit(AssignNode n) throws Exception {
        return super.visit(n);
    }

    //visit array element access
    @Override
    public Void visit(ArrayAccessNode n) throws Exception {
        n.name.accept(this); 
        n.i.accept(this);
        return null;
    }

}