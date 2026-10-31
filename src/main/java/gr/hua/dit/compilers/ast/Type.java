package gr.hua.dit.compilers.ast;

//abstract base class for language types
public abstract class Type {

    //predefined static type instances
    public static final Type INT = new IntType();
    public static final Type BOOL = new BoolType();
    public static final Type CHAR = new CharType();
    //void type represented as null
    public static final Type VOID = null; 

    //nil type represented as a list with null element
    public static final Type NIL = new ListType(null); 

    //equals method to compare type compatibility
    @Override
    public boolean equals(Object o) {
        
        if (this.getClass() == o.getClass()) {
            //compare array types by element
            if (this instanceof ArrayType) {
                return ((ArrayType) this).elementType.equals(((ArrayType) o).elementType);
            }
            //compare list types by element
            if (this instanceof ListType) {
                Type thisElem = ((ListType) this).elementType;
                Type objElem = ((ListType) o).elementType;
                
                //handle nil-based equality
                if (thisElem == null || objElem == null) {
                    return true;
                }
                return thisElem.equals(objElem);
            }
            return true;
        }
        return false;
    }

    //abstract print method for tree visualization
    public abstract void print(int depth);

    //integer type node
    public static class IntType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "int");
        }
    }
    //boolean type node
    public static class BoolType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "bool");
        }
    }
    //character type node
    public static class CharType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "char");
        }
    }
    //array type node
    public static class ArrayType extends Type {
        public Type elementType;
        public ArrayType(Type elementType) { this.elementType = elementType; }
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "Array of:");
            elementType.print(depth + 1);
        }
    }
    //list type node
    public static class ListType extends Type {
        public Type elementType;
        public ListType(Type elementType) { this.elementType = elementType; }
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "List of:");
            elementType.print(depth + 1);
        }
    }

}