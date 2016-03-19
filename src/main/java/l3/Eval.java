package l3;

import l3.Nodes.Apply.App;
import l3.Nodes.Node;

public class Eval {
    // 無法確定 root 裡面的東西有 implement "Apply"
    // cast 就死了
    // 不過像 find / replace 他們本來就不能確定別人用他們的時候會想要 apply
    // 所以自然進出時都不會有限制
    // 這樣切這麼細是不是虧了?
    public static void eval(Node root) {
        AppReplacer replacer = new LeftMostAppReplacer();
        replacer.init(root);
        while (replacer.find()) {
            App app = (App) replacer.getApp();
            System.out.println("=== found: app: " + app);
            Node newNode = app.apply();
            replacer.replace(newNode);
        }
        System.out.println("eval: " + replacer.getRoot());
    }
}
