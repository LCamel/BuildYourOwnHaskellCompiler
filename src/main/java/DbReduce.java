
public class DbReduce {

    static DbExp beta(DbExp expA, DbExp expB) {
        System.out.println(">>>> beta: expA: " + expA + " expB: " + expB);
        DbExp tmp = doA(expA, 1, expB);
        System.out.println("<<<< beta: result: " + tmp);
        return tmp;
    }

    static DbExp doA(DbExp expA, int aLamDepth, DbExp expB) {
        //System.out.println("doA: expA: " + expA + " expB: " + expB + " aLamDepth: " + aLamDepth);
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
        if (expA instanceof DbNat) {
            return expA;
        }
        throw new RuntimeException("Why? expA: " + expA + " expB: " + expB);
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
        if (expB instanceof DbNat) {
            return expB;
        }
        throw new RuntimeException("Why?");
    }

    static DbExp leftMost(DbExp exp, boolean waitLam) {
        System.out.println("leftMost: " + exp);
        if (exp instanceof DbApp) {
            DbApp e = (DbApp) exp;
            DbExp newLeft = leftMost(e.left, true);
            if (newLeft instanceof DbLam) {
                DbLam lam = (DbLam) newLeft;
                return leftMost(beta(lam.body, e.right), waitLam);
            }
            if (newLeft instanceof DbNatFun) {
                DbNatFun fun = (DbNatFun) newLeft;
                return leftMost(fun.passArg(e.right), waitLam);
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
        if (exp instanceof DbNat) {
            return exp;
        }
        throw new RuntimeException("Why? " + exp);
    }

    static DbExp leftMost(DbExp exp) {
        return leftMost(exp, false);
    }

}
