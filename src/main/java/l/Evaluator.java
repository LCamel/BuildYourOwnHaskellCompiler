package l;

// 因為還想不清楚, 所以先用實作代替 spec
public class Evaluator {

    public interface App {
        Object getLeft();
        Object getRight();
        App gen(Object left, Object right); // may return the same object
    }
    public interface Lam {
        Object apply(Object arg);
        Object getBody();
        Lam gen(Object body);
    }

    public interface Var {
    }

    // 問題: algorithm 和 node 互相糾纏, 是否有個好的切點呢?
    // eval 裡面往下走再回來時 需要生成 node
    // 要讓 node 自己可以生成自己嗎? (在沒有 change 的狀況下, 至少只會生和自己一樣的 node 出來)


    // only for returning two values
    private static class NodeAndChanged {
        public final Object node;
        public final boolean changed;
        public NodeAndChanged(Object node, boolean changed) {
            this.node = node;
            this.changed = changed;
        }
    }

    public Object eval(Object node) {
        while (true) {
            NodeAndChanged tmp = evalOnce(node);
            if (tmp.changed == true) {
                node = tmp.node;
            } else {
                return tmp.node;
            }
        }
    }
    public NodeAndChanged evalOnce(Object node) {
        // apply
        if (node instanceof App) {
            App app = (App) node;
            if (app.getLeft() instanceof Lam) {
                Lam lam = (Lam) app.getLeft();
                return new NodeAndChanged(lam.apply(app.getRight()), true);
            }
        }

        // recursion
        if (node instanceof App) {
            App app = (App) node;
            NodeAndChanged tmp1 = evalOnce(app.getLeft());
            if (tmp1.changed == true) {
                return new NodeAndChanged(app.gen(tmp1.node, app.getRight()), true);
            } else {
                NodeAndChanged tmp2 = evalOnce(app.getRight());
                return new NodeAndChanged(app.gen(tmp1.node, tmp2.node), tmp2.changed);
            }
        }
        if (node instanceof Lam) {
            Lam lam = (Lam) node;
            NodeAndChanged tmp1 = evalOnce(lam.getBody());
            return new NodeAndChanged(lam.gen(tmp1.node), tmp1.changed);
        }
        if (node instanceof Var) {
            return new NodeAndChanged(node, false);
        }

        throw new RuntimeException("no such case");
    }
}
