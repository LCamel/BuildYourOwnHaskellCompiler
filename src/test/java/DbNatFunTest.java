import java.io.File;

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

//    @Test
//    public void testMkPairFst() {
//        DbExp dbExp = DbExp.fromExp(Util.parse("(((mkPair 6) 4) fst)"));
//        //DbExp dbExp = DbExp.fromExp(Util.parse("((mkPair 6) 4)"));
//        //DbExp dbExp = DbExp.fromExp(Util.parse("(mkPair 6)"));
//        DbNatInt dbExp2 = (DbNatInt) DbReduce.leftMost(dbExp);
//        //System.out.println("dbExp2: " + dbExp2);
//        Assert.assertEquals(dbExp2.i, 6);
//    }
//    @Test
//    public void testMkPairSnd() {
//        DbExp dbExp = DbExp.fromExp(Util.parse("(((mkPair 6) 4) snd)"));
//        DbNatInt dbExp2 = (DbNatInt) DbReduce.leftMost(dbExp);
//        Assert.assertEquals(dbExp2.i, 4);
//    }

    @Test
    public void testPrintInt() {
        DbExp dbExp = DbExp.fromExp(Util.parse("(printInt 3)"));
        //System.out.println("dbExp: " + dbExp);
        DbNatFun printIntWithInt = (DbNatFun) DbReduce.leftMost(dbExp);
        //System.out.println("printIntWithInt: " + printIntWithInt);
        //DbExp pair = dbExp2.passArg(new DbNatWorld());
        DbExp pair = DbReduce.leftMost(new DbApp(printIntWithInt, new DbNatWorld()));
        //System.out.println("pair: " + pair);

        DbExp gotFst = DbReduce.leftMost(new DbApp(pair, DbExp.fromExp(Util.parse("(λx (λy x))"))));
        //System.out.println("gotFst: " + gotFst);
        // TODO: check the first is Unit

        // the snd should be a "world"
        DbNatWorld gotSnd = (DbNatWorld) DbReduce.leftMost(new DbApp(pair, DbExp.fromExp(Util.parse("(λx (λy y))"))));
        //System.out.println("gotSnd: " + gotSnd);
    }

    @Test
    public void testBind() {
        // "(λio_a (λf (λworld0       (   (λa_world1   ( (f (a_world1  (λx (λy x))  )) (a_world1 (λx (λy y))  ) )             )   (io_a world0)   )        )))"
        // (λx (+ 3) x)
        DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO (printInt 123))  (λx (printInt 456))  )"));

        //DbExp dbExp = DbExp.fromExp(Util.parse("(λx ((+ 3) x))"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("((λx ((+ 3) x)) 5)"));

        //DbExp result = DbReduce.leftMost(dbExp);
        //System.out.println("result: " + result);
        DbExp dbExp2 = new DbApp(dbExp, new DbNatWorld());
        DbExp dbExp3 = DbReduce.leftMost(dbExp2);
    }

    @Test
    public void testInputOutput() {
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)   (λx (printInt x))  )"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO (printInt 5))   (λx (printInt 33))  )"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)  (λx (printInt x))  )"));
        DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)  printInt  )"));
        DbExp dbExp2 = new DbApp(dbExp, new DbNatWorld());
        System.out.println("==== dbExp2: " + dbExp2);
        DbReduce.leftMost(dbExp2);
    }
    @Test
    public void testInputOutput2() {
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)   (λx (printInt x))  )"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO (printInt 5))   (λx (printInt 33))  )"));
        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)  (λx (printInt x))  )"));
        DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO (printInt 3))  (λx readInt ))"));
        DbExp dbExp2 = new DbApp(dbExp, new DbNatWorld());
        System.out.println("==== dbExp2: " + dbExp2);
        DbReduce.leftMost(dbExp2);
    }

//    @Test
//    public void testInputOutput22() {
//        //DbExp dbExp = DbExp.fromExp(Util.parse("(  (bindIO readInt)   (λx (printInt x))  )"));
//        DbExp dbExp = DbExp.fromExp(Util.parse("bindIO"));
//        System.out.println("dbExp2: " + dbExp);
//    }
    @Test
    public void testBindIO() {
        DbExp dbExp = DbExp.fromExp(Util.parseFile(new File("src/test/resources/BindIO.ulc")));
        System.out.println("dbExp: " + dbExp);
        DbExp dbExp2 = new DbApp(dbExp, new DbNatWorld());
        DbReduce.leftMost(dbExp2);
    }
}
