package l;


import java.util.Arrays;
import java.util.LinkedList;


public class Eval8_MatchAtApp {
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
        public Node getRoot(); // for final result / debugging. Since root may be replaced
    }

    private static class LeftMostFinder implements Finder {
        private LinkedList<Node> path = new LinkedList<>();
        private LinkedList<Boolean> isAppLeft = new LinkedList<>();
        private Node root;

        @Override
        public void init(Node root) {
            this.root = root;
            path.push(root);
            isAppLeft.push(false);
        }
        @Override
        public Node getRoot() {
            return root;
        }

        @Override
        public App getApp() {
            return (App) path.peek();
        }

        @Override
        public void replace(Node node) {
            path.pop();

            Node parent = path.peek();
            if (parent instanceof Lam) {
                ((Lam) parent).setBody(node);
            } else if (parent instanceof App) {
                if (isAppLeft.peek()) {
                    ((App) parent).setLeft(node);
                } else {
                    ((App) parent).setRight(node);
                }
            } else if (parent == null) {
                // this is the top
                root = node;
            } else {
                throw new RuntimeException("replace: how come? " + parent);
            }

            path.push(node);
        }

        @Override
        public boolean find() {
            System.out.println("find: path: " + path);
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
        f.init(root);
        while (f.find()) {
            f.replace(f.getApp().apply());
        }
    }

    public static class AppImpl implements App {
        private Node left;
        private Node right;
        public AppImpl(Node left, Node right) {
            this.left = left;
            this.right = right;
        }
        @Override
        public Node getLeft() {
            return left;
        }
        @Override
        public void setLeft(Node node) {
            left = node;
        }
        @Override
        public Node getRight() {
            return right;
        }
        @Override
        public void setRight(Node node) {
            right = node;
        }
        // tmp
        @Override
        public String toString() {
            return "( " + left + "  " + right + " )";
        }
    }
    public static class LamImpl implements Lam {
        private Node body;
        private String param;
        public LamImpl(String param, Node body) {
            this.param = param;
            this.body = body;
        }
        @Override
        public Node getBody() {
            return body;
        }
        @Override
        public void setBody(Node node) {
            body = node;
        }
        @Override
        public Node apply(Node arg) {
            throw new RuntimeException("not implemented");
        }
        // tmp
        @Override
        public String toString() {
            return "(λ" + param + " " + body + ")";
        }
    }
    public static class VarImpl implements Var {
        private String name;
        public VarImpl(String name) {
            this.name = name;
        }
        // tmp
        @Override
        public String toString() {
            return name;
        }
    }









    private static LinkedList<String> getTokens(String line) {
        String[] tmp = line.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").trim().split("\\s+");
        return new LinkedList<String>(Arrays.asList(tmp));
    }
    private static Node parseOne(LinkedList<String> tokens) {
        // System.out.println("====");
        // System.out.println("tokens: " + tokens);
        String token = tokens.removeFirst();
        // System.out.println("tokens: " + tokens);
        switch (token) {
        case "(":
            Node tmp = null;
            switch (tokens.getFirst().charAt(0)) {
            case '\\':
            case '/':
            case 'λ':
                tmp = new LamImpl(tokens.removeFirst().substring(1), parseOne(tokens));
                break;
            default:
                tmp = new AppImpl(parseOne(tokens), parseOne(tokens));
            }
            if (tokens.removeFirst().equals(")") == false) {
                throw new RuntimeException("no matching ')'");
            }
            return tmp;
        case ")":
            throw new RuntimeException("bad ')'");
        default:
            return new VarImpl(token);
        }
    }














    public static void main(String[] args) {
        String line = "(( (λz z) x ) ( (λz z) y ))";
        Node root = parseOne(getTokens(line));
        System.out.println(root);

        Finder f = new LeftMostFinder();
        f.init(root);
        int i = 0;
        while (true) {
            System.out.println("-------");
            System.out.println(f.getRoot());
            //System.out.println(root); // <-- not corret
            if (f.find()) {
                System.out.println("found! => " + f.getApp());
                f.replace(parseOne(getTokens("(λp (λq r))")));
            } else {
                System.out.println("not found!");
                break;
            }
            i++;
            if (i > 2) break;
        }
    }
}
