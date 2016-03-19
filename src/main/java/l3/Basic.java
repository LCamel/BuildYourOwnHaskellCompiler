package l3;
import java.util.Set;

import l3.Nodes.Node;

public class Basic {
    public static class App implements Nodes.WriteName.App, Nodes.Apply.App {
        private Node left;
        private Node right;
        public App(Node left, Node right) { this.left = left; this.right = right; }
        @Override
        public Node getLeft() { return left; }
        @Override
        public Node getRight() { return right; }
        @Override
        public void setLeft(Node node) { left = node; }
        @Override
        public void setRight(Node node) { right = node; }
        @Override
        public String toString() {
            return "(" + left + " " + right + ")";
        }
    }
    public static class Lam implements Nodes.WriteName.Lam, Nodes.Apply.Lam {
        private String param;
        private Node body;
        public Lam(String param, Node body) { this.param = param; this.body = body; }
        @Override
        public String getParam() { return param; }
        @Override
        public Node getBody() { return body; }
        @Override
        public void setBody(Node node) { body = node; }
        @Override
        public void setParam(String param) { this.param = param; }
        @Override
        public String toString() {
            return "(λ" + param + " " + body + ")";
        }
        @Override
        public Node apply(Node arg) {
            return sub(this, arg);
        }
    }
    public static class Var implements Nodes.WriteName.Var, Nodes.Apply.Var {
        private String name;
        public Var(String name) { this.name = name; }
        @Override
        public String getName() { return name; }
        @Override
        public void setName(String name) { this.name = name; }
        @Override
        public String toString() {
            return name;
        }
    }

    public static final Nodes.Factory factory = new Nodes.Factory() {
        @Override
        public Nodes.Read.App newApp(Node left, Node right) {
            return new App(left, right);
        }
        @Override
        public Nodes.Read.Lam newLam(String param, Node body) {
            return new Lam(param, body);
        }
        @Override
        public Nodes.Read.Var newVar(String name) {
            return new Var(name);
        }
    };

    public static Nodes.Node parse(String line) {
        return Parser.parseOne(Parser.getTokens(line), factory);
    }

    public static Node sub(Lam lam, Node arg) {
        Set<String> freeVars = FreeVarFinder.find(arg);
        return sub(lam.getBody(), lam.getParam(), arg, freeVars);
    }

    private static int nextName = 0;
    private static String getNewName() {
        return "__x" + (nextName++);
    }
    private static Node sub(Node node, String param, Node arg, Set<String> freeVars) {
        if (node instanceof App) {
            App app = (App) node;
            return new App(sub(app.getLeft(), param, arg, freeVars), sub(app.getRight(), param, arg, freeVars));
        } else if (node instanceof Lam) {
            Lam lam = (Lam) node;
            if (lam.getParam().equals(param)) { // 碰到相同的了 不用往下
                return node;
            } else {
                boolean conflict = freeVars.contains(lam.getParam());
                if (conflict) {
                    String newName = getNewName();
                    Node newBody = changeName(lam.getBody(), lam.getParam(), newName);
                    return new Lam(newName, sub(newBody, param, arg, freeVars));
                } else {
                    return new Lam(lam.getParam(), sub(lam.getBody(), param, arg, freeVars));
                }
            }
        } else if (node instanceof Var) {
            Var var = (Var) node;
            if (var.getName().equals(param)) {
                return arg;
            } else {
                return node;
            }
        } else {
            throw new RuntimeException("why?");
        }
    }
    private static Node changeName(Node node, String oldName, String newName) {
        if (node instanceof App) {
            App app = (App) node;
            return new App(changeName(app.getLeft(), oldName, newName), changeName(app.getRight(), oldName, newName));
        } else if (node instanceof Lam) {
            Lam lam = (Lam) node;
            if (lam.getParam().equals(oldName)) {
                return node;
            } else {
                return new Lam(lam.getParam(), changeName(lam.getBody(), oldName, newName));
            }
        } else if (node instanceof Var) {
            Var var = (Var) node;
            if (var.getName().equals(oldName)) {
                return new Var(newName);
            } else {
                return node;
            }
        } else {
            throw new RuntimeException("why?");
        }
    }

    public static void main(String[] args) {
        Lam lam = (Lam) Basic.parse("( λx (λy x)) )");
        Node arg = Basic.parse("y");
        Node result = sub(lam, arg);
        System.out.println("result: " + result);
    }
}
