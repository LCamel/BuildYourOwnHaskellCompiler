import java.io.IOException;

import org.json.JSONArray;


public class Test1 {
    public static void main(String args[]) throws IOException {
        JSONArray jsonArray = Parser.parseFileAsJSONArray("/Users/lcamel/vc/BuildYourOwnHaskellCompiler/generated_3_6_normal.json");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray pair = jsonArray.getJSONArray(i);
            Exp orig = Parser.parse(pair.getJSONArray(0));
            Exp normal = Parser.parse(pair.getJSONArray(1));

            //System.out.println("orig: " + orig.toJSONArray());

            if (false)
            {
                Exp toFromDb = DbExp.toExp(DbExp.fromExp(orig));
                System.out.println("toFromDb: " + toFromDb.toJSONArray());

                String s1 = orig.toJSONArray().toString();
                String s2 = toFromDb.toJSONArray().toString();
                if (s1.equals(s2) == false) {
                    throw new RuntimeException("die");
                }
            }


            {
                // test normalization
                Exp exp = DbExp.toExp(DbExp.leftMost(DbExp.fromExp(orig)));
                String s1 = normal.toJSONArray().toString();
                String s2 = exp.toJSONArray().toString();
                if (s1.equals(s2) == false) {
                    System.out.println("========");
                    System.out.println(s1);
                    System.out.println(s2);
                    throw new RuntimeException("die");
                }
            }
        }
        System.out.println("done");
    }
}
