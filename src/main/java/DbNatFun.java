
public class DbNatFun extends DbNat {
    @FunctionalInterface
    public interface Eval {
        public DbExp eval(DbExp args[]);
    }
    private final DbExp[] args;
    private int argIdx = 0;
    private final String name;
    private final Eval eval;
    private DbExp result = null;
    public DbNatFun(int numArgs, String name, Eval eval) {
        args = new DbExp[numArgs];
        this.name = name;
        this.eval = eval;
    }
    public DbExp passArg(DbExp arg) {
        System.out.println("passArg[" + name + " " + System.identityHashCode(this) + "](" + (argIdx + 1) + "/" + args.length + "): " + arg);
        args[argIdx] = arg;
        argIdx++;
        if (argIdx == args.length) {
            // TODO: think
            // the only chance that a single DbNatFun object being used multiple times
            // should be the "argument" case
            if (result == null) {
                result = eval.eval(args);
            }
            argIdx = 0;
            return result;
        } else {
            return this;
        }
    }
    @Override
    public String toString() {
        return "DbNatFun[" + name + "]";
    }
}
