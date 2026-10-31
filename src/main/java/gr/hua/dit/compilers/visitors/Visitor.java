package gr.hua.dit.compilers.visitors;

import gr.hua.dit.compilers.ast.*;

//interface defining visitor methods for each ast node
public interface Visitor<T> {
    //declarations
    T visit(FuncDefNode n) throws Exception;
    T visit(FuncDeclNode n) throws Exception;
    T visit(VarDefNode n) throws Exception;
    T visit(HeaderNode n) throws Exception;
    T visit(FormalNode n) throws Exception;

    //statements
    T visit(AssignNode n) throws Exception;
    T visit(ExitNode n) throws Exception;
    T visit(ReturnNode n) throws Exception;
    T visit(IfNode n) throws Exception;
    T visit(ElsifNode n) throws Exception;
    T visit(ForNode n) throws Exception;
    T visit(SkipNode n) throws Exception;
    T visit(ExprStmtNode n) throws Exception;

    //arithmetic expressions
    T visit(AddNode n) throws Exception;
    T visit(SubNode n) throws Exception;
    T visit(MultNode n) throws Exception;
    T visit(DivNode n) throws Exception;
    T visit(ModNode n) throws Exception;
    
    //logical & comparison expressions
    T visit(AndNode n) throws Exception;
    T visit(OrNode n) throws Exception;
    T visit(NotNode n) throws Exception;
    T visit(EqNode n) throws Exception;
    T visit(NeqNode n) throws Exception;
    T visit(LtNode n) throws Exception;
    T visit(GtNode n) throws Exception;
    T visit(LeqNode n) throws Exception;
    T visit(GeqNode n) throws Exception;

    //unary expressions
    T visit(UplusNode n) throws Exception;
    T visit(UminusNode n) throws Exception;

    //list & array operations
    T visit(ConsNode n) throws Exception;
    T visit(HeadNode n) throws Exception;
    T visit(TailNode n) throws Exception;
    T visit(NilNode n) throws Exception;
    T visit(NilqNode n) throws Exception;
    T visit(NewNode n) throws Exception;
    T visit(ArrayAccessNode n) throws Exception;

    //literals, variables & calls
    T visit(VariableNode n) throws Exception;
    T visit(IntNode n) throws Exception;
    T visit(CharNode n) throws Exception;
    T visit(BoolNode n) throws Exception;
    T visit(StringNode n) throws Exception;
    T visit(CallNode n) throws Exception;

    //blocks
    T visit(Block s) throws Exception;
}