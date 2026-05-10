package gr.hua.dit.compilers.ast;

public abstract class Type {

    public abstract void print(int depth);

    public static class IntType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "int");
        }
    }
    public static class BoolType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "bool");
        }
    }
    public static class CharType extends Type {
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "char");
        }
    }
    public static class ArrayType extends Type {
        private Type elementType;
        public ArrayType(Type elementType) { this.elementType = elementType; }
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "Array of:");
            elementType.print(depth + 1);
        }
    }
    public static class ListType extends Type {
        private Type elementType;
        public ListType(Type elementType) { this.elementType = elementType; }
        public void print(int depth) {
            System.out.println("  ".repeat(depth) + "List of:");
            elementType.print(depth + 1);
        }
    }

} 
