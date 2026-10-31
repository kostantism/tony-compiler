package gr.hua.dit.compilers.ast;
import gr.hua.dit.compilers.visitors.Visitor;

//abstract base class for all declaration nodes in the ast
public abstract class Decl {

    //abstract print method for tree visualization
    abstract public void print(int depth);
    
    //accept method for visitor pattern implementation
    abstract public <T>  T accept(Visitor<T> visitor) throws Exception;
}