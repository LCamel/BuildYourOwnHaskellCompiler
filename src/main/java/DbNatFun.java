
public class DbNatFun extends DbNat {
    @FunctionalInterface
    public interface Eval {
        public DbExp eval(DbExp args[]);
    }
    private final DbExp[] args;
    private int argIdx = 0;
    private final String name;
    private final Eval eval;
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
            return eval.eval(args);
        } else {
            return this;
        }
    }
    @Override
    public String toString() {
        return "DbNatFun[" + name + "]";
    }
}
