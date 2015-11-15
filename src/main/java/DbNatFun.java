
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
    public static void main(String[] a) {
        DbNatFun fun = new DbNatFun(2,  "a", (args) ->
            new DbNatInt(((DbNatInt) (args[0])).i + ((DbNatInt) (args[1])).i));
        DbExp f1 = fun.passArg(new DbNatInt(3));
        DbExp f2 = ((DbNatFun) f1).passArg(new DbNatInt(5));
        System.out.println(((DbNatInt) f2).i);
    }
}
