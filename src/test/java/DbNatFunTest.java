import org.testng.Assert;
import org.testng.annotations.Test;


public class DbNatFunTest {
    @Test
    public void test_passArg() {
        DbNatFun fun = new DbNatFun(2, "add", (args) ->
            new DbNatInt(((DbNatInt) (args[0])).i + ((DbNatInt) (args[1])).i));
        DbExp f1 = fun.passArg(new DbNatInt(6));
        DbExp f2 = ((DbNatFun) f1).passArg(new DbNatInt(4));
        Assert.assertEquals(10, ((DbNatInt) f2).i);
    }

}
