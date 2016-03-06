package l2;



public class LeftMostAppReplacer extends LeftMostAppFinder implements AppReplacer {
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
            AppReplacer.App parent = (AppReplacer.App) path.peek();
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

                if (parent instanceof AppReplacer.App) {
                    ((AppReplacer.App) parent).setRight(node);
                } else if (parent instanceof AppReplacer.Lam) {
                    ((AppReplacer.Lam) parent).setBody(node);
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
