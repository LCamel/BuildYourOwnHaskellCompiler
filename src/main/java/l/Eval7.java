package l;

import java.util.LinkedList;


public class Eval7 {
    public interface Node {
    }
    private interface App extends Node {
        Node getLeft();
        Node getRight();
        void setLeft(Node node); // mutable
        void setRight(Node node); // mutable
        default Node apply() { return ((Lam) getLeft()).apply(getRight()); }
    }
    private interface Lam extends Node {
        Node getBody();
        void setBody(Node node); // mutable
        Node apply(Node arg);
    }
    private interface Var extends Node {
    }

    public interface Finder {
        public void init(Node root);
        public boolean find();
        public App getApp();
        public void replace(Node node);
    }

    private static class LeftMostFinder implements Finder {
        private LinkedList<Node> path;
        private LinkedList<Boolean> isAppLeft;

        @Override
        public void init(Node root) {
            path.push(root);
            isAppLeft.push(false);
        }

        @Override
        public App getApp() {
            return (App) path.peek();
        }

        @Override
        public void replace(Node node) {
            path.pop();
            path.push(node);
            Node parent = path.peek();
            if (parent instanceof Lam) {
                ((Lam) parent).setBody(node);
            } else if (parent instanceof App) {
                if (isAppLeft.peek()) {
                    ((App) parent).setLeft(node);
                } else {
                    ((App) parent).setRight(node);
                }
            } else {
                throw new RuntimeException("replace: how come? " + parent);
            }
        }

        @Override
        public boolean find() {
    //        if (path.isEmpty()) {
    //            return false;
    //        }
            while (true) {
                Node curr = path.peek();
                if (curr instanceof Lam) {
                    if (Boolean.TRUE.equals(isAppLeft.peek()) && path.get(1) instanceof App) {
                        path.pop();
                        isAppLeft.pop();
                        return true;
                    }
                    path.push(((Lam) curr).getBody());
                    isAppLeft.push(false);
                } else if (curr instanceof App) {
                    path.push(((App) curr).getLeft());
                    isAppLeft.push(true);
                } else if (curr instanceof Var) {
                    while (true) {
                        path.pop();
                        if (isAppLeft.pop() == true) { // the only one case who has next sibling
                            isAppLeft.push(false);
                            path.push(((App) path.peek()).getRight());
                            break;
                        }

                        if (path.isEmpty()) {
                            return false;
                        }
                    }
                } else {
                    throw new RuntimeException("strange: curr: " + curr);
                }
            }

        }

    }
    public static void eval(Node root) {
        Finder f = new LeftMostFinder();
        while (f.find()) {
            f.replace(f.getApp().apply());
        }
    }
}
