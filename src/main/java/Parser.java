import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;


public class Parser {
    public static Exp parse(JSONArray a) {
        switch (a.getString(0)) {
        case "var":
            return new Var(a.getString(1));
        case "app":
            return new App(parse(a.getJSONArray(1)), parse(a.getJSONArray(2)));
        default:
            return new Lam(a.getString(1), parse(a.getJSONArray(2)));
        }
    }
    public static JSONArray parseFileAsJSONArray(String path) throws IOException {
        return new JSONArray(new String(Files.readAllBytes(Paths.get(path))));
    }

}
