import java.util.Arrays;

import org.json.JSONArray;


public class Var implements Exp {
    public final String name;
    public Var(String name) { this.name = name; }
    @Override
    public JSONArray toJSONArray() {
        return new JSONArray(Arrays.asList("var", name));
    }
}
