
public class DbApp implements DbExp {
    public final DbExp left;
    public final DbExp right;
    public DbApp(DbExp left, DbExp right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public String toString() {
        return "(" + left + " " + right + ")";
    }
}
