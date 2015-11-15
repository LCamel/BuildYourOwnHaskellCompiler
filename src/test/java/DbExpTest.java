import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;


public class DbExpTest {
    @Test
    public void testConvertDbExp() {
        List<Exp[]> listOfExpPairs = NormalizedData.getListOfExpPairs();
        for (Exp[] pair : listOfExpPairs) {
            Exp orig = pair[0];
            Exp toFromDb = DbExp.toExp(DbExp.fromExp(orig));
            //System.out.println("toFromDb: " + toFromDb.toJSONArray());

            // we have an assumption here:
            // the "orig" were already being "renamed"
            // and the naming rule is just the same with to/from DbExp
            String s1 = orig.toJSONArray().toString();
            String s2 = toFromDb.toJSONArray().toString();
            Assert.assertEquals(s2, s1);
        }
    }
    @Test
    public void testNat() {
        Exp exp = Util.parse("( ( +   ( (λy y) 6) )   ( (λy y) 4)    )");
        DbExp dbExp = DbExp.fromExp(exp);
        DbExp result = DbReduce.leftMost(dbExp);
        int i = ((DbNatInt) result).i;
        Assert.assertEquals(i, 10);
    }
}
