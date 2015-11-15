
public class DbLam implements DbExp {
    public final DbExp body;
    public DbLam(DbExp body) {
        this.body = body;
    }
    @Override
    public String toString() {
        return "(λ " + body + ")";
    }
}
