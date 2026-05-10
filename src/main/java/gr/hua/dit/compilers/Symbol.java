package gr.hua.dit.compilers;

import java.util.List;

import gr.hua.dit.compilers.ast.Type;

public class Symbol {

    private String name;
    private Kind kind;
    private Type type;
    private int scope;
    private String address;

    private boolean isReference;
    private List<Type> parameterTypes;

    // Constructor για μεταβλητές
    public Symbol(String name, Kind kind, Type type, int scope, String address) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.address = address;
    }

    // Constructor για παραμέτρους (για να ξέρουμε αν είναι ref)
    public Symbol(String name, Kind kind, Type type, int scope, String address, boolean isReference) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.address = address;
        this.isReference = isReference;
    }

    // Constructor για συναρτήσεις (για να ελέγχουμε τα ορίσματα στις κλήσεις)
    public Symbol(String name, Kind kind, Type type, int scope, String address, List<Type> parameterTypes) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.address = address;
        this.parameterTypes = parameterTypes;
    }

    public String getName() { return name; }
    public Kind getKind() { return kind; }
    public Type getType() { return type; }
    public int getScope() { return scope; }
    public String getAddress() { return address; }
    public boolean isReference() { return isReference; }
    public List<Type> getParameterTypes() { return parameterTypes; }
}
