package gr.hua.dit.compilers.visitors;

import java.util.List;

import gr.hua.dit.compilers.TypeError;
import gr.hua.dit.compilers.ast.*;
import gr.hua.dit.compilers.symbols.SymbolEntry;
import gr.hua.dit.compilers.symbols.SymbolTable;
import gr.hua.dit.compilers.symbols.Kind;

public class TypeChecker extends AbstractVisitor<Type> {

    private SymbolTable t;

    private Type currentReturnType = Type.VOID; 

    public TypeChecker() {
        t = new SymbolTable();
    }

    public void assertType(Expr e, Type t) throws Exception {
        if (e.type == null || !e.type.equals(t)) {
            throw new TypeError("Type mismatch: expected " + t + ", got " + e.type);
        }
    }

    @Override
    public Type visit(Block n) throws Exception {
        t.enterScope(); 

        if (n.decls != null) {
            for (Decl d : n.decls) {
                if (d != null) d.accept(this);
            }
        }

        if (n.stmts != null) {
            for (Stmt s : n.stmts) {
                if (s != null) s.accept(this);
            }
        }

        t.exitScope(); 

        return null;
    }
    
    @Override
    public Type visit(AddNode n) throws Exception {
        super.visit(n);
        assertType(n.e1, Type.INT);
        assertType(n.e2, Type.INT);
        n.type = Type.INT;
        return n.type;
    }

    @Override
    public Type visit(SubNode n) throws Exception {
        super.visit(n);
        assertType(n.e1, Type.INT);
        assertType(n.e2, Type.INT);
        n.type = Type.INT;
        return Type.INT;
    }

    @Override
    public Type visit(AndNode n) throws Exception {
        super.visit(n); 
        assertType(n.e1, Type.BOOL); 
        assertType(n.e2, Type.BOOL);
        n.type = Type.BOOL; 
        return n.type;
    }

    @Override
    public Type visit(MultNode n) throws Exception {
        super.visit(n); 
        assertType(n.e1, Type.INT); 
        assertType(n.e2, Type.INT);
        n.type = Type.INT; 
        return n.type;
    }

    @Override
    public Type visit(DivNode n) throws Exception {
        super.visit(n); 
        assertType(n.e1, Type.INT); 
        assertType(n.e2, Type.INT);
        n.type = Type.INT; 
        return n.type;
    }

    @Override
    public Type visit(ModNode n) throws Exception {
        super.visit(n); 
        assertType(n.e1, Type.INT); 
        assertType(n.e2, Type.INT);
        n.type = Type.INT; 
        return n.type;
    }

    @Override
    public Type visit(OrNode n) throws Exception {
        super.visit(n); 
        assertType(n.e1, Type.BOOL); 
        assertType(n.e2, Type.BOOL);
        n.type = Type.BOOL; 
        return n.type;
    }

    @Override
    public Type visit(NotNode n) throws Exception {
        super.visit(n); 
        assertType(n.e, Type.BOOL);
        n.type = Type.BOOL; 
        return n.type;
    }

    @Override
    public Type visit(UplusNode n) throws Exception {
        super.visit(n); 
        assertType(n.e, Type.INT);
        n.type = Type.INT; 
        return n.type;
    }

    @Override
    public Type visit(UminusNode n) throws Exception {
        super.visit(n); 
        assertType(n.e, Type.INT);
        n.type = Type.INT; 
        return n.type;
    }

    private Type checkComparison(Expr e1, Expr e2) throws Exception {
        if (e1.type == null || !e1.type.equals(e2.type)) {
            throw new TypeError("Type mismatch: expected " + e1.type + " got " + e2.type);
        }
        if (e1.type instanceof Type.ArrayType || e1.type instanceof Type.ListType) {
            throw new TypeError("comparison operators are only allowed for basic types");
        }
        return Type.BOOL;
    }

    @Override
    public Type visit(EqNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }

    @Override
    public Type visit(NeqNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }

    @Override
    public Type visit(LtNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }

    @Override
    public Type visit(GtNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }

    @Override
    public Type visit(LeqNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }

    @Override
    public Type visit(GeqNode n) throws Exception { 
        super.visit(n); 
        n.type = checkComparison(n.e1, n.e2); 
        return n.type; 
    }


    @Override
    public Type visit(StringNode n) throws Exception {
        n.type = new Type.ArrayType(Type.CHAR); 
        return n.type;
    }

    @Override
    public Type visit(NilNode n) throws Exception {
        n.type = Type.NIL; 
        return n.type;
    }

    @Override
    public Type visit(NilqNode n) throws Exception {
        super.visit(n);
        if (!(n.listName.type instanceof Type.ListType)) {
            throw new TypeError("nil? expects a list argument");
        }
        n.type = Type.BOOL;
        return n.type;
    }

    @Override
    public Type visit(TailNode n) throws Exception {
        super.visit(n);
        if (!(n.listName.type instanceof Type.ListType)) {
            throw new TypeError("tail expects a list argument");
        }
        n.type = n.listName.type; 
        return n.type;
    }

    @Override
    public Type visit(BoolNode n) throws Exception {
        n.type = Type.BOOL; 
        return n.type;
    }

    @Override
    public Type visit(CharNode n) throws Exception {
        n.type = Type.CHAR; 
        return n.type;
    }

    @Override
    public Type visit(IntNode n) throws Exception {
        n.type = Type.INT; 
        return n.type;
    }

    @Override
    public Type visit(IfNode n) throws Exception {
        super.visit(n); 
        assertType(n.condition, Type.BOOL);
        return Type.VOID; 
    }

    @Override
    public Type visit(ElsifNode n) throws Exception {
        super.visit(n);        
        assertType(n.condition, Type.BOOL);
        return Type.VOID;
    }

    @Override
    public Type visit(ArrayAccessNode n) throws Exception {
        super.visit(n);
        if (!(n.name.type instanceof Type.ArrayType)) {
            throw new TypeError("Subscripted value is not an array, got " + n.name.type);
        }
        assertType(n.i, Type.INT);
        Type.ArrayType arrType = (Type.ArrayType) n.name.type;
        n.type = arrType.elementType;
        return n.type;
    }

    private boolean isLValue(Expr e) {
        return (e instanceof VariableNode) || (e instanceof ArrayAccessNode);
    }

    @Override
    public Type visit(AssignNode n) throws Exception {
        super.visit(n);
        if (!isLValue(n.var)) {
            throw new TypeError("Left side of assignment must be an l-value");
        }
        assertType(n.e, n.var.type);
        return Type.VOID;
    }

    @Override
    public Type visit(VarDefNode n) throws Exception {
        if (n.varNames != null) {
            for (String name : n.varNames) {
                t.addVariable(name, n.type);
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visit(VariableNode n) throws Exception {
        SymbolEntry e = t.getEntry(n.varName); 
        if (e == null) {
            throw new TypeError("Variable '" + n.varName + "' is not declared in this scope");
        }
        n.type = e.getType(); 
        return n.type;
    }

    @Override
    public Type visit(ConsNode n) throws Exception {
        super.visit(n);

        if (!(n.e2.type instanceof Type.ListType)) {
            throw new TypeError("Right operand of # must be a list, but got " + n.e2.type);
        }

        Type.ListType listType = (Type.ListType) n.e2.type;

        if (listType.elementType == null) {
            n.type = new Type.ListType(n.e1.type);
        } else {
            assertType(n.e1, listType.elementType);
            n.type = listType;
        }

        return n.type;
    }

    public Type visit(Decl n) throws Exception {
        return Type.VOID; 
    }

    @Override
    public Type visit(ExitNode n) throws Exception {
        if (this.currentReturnType != Type.VOID) {
            throw new TypeError("exit can only appear in a void function");
        }
        return Type.VOID;
    }

    @Override
    public Type visit(SkipNode n) throws Exception {
        return Type.VOID;
    }

    @Override
    public Type visit(ExprStmtNode n) throws Exception {
        super.visit(n); 
        return Type.VOID;
    }

    @Override
    public Type visit(FormalNode n) throws Exception {
        for (String name : n.names) {
            t.addParameter(name, n.type, n.isRef);
        }
        return Type.VOID;
    }

    @Override
    public Type visit(ForNode n) throws Exception {
        if (n.init != null) {
            for (Stmt s : n.init) {
                if (s != null) s.accept(this);
            }
        }

        if (n.condition != null) {
            n.condition.accept(this);
            assertType(n.condition, Type.BOOL);
        }

        if (n.step != null) {
            for (Stmt s : n.step) {
                if (s != null) s.accept(this);
            }
        }

        if (n.body != null) {
            for (Stmt s : n.body) {
                if (s != null) s.accept(this);
            }
        }

        return Type.VOID;
    }

    @Override
    public Type visit(FuncDeclNode n) throws Exception {
        List<Type> paramTypes = new java.util.ArrayList<>();
        
        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                if (formal != null && formal.names != null) {
                    for (int i = 0; i < formal.names.size(); i++) {
                        paramTypes.add(formal.type);
                    }
                }
            }
        }

        Type retType = (n.returnType != null) ? n.returnType : Type.VOID;
        
        t.addFunctionDecl(n.name, retType, paramTypes);

        return Type.VOID;
    }

    @Override
    public Type visit(FuncDefNode n) throws Exception {
        List<Type> paramTypes = new java.util.ArrayList<>();
        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                if (formal != null && formal.names != null) {
                    for (int i = 0; i < formal.names.size(); i++) {
                        paramTypes.add(formal.type);
                    }
                }
            }
        }

        Type retType = (n.returnType != null) ? n.returnType : Type.VOID;

        t.addFunction(n.name, retType, paramTypes);

        Type previousRetType = this.currentReturnType; 
        this.currentReturnType = retType;

        t.enterScope();

        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                if (formal != null) formal.accept(this);
            }
        }

        if (n.defs != null) {
            for (Decl d : n.defs) {
                if (d != null) d.accept(this);
            }
        }

        if (n.stmts != null) {
            for (Stmt s : n.stmts) {
                if (s != null) s.accept(this);
            }
        }

        t.exitScope();

        this.currentReturnType = previousRetType;

        return Type.VOID;
    }

    @Override
    public Type visit(HeadNode n) throws Exception {
        super.visit(n);
        if (!(n.listName.type instanceof Type.ListType)) {
            throw new TypeError("Argument to head must be a list, got " + n.listName.type);
        }
        Type.ListType listType = (Type.ListType) n.listName.type;
        if (listType.elementType == null) {
            throw new TypeError("Cannot perform hea on an empty list");
        }
        n.type = listType.elementType;
        return n.type;
    }

    @Override
    public Type visit(NewNode n) throws Exception {
        n.size.accept(this);
        assertType(n.size, Type.INT);
        n.type = new Type.ArrayType(n.elementType);
        return n.type;
    }

    @Override
    public Type visit(ReturnNode n) throws Exception {
        if (n.e != null) {
            n.e.accept(this);
            
            if (this.currentReturnType == Type.VOID) {
                throw new TypeError("A void function cannot return a value");
            }
            
            assertType(n.e, this.currentReturnType);
        } else {
            if (this.currentReturnType != Type.VOID) {
                throw new TypeError("Non-void function expects a return value");
            }
        }

        return Type.VOID;
    }

    @Override
    public Type visit(CallNode n) throws Exception {
        SymbolEntry entry = t.getEntry(n.functionName);

        if (entry.getKind() != Kind.FUNCTION && entry.getKind() != Kind.FUNC_DECL) {
            throw new TypeError("Identifier '" + n.functionName + "' is not a function");
        }

        List<Type> expectedTypes = entry.getParameterTypes();
        int expectedSize = (expectedTypes != null) ? expectedTypes.size() : 0;
        int actualSize = (n.arguments != null) ? n.arguments.size() : 0;

        if (expectedSize != actualSize) {
            throw new TypeError("Function '" + n.functionName + "' expects " + 
                                expectedSize + " arguments, but got " + actualSize);
        }

        if (n.arguments != null) {
            for (int i = 0; i < actualSize; i++) {
                Expr actualArg = n.arguments.get(i);
                Type expectedType = expectedTypes.get(i);

                if (actualArg != null) {
                    actualArg.accept(this);

                    if (!actualArg.type.equals(expectedType)) {
                        throw new TypeError("Argument " + (i + 1) + " of function '" + n.functionName + 
                                            "' mismatch: expected " + expectedType + " got " + actualArg.type);
                    }
                }
            }
        }

        n.type = entry.getType();
        return n.type;
    }

}
