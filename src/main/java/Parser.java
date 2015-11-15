import org.json.JSONArray;


public class Parser {
    public static Exp jsonToExp(JSONArray a) {
        switch (a.getString(0)) {
        case "var":
            return new Var(a.getString(1));
        case "app":
            return new App(jsonToExp(a.getJSONArray(1)), jsonToExp(a.getJSONArray(2)));
        default:
            return new Lam(a.getString(1), jsonToExp(a.getJSONArray(2)));
        }
    }
}
