package l3;
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
        public Node apply(Node node) {
            return this;
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
}
