package l;

import java.util.Stack;


public class Eval6 {
    public interface Node {
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
    private Stack<Boolean> isLeft;
    private Node curr;

    public boolean find() {
//        if (path.isEmpty()) {
//            return false;
//        }
        while (true) {
            if (curr instanceof Lam) {
                if (Boolean.TRUE.equals(isLeft.peek()) && path.peek() instanceof App) {
                    return true;
                } else {
                    path.push(curr);
                    isLeft.push(false);
                    curr = ((Lam) curr).getBody();
                }
            } else if (curr instanceof App) {
                path.push(curr);
                isLeft.push(true);
                curr = ((App) curr).getLeft();
            } else if (curr instanceof Var) {
                while (true) {
                    if (isLeft.isEmpty()) {
                        return false;
                    }
                    if (isLeft.pop() == true) {
                        isLeft.push(false);
                        curr = ((App) path.peek()).getRight();
                        break;
                    } else {
                        path.pop();
                    }
                }
            }
        }


    }
}
