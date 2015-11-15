import java.util.Arrays;
import java.util.LinkedList;

import org.json.JSONArray;



public class Util {
    private static class Env {
        public final Env outer;
        public final String k;
        public final String v;
        public Env(Env outer, String k, String v) {
            this.outer = outer;
            this.k = k;
            this.v = v;
        }
        public String get(String key) {
            return key.equals(k) ? v : outer.get(key);
        }
    }
    private static class NextVar {
        private int nextVar;
        public NextVar() { this.nextVar = 0; }
        public String next() { return "_" + (this.nextVar++); }
    }
    private static Exp rename(Exp exp, Env env, NextVar nextVar) {
        if (exp instanceof Var) {
            Var var = (Var) exp;
            return new Var(env.get(var.name));
        }
        if (exp instanceof App) {
            App app = (App) exp;
            return new App(rename(app.left, env, nextVar), rename(app.right, env, nextVar));
        }
        if (exp instanceof Lam) {
            Lam lam = (Lam) exp;
            String v = nextVar.next();
            return new Lam(v, rename(lam.body, new Env(env, lam.var, v), nextVar));
        }
        throw new RuntimeException("unknown exp: " + exp);
    }
    public static Exp rename(Exp exp) {
        return rename(exp, null, new NextVar());
    }

    private static LinkedList<String> getTokens(String line) {
        String[] tmp = line.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").trim().split("\\s+");
        return new LinkedList<String>(Arrays.asList(tmp));
    }
    private static Exp parseOne(LinkedList<String> tokens) {
        // System.out.println("====");
        // System.out.println("tokens: " + tokens);
        String token = tokens.removeFirst();
        // System.out.println("tokens: " + tokens);
        switch (token) {
        case "(":
            Exp tmp = null;
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
    public static Exp parse(String lamExpString) {
        lamExpString = lamExpString.replaceAll("(#.*)?\\n", "");
        return parseOne(getTokens(lamExpString));
    }

    public static Exp jsonToExp(JSONArray a) {
        switch (a.getString(0)) {
        case "var":
            return new Var(a.getString(1));
        case "app":
            return new App(jsonToExp(a.getJSONArray(1)), jsonToExp(a.getJSONArray(2)));
        default:
            return new Lam(a.getString(1), jsonToExp(a.getJSONArray(2)));
        }
    }
}
