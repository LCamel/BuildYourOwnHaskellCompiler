
public class DbNatInt extends DbNat {
    public final int i;
    public DbNatInt(int i) {
        this.i = i;
    }
    @Override
    public String toString() {
        return "DbNatInt[" + i + "]";
    }
}
