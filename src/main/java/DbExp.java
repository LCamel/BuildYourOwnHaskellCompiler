import java.util.ArrayList;
import java.util.List;


public interface DbExp {
    static DbExp fromExp(Exp exp, List<String> binds) {
        if (exp instanceof App) {
            App e = (App) exp;
            return new DbApp(fromExp(e.left, binds), fromExp(e.right, binds));
        }
        if (exp instanceof Lam) {
            Lam e = (Lam) exp;
            List<String> newBinds = new ArrayList<String>(binds);
            newBinds.add(e.var);
            return new DbLam(fromExp(e.body, newBinds));
        }
        if (exp instanceof Var) {
            Var e = (Var) exp;
            int i = 1;
            while (true) {
                if ((binds.get(binds.size() - i)).equals(e.name)) {
                    return new DbVar(i);
                }
                i++;
            }
        }
        throw new RuntimeException("Why?");
    }
    static DbExp fromExp(Exp exp) {
        return fromExp(exp, new ArrayList<String>());
    }

    static Exp toExp(DbExp dbExp, List<String> binds, int[] nextVar) {
        if (dbExp instanceof DbApp) {
            DbApp e = (DbApp) dbExp;
            return new App(toExp(e.left, binds, nextVar), toExp(e.right, binds, nextVar));
        }
        if (dbExp instanceof DbLam) {
            DbLam e = (DbLam) dbExp;
            String newVar = "_" + nextVar[0]++;
            List<String> newBinds = new ArrayList<String>(binds);
            newBinds.add(newVar);
            return new Lam(newVar, toExp(e.body, newBinds, nextVar));
        }
        if (dbExp instanceof DbVar) {
            DbVar e = (DbVar) dbExp;
            return new Var(binds.get(binds.size() - e.i));
        }
        throw new RuntimeException("Why?");
    }
    static Exp toExp(DbExp dbExp) {
        return toExp(dbExp, new ArrayList<String>(), new int[] { 0 });
    }
}
