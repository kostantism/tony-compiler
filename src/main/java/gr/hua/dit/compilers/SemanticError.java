package gr.hua.dit.compilers;

//base class for semantic errors
public class SemanticError extends Exception {
    
    public SemanticError(String msg) {
        super(msg);
    }

}