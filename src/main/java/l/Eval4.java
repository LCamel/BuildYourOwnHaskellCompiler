package l;

import java.util.LinkedList;

public class Eval4 {
    private interface Node {
        public Node[] getChildren();
    }
    private interface App extends Node {
    }
    private interface Lam extends Node {
    }
    private interface Var extends Node {
    }

    private LinkedList<Node> path;
    private LinkedList<Integer> indexes;

    public boolean find() {
        while (true) {
            if (path.isEmpty()) {
                return false;
            }
            if (path.get(0) instanceof Lam && indexes.size() > 0 && indexes.get(0) == 0 && path.get(1) instanceof App) {
                return true;
            }
            Node[] children = path.get(0).getChildren();
            if (children.length == 0) {
                pop();
            } else {
                indexes.offerFirst(0);
                path.offerFirst(children[0]);
            }
        }
    }
    private void pop() {
        while (true) {
            path.pollFirst();
            Node parent = path.peekFirst();
            if (parent == null) return;
            int index = indexes.pollFirst() + 1;
            Node[] children = parent.getChildren();
            if (index < children.length) {
                indexes.offerFirst(index);
                path.offerFirst(children[index]);
                return;
            }
        }
    }

}
