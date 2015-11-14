import java.util.Arrays;

import org.json.JSONArray;


public class Lam implements Exp {
    public final String var;
    public final Exp body;
    public Lam(String var, Exp body) { this.var = var; this.body = body; }

    @Override
    public JSONArray toJSONArray() {
        return new JSONArray(Arrays.asList("lam", var, body.toJSONArray()));
    }
}
