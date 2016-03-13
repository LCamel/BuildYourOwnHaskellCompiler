package l2;

import java.util.HashSet;
import java.util.Set;

import l2.AppFinder.Node;


public class FreeVarFinder {
    public interface App extends AppFinder.App {
    }
    public interface Lam extends AppFinder.Lam {
        String getParam();
    }
    public interface Var extends AppFinder.Var {
        String getName();
    }

    public static Set<String> find(Node node) {
        if (node instanceof App) {
            Set<String> s1 = find(((App) node).getLeft());
            Set<String> s2 = find(((App) node).getRight());
            s1.addAll(s2);
            return s1;
        } else if (node instanceof Lam) {
            Set<String> s1 = find(((Lam) node).getBody());
            s1.remove(((Lam) node).getParam());
            return s1;
        } else if (node instanceof Var) {
            Set<String> s1 = new HashSet<String>();
            s1.add(((Var) node).getName());
            return s1;
        } else {
            throw new RuntimeException("what ? " + node);
        }
    }

}
