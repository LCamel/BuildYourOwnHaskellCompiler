package l3;

import java.util.Arrays;
import java.util.LinkedList;

import l3.Nodes.Factory;
import l3.Nodes.Node;

public class Parser {
    public static LinkedList<String> getTokens(String line) {
        String[] tmp = line.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ").trim().split("\\s+");
        return new LinkedList<String>(Arrays.asList(tmp));
    }

    public static Node parseOne(LinkedList<String> tokens, Factory factory) {
        String token = tokens.removeFirst();
        switch (token) {
        case "(":
            Node tmp = null;
            switch (tokens.getFirst().charAt(0)) {
            case '\\':
            case '/':
            case 'λ':
                tmp = factory.newLam(tokens.removeFirst().substring(1), parseOne(tokens, factory));
                break;
            default:
                tmp = factory.newApp(parseOne(tokens, factory), parseOne(tokens, factory));
            }
            if (tokens.removeFirst().equals(")") == false) {
                throw new RuntimeException("no matching ')'");
            }
            return tmp;
        case ")":
            throw new RuntimeException("bad ')'");
        default:
            return factory.newVar(token);
        }
    }

}
