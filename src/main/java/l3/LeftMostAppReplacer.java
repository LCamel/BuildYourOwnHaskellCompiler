package l3;

import l3.Nodes.Node;
import l3.Nodes.Write.App;
import l3.Nodes.Write.Lam;



public class LeftMostAppReplacer
    extends LeftMostAppFinder implements AppReplacer {
    protected Node root;

    @Override
    public void init(Node root) {
        super.init(root);
        this.root = root;
    }

    @Override
    public void replace(Node node) {
        if (isAppLeft.peek() == true) {
            path.pop();
            isAppLeft.pop();
            App parent = (App) path.peek();
            parent.setLeft(node);
        } else {
            // isAppLeft does not have to be changed
            path.pop();
            if (path.isEmpty()) {
                path.push(node);
                root = node;
            } else {
                Node parent = path.peek();
                path.push(node);

                if (parent instanceof App) {
                    ((App) parent).setRight(node);
                } else if (parent instanceof Lam) {
                    ((Lam) parent).setBody(node);
                } else {
                    throw new RuntimeException("replace: how come? " + parent);
                }
            }
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
