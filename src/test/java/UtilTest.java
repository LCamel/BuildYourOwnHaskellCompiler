import org.testng.Assert;
import org.testng.annotations.Test;


public class UtilTest {
    @Test
    public void testParseString() {
        Exp exp = Util.parse("(λ_1 (λ_0 _0))");
        String actual = exp.toJSONArray().toString();
        String expected = "[\"lam\",\"_1\",[\"lam\",\"_0\",[\"var\",\"_0\"]]]";
        Assert.assertEquals(actual, expected);
    }
}
