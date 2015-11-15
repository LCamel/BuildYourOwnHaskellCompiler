
public class DbVar implements DbExp {
    public final int i;
    public DbVar(int i) { this.i = i; }
    @Override
    public String toString() {
        return "DbVar[" + i + "]";
    }
}
