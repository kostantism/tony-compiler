package gr.hua.dit.compilers;

//exception thrown when a variable is used without being defined
public class UndefinedVarException extends SemanticError {
    
    public UndefinedVarException(String msg) {
        super(msg);
    }

}
