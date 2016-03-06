package l2;


// TODO: 這邊假設了 mutable 的作法, 要求有點多
// 不過先這樣
// TODO: 會有人光用 finder 而不用 replacer 嗎?
// 分成兩個 interface 是讓 code 變好懂還是變難懂呢?
//
public interface AppReplacer extends AppFinder {
    public interface App extends AppFinder.App {
        void setLeft(Node node);  // mutable
        void setRight(Node node); // mutable
        default Node apply() { return ((Lam) getLeft()).apply(getRight()); }
    }
    public interface Lam extends AppFinder.Lam {
        void setBody(Node node);  // mutable
        Node apply(Node arg);
    }
    public interface Var extends AppFinder.Var {
    }

    public void replace(Node node);
    public Node getRoot();        // root might be replaced
}
