import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;


public class DbReduceTest {
    @Test
    public void test_leftMost() {
        List<Exp[]> listOfExpPairs = NormalizedData.getListOfExpPairs();
        for (Exp[] pair : listOfExpPairs) {
            Exp orig = pair[0];
            Exp normal = pair[1];


            Exp exp = DbExp.toExp(DbReduce.leftMost(DbExp.fromExp(orig)));
            String s1 = normal.toJSONArray().toString();
            String s2 = exp.toJSONArray().toString();
            Assert.assertEquals(s2, s1);
        }
    }
}
