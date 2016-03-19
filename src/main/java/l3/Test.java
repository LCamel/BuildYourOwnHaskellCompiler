package l3;
import l3.Nodes.Node;
public class Test {
    public static void main(String[] args) {
//        String line = "( λx (( λy x ) x) )";
//        Node node = Basic.parse(line);
        String line = "[\"lam\",\"_0\",[\"lam\",\"_1\",[\"app\",[\"var\",\"_0\"],[\"app\",[\"lam\",\"_2\",[\"var\",\"_2\"]],[\"app\",[\"var\",\"_0\"],[\"var\",\"_1\"]]]]]]";
        Node node = Basic.convertFromJson(line);
        System.out.println("node: " + node);
        Eval.eval(node);
        //System.out.println("node: " + node);
    }
}
