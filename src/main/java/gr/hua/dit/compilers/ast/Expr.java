package gr.hua.dit.compilers.ast;

import gr.hua.dit.compilers.visitors.Visitor;

//abstract base class for all expression nodes in the ast
public abstract class Expr {
    //data type of the expression
    public Type type;
    
    //abstract print method for tree visualization
    public abstract void print(int depth);
    
    //accept method for visitor pattern implementation
    abstract public <T> T accept(Visitor<T> visitor) throws Exception;
}