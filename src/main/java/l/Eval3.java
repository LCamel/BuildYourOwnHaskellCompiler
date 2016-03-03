package l;

import java.util.List;

public class Eval3 {
    private interface Node {
        public List<Node> getChildren();
    }
    private interface App extends Node {
    }
    private interface Lam extends Node {
    }
    private interface Var extends Node {
    }

    public static Node findRecursive(Node node) {
        while (true) {
            if (node instanceof App && node.getChildren().get(0) instanceof Lam) {
                return node;
            }
            Node result = null;
            for (Node child : node.getChildren()) {
                result = findRecursive(child);
                if (result != null) return result;
            }
            return result;
        }
    }
    //    private Node root;
//    private LinkedList<Node> nodes;
//    private LinkedList<Boolean> waitLams;

}
