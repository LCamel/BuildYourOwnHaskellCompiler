import java.util.Arrays;

import org.json.JSONArray;


public class App implements Exp {
    public final Exp left;
    public final Exp right;
    public App(Exp left, Exp right) { this.left = left; this.right = right; }
    @Override
    public JSONArray toJSONArray() {
        return new JSONArray(Arrays.asList("app", left.toJSONArray(), right.toJSONArray()));
    }
}
