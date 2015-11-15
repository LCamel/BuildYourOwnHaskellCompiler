import org.testng.Assert;
import org.testng.annotations.Test;


public class DbNatFunTest {
    @Test
    public void test_passArg() {
        DbNatFun fun = new DbNatFun(2, "add", (args) ->
            new DbNatInt(((DbNatInt) (args[0])).i + ((DbNatInt) (args[1])).i));
        DbExp f1 = fun.passArg(new DbNatInt(6));
        DbExp f2 = ((DbNatFun) f1).passArg(new DbNatInt(4));
        Assert.assertEquals(((DbNatInt) f2).i, 10);
    }

    @Test
    public void testMkPairFst() {
        DbExp dbExp = DbExp.fromExp(Util.parse("(((mkPair 6) 4) fst)"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("((mkPair 6) 4)"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("(mkPair 6)"));
        DbNatInt dbExp2 = (DbNatInt) DbReduce.leftMost(dbExp);
        //System.out.println("dbExp2: " + dbExp2);
        Assert.assertEquals(dbExp2.i, 6);
    }
    @Test
    public void testMkPairSnd() {
        DbExp dbExp = DbExp.fromExp(Util.parse("(((mkPair 6) 4) snd)"));
        DbNatInt dbExp2 = (DbNatInt) DbReduce.leftMost(dbExp);
        Assert.assertEquals(dbExp2.i, 4);
    }

}
