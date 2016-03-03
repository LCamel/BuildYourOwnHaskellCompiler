package l;

import java.util.Stack;

public class Eval5 {
    private interface Node {
    }
    private interface App extends Node {
        Node getLeft();
        Node getRight();
    }
    private interface Lam extends Node {
        Node getBody();
    }
    private interface Var extends Node {
    }

    private Stack<Node> path;
    private Stack<Boolean> waitLams;

    public boolean find() {
        while (true) {
            if (path.isEmpty()) {
                return false;
            }
            Node curr = path.peek();
            if (curr instanceof Lam) {
                if (waitLams.peek() == true) {
                    return true;
                } else {
                    path.push(((Lam) curr).getBody()); waitLams.push(false);
                }
            } else if (curr instanceof App) {
                path.push(((App) curr).getLeft()); waitLams.push(true);
            } else if (curr instanceof Var) {
                while (true) {
                    Node parent = path.pop(); waitLams.pop();
                    if (parent instanceof App) {
                        path.push(((App) curr).getRight()); waitLams.push(false);
                        break;
                    } else if (parent instanceof Lam) {
                        path.pop(); waitLams.pop();
                    }
                }
            } else {
                throw new RuntimeException("strange: " + curr);
            }
        }
    }

}
