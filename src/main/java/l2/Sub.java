package l2;

import java.util.Set;

import l2.AppFinder.Lam;
import l2.AppFinder.Node;

public class Sub {
    public static Node sub(Lam lam, Node arg) {
        Set<String> freeVars = FreeVarFinder.find(arg);
        return sub(lam, arg, freeVars);
    }
    private static Node sub(Lam lam, Node arg, Set<String> freeVars) {
        return null;
    }
}
