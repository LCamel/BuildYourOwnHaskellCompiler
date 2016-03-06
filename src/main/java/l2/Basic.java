package l2;

import java.util.Arrays;
import java.util.LinkedList;

import l2.AppFinder.Node;

public class Basic {
    public static class App implements AppReplacer.App {
        private Node left;
        private Node right;

        public App(Node left, Node right) {
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

    public static class Lam implements AppReplacer.Lam {
        private Node body;
        private String param;

        public Lam(String param, Node body) {
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

    public static class Var implements AppReplacer.Var {
        private String name;
        public Var(String name) {
            this.name = name;
        }

        // tmp
        @Override
        public String toString() {
            return name;
        }
    }

    public static LinkedList<String> getTokens(String line) {
        String[] tmp = line.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").trim().split("\\s+");
        return new LinkedList<String>(Arrays.asList(tmp));
    }

    // 或許可以把 new 的地方弄個 factory 進來, 可以生不一樣的
    public static Node parseOne(LinkedList<String> tokens) {
        String token = tokens.removeFirst();
        switch (token) {
        case "(":
            Node tmp = null;
            switch (tokens.getFirst().charAt(0)) {
            case '\\':
            case '/':
            case 'λ':
                tmp = new Lam(tokens.removeFirst().substring(1), parseOne(tokens));
                break;
            default:
                tmp = new App(parseOne(tokens), parseOne(tokens));
            }
            if (tokens.removeFirst().equals(")") == false) {
                throw new RuntimeException("no matching ')'");
            }
            return tmp;
        case ")":
            throw new RuntimeException("bad ')'");
        default:
            return new Var(token);
        }
    }





}
