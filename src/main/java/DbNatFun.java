
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
        args[argIdx] = arg;
        argIdx++;
        if (argIdx == args.length) {
            return eval.eval(args);
        } else {
            return this;
        }
    }
}
