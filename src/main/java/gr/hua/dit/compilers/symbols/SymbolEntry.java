package gr.hua.dit.compilers.symbols;

import java.util.List;

import gr.hua.dit.compilers.ast.Type;

public class SymbolEntry {

    public String name;
    public Kind kind;
    public Type type;
    public int scope;
    public String address;

    public boolean isReference;
    public List<Type> parameterTypes;

    //constructor for variables
    public SymbolEntry(String name, Kind kind, Type type, int scope, String address) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.address = address;
    }

    //constructor for parameters
    public SymbolEntry(String name, Kind kind, Type type, int scope, String address, boolean isReference) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.address = address;
        this.isReference = isReference;
    }

    //constructor for functions
    public SymbolEntry(String name, Kind kind, Type type, int scope, String address, List<Type> parameterTypes) {
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

    public void setAddress(String address) {
        this.address = address;
    }
}
