package gr.hua.dit.compilers.runtime;

public class TonyList {
    public Object head;
    public TonyList tail;

    public TonyList(Object head, TonyList tail) {
        this.head = head;
        this.tail = tail;
    }

    public static TonyList cons(Object head, TonyList tail) {
        return new TonyList(head, tail);
    }

    public static boolean isNil(TonyList list) {
        return list == null;
    }

    public static Object head(TonyList list) {
        if (list == null) throw new RuntimeException("head of empty list");
        return list.head;
    }

    public static TonyList tail(TonyList list) {
        if (list == null) throw new RuntimeException("tail of empty list");
        return list.tail;
    }
}
