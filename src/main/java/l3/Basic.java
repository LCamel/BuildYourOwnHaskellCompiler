package l3;
import java.util.HashSet;
import java.util.Set;

import l3.Nodes.Node;

import org.json.JSONArray;

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
            Node result = sub(this, arg);
            //System.out.println("==== apply");
            //System.out.println("    " + this);
            //System.out.println("    " + result);
            return result;
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
                    // tradeoff between speed and readability
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

    // 限制只有 free var 可以換掉
    // 而且只有在一開始的時候一起換掉
    public static interface ReplaceFreeVar {
        public Node replace(Var var);
    }
    public static Node scanFreeVar(Node node, ReplaceFreeVar r) {
        return scanFreeVar(node, r, new HashSet<String>());
    }
    private static Node scanFreeVar(Node node, ReplaceFreeVar r, Set<String> params) {
        if (node instanceof App) {
            App app = (App) node;
            return new App(scanFreeVar(app.getLeft(), r, params), scanFreeVar(app.getRight(), r, params));
        } else if (node instanceof Lam) {
            Lam lam = (Lam) node;
            HashSet<String> newParams = new HashSet<>(params); // TODO: efficiency
            newParams.add(lam.getParam());
            return new Lam(lam.getParam(), scanFreeVar(lam.getBody(), r, newParams));
        } else if (node instanceof Var) {
            Var var = (Var) node;
            if (params.contains(var.getName())) { // bounded
                return node;
            } else { // free
                return r.replace(var);
            }
        } else {
            throw new RuntimeException("??");
        }
    }

    public static class NativeInt implements Nodes.ReadName.Var, Nodes.Apply.Var {
        private int i;
        public NativeInt(int i) { this.i = i; }
        @Override
        public String getName() {
            return null;
        }
        @Override
        public String toString() {
            return "#" + i;
        }
        public int getInt() {
            return i;
        }
    }

    public static Node replaceNative(Node node) {
        //return scanFreeVar(node, var -> new NativeInt(Integer.parseInt(var.getName())));
        //return scanFreeVar(node, var -> { return new NativeInt(Integer.parseInt(var.getName())); } );
        return scanFreeVar(node, new ReplaceFreeVar() {
            @Override
            public Node replace(Var var) {
                String name = var.getName();
                try {
                    return new NativeInt(Integer.parseInt(name));
                } catch (Exception e) {
                    if (name.equals("+")) {
                        return new Nodes.Apply.Lam() {
                            @Override
                            public Node apply(final Node arg0) {
                                return new Nodes.Apply.Lam() {
                                    @Override
                                    public Node apply(final Node arg1) {
                                        int i0 = ((NativeInt) arg0).getInt();
                                        int i1 = ((NativeInt) arg1).getInt();
                                        return new NativeInt(i0 + i1);
                                    }
                                };

                            }
                        };
                    } else {
                        throw new RuntimeException("unknown free variable: " + name);
                    }
                }
            }
        });
    }

    // ===================
    public static Node convertFromJson(String s) {
        return convertFromJson(new JSONArray(s));
    }
    private static Node convertFromJson(JSONArray a) {
        switch (a.getString(0)) {
        case "var":
            return new Var(a.getString(1));
        case "app":
            return new App(convertFromJson(a.getJSONArray(1)), convertFromJson(a.getJSONArray(2)));
        case "lam":
            return new Lam(a.getString(1), convertFromJson(a.getJSONArray(2)));
        default:
            throw new RuntimeException("???");
        }
    }

    public static void main(String[] args) {
        if (false) {
            Lam lam = (Lam) Basic.parse("( λx (λy x)) )");
            Node arg = Basic.parse("y");
            Node result = sub(lam, arg);
            System.out.println("result: " + result);
        }

        if (true) {
            Node node = Basic.parse("((+ 3) 5)");
            Node withNative = replaceNative(node);
            System.out.println("withNative: " + withNative);
        }
    }
}
