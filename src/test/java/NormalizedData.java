import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;


public class NormalizedData {
    private static JSONArray getListOfPairs() {
        try {
            FileInputStream fis = new FileInputStream("generated_3_6_normal.json.gz");
            try {
                GZIPInputStream gis = new GZIPInputStream(fis);
                try {
                    String s = IOUtils.toString(gis, StandardCharsets.UTF_8);
                    return new JSONArray(s);
                } finally {
                    gis.close();
                }
            } finally {
                fis.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private static List<Exp[]> cachedListOfExpPairs = null;
    public static List<Exp[]> getListOfExpPairs() {
        if (cachedListOfExpPairs != null) {
            return cachedListOfExpPairs;
        }

        JSONArray l = getListOfPairs();
        ArrayList<Exp[]> result = new ArrayList<Exp[]>(l.length());
        for (int i = 0; i < l.length(); i++) {
            JSONArray jsonPair = l.getJSONArray(i);
            Exp[] pair = new Exp[2];
            pair[0] = Parser.jsonToExp(jsonPair.getJSONArray(0));
            pair[1] = Parser.jsonToExp(jsonPair.getJSONArray(1));
            result.add(pair);
        }
        cachedListOfExpPairs = result;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getListOfPairs().length());
        System.out.println(getListOfExpPairs().size());
    }
}
