package l3;

public class A {
    public static interface I1 {
        public int f1(int i);
    }
    public static interface I2 extends I1 {
        @Override
        public default int f1(int i) { return 1; }
        public int f2(int i);
    }

    public static void blah1(I1 i1) {
        System.out.println(i1.f1(10));
    }
    public static void blah2(I2 i2) {
        System.out.println(i2.f1(10));
        System.out.println(i2.f2(10));
    }
    public static I1 rtn1() {
        return i -> i + 1;
    }
    public static I2 rtn2() {
        return i -> i + 2;
    }

    public static void main(String[] args) {
        blah1(i -> i + 1);
        blah2(i -> i + 1);
    }

}
