package gr.hua.dit.compilers;

//exception thrown when a type mismatch is detected
public class TypeError extends SemanticError {

    public TypeError(String msg) {
        super(msg);
    }
    
}