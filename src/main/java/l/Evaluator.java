package l;

public class Evaluator {

    public interface App {
        Object getLeft();
        Object getRight();
        App gen(Object left, Object right);
    }
//    public static class AppNode implements App {
//        public final Object left;
//        public final Object right;
//        public AppNode(Object l, Object r) { left = l; right = r; }
//        @Override
//        public Object getLeft() { return left; }
//        @Override
//        public Object getRight() { return right; }
//    }

    public interface Lam {
        Object apply(Object arg);
        Object getBody();
        Lam gen(Object body);
    }
//    public static class LamNode implements Lam {
//
//        @Override
//        public Object apply(Object arg) {
//            return null;
//        }
//
//        @Override
//        public Object getBody() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    }
    public interface Var {
    }

    // 問題: algorithm 和 node 互相糾纏, 是否有個好的切點呢?
    // eval 裡面往下走再回來時 需要生成 node
    // 要讓 node 自己可以生成自己嗎? (在沒有 change 的狀況下, 至少只會生和自己一樣的 node 出來)


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
        if (node instanceof App) {
            App app = (App) node;
            if (app.getLeft() instanceof Lam) {
                Lam lam = (Lam) app.getLeft();
                return new NodeAndChanged(lam.apply(app.getRight()), true);
            }
        }

        if (node instanceof App) {
            App app = (App) node;
            NodeAndChanged tmp1 = evalOnce(app.getLeft());
            if (tmp1.changed == true) {
                //return new NodeAndChanged(new AppNode(tmp1.node, app.getRight()), true);
                return new NodeAndChanged(app.gen(tmp1.node, app.getRight()), true);
            } else {
                NodeAndChanged tmp2 = evalOnce(app.getRight());
                //return new NodeAndChanged(new AppNode(tmp1.node, tmp2.node), tmp2.changed);
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
