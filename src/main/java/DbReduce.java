
public class DbReduce {

    static DbExp beta(DbExp expA, DbExp expB) {
        return doA(expA, 1, expB);
    }

    static DbExp doA(DbExp expA, int aLamDepth, DbExp expB) {
        if (expA instanceof DbApp) {
            DbApp e = (DbApp) expA;
            return new DbApp(doA(e.left, aLamDepth, expB), doA(e.right, aLamDepth, expB));
        }
        if (expA instanceof DbLam) {
            DbLam e = (DbLam) expA;
            return new DbLam(doA(e.body, aLamDepth + 1, expB));
        }
        if (expA instanceof DbVar) {
            DbVar e = (DbVar) expA;
            if (e.i == aLamDepth) {
                return doB(expB, 0, aLamDepth);
            } else if (e.i < aLamDepth) {
                return e;
            } else {
                return new DbVar(e.i - 1);
            }
        }
        throw new RuntimeException("Why?");
    }

    static DbExp doB(DbExp expB, int bLamDepth, int aLamDepth) {
        if (expB instanceof DbApp) {
            DbApp e = (DbApp) expB;
            return new DbApp(doB(e.left, bLamDepth, aLamDepth), doB(e.right, bLamDepth, aLamDepth));
        }
        if (expB instanceof DbLam) {
            DbLam e = (DbLam) expB;
            return new DbLam(doB(e.body, bLamDepth + 1, aLamDepth));
        }
        if (expB instanceof DbVar) {
            DbVar e = (DbVar) expB;
            if (e.i > bLamDepth) {
                return new DbVar(e.i + aLamDepth - 1);
            } else {
                return e;
            }
        }
        throw new RuntimeException("Why?");
    }

    static DbExp leftMost(DbExp exp, boolean waitLam) {
        if (exp instanceof DbApp) {
            DbApp e = (DbApp) exp;
            DbExp newLeft = leftMost(e.left, true);
            if (newLeft instanceof DbLam) {
                DbLam lam = (DbLam) newLeft;
                return leftMost(beta(lam.body, e.right), waitLam);
            }
            return new DbApp(newLeft, leftMost(e.right, false));
        }
        if (exp instanceof DbLam) {
            DbLam e = (DbLam) exp;
            if (waitLam) return e;
            return new DbLam(leftMost(e.body, false));
        }
        if (exp instanceof DbVar) {
            return exp;
        }
        throw new RuntimeException("Why?");
    }

    static DbExp leftMost(DbExp exp) {
        return leftMost(exp, false);
    }

}
