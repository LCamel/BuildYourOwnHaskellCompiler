import java.io.File;

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

    @Test
    public void testParseMultiLine() {
        String lamExpString = "# false\n" +
                "(λ_1 \n" +
                "   (λ_0 _0) # id λfoo λbar\n" +
                ")";
        Exp exp = Util.parse(lamExpString);
        String actual = exp.toJSONArray().toString();
        String expected = "[\"lam\",\"_1\",[\"lam\",\"_0\",[\"var\",\"_0\"]]]";
        Assert.assertEquals(actual, expected);
    }
    @Test
    public void testParseFile() {
        Exp exp = Util.parseFile(new File("src/test/resources/multiline.ulc"));
        String actual = exp.toJSONArray().toString();
        String expected = "[\"app\",[\"lam\",\"id\",[\"app\",[\"var\",\"id\"],[\"var\",\"id\"]]],[\"lam\",\"x\",[\"var\",\"x\"]]]";
        Assert.assertEquals(actual, expected);
    }
}
