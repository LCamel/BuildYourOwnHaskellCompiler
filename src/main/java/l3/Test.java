package l3;
import l3.Nodes.Node;
public class Test {
    public static void main(String[] args) {
        String line = "( λx (( λx x ) x) )";
        Node node = Basic.parse(line);
        System.out.println("node: " + node);
        Eval.eval(node);
        System.out.println("node: " + node);
    }
}
