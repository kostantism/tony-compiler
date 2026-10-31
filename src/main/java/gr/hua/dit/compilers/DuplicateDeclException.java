package gr.hua.dit.compilers;

//exception thrown when a duplicate declaration is detected
public class DuplicateDeclException extends SemanticError {
    
    public DuplicateDeclException(String msg) {
        super(msg);
    }

}