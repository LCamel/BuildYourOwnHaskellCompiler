package l3;

import java.util.Stack;

import l3.Nodes.Node;
import l3.Nodes.Read.App;
import l3.Nodes.Read.Var;

public class LeftMostAppFinder implements AppFinder {
    // 這裡比較節省地用了 Stack 的 interface
    // 我們最多只需要 peek top, 不一定需要 peek 兩個
    // 沒有用 LinkedList, 因為語意比較豪華 (Deque), 也要一直 allocate
    //
    // 另外有個差別在: LinkedList 的 peek 在空的時候是 null, 而 Stack 是 throw exception
    //
    // 實作: 因為沒有要 replace, 所以也沒有把 root 記下來
    protected Stack<Node> path = new Stack<>();
    protected Stack<Boolean> isAppLeft = new Stack<>();

    @Override
    public void init(Node root) {
        path.push(root);
        isAppLeft.push(false);
    }

    @Override
    public boolean find() {
        //System.out.println("find: path: " + path);
        // 隨著 runtime 時 Lam 所在的位置, 對 Lam 的期待也不同
        // 難避免 runtime cast 失敗. 或許有辦法, 不過可能挺複雜

        while (true) {
            Node curr = path.peek();
            if (curr instanceof App) {
                Node left = ((App) curr).getLeft();
                if (left instanceof Nodes.Apply.Lam) {
                    return true;
                } else {
                    path.push(left);
                    isAppLeft.push(true);
                }
            } else if (curr instanceof Nodes.Read.Lam) {
                path.push(((Nodes.Read.Lam) curr).getBody());
                isAppLeft.push(false);
            } else if (curr instanceof Var) {
                while (true) {
                    path.pop();
                    boolean isLeft = isAppLeft.pop();
                    if (isLeft == true) { // the only one case who has a next sibling
                        path.push(((App) path.peek()).getRight());
                        isAppLeft.push(false);
                        break;
                    }

                    if (path.isEmpty()) {
                        return false;
                    }
                }
            } else {
                throw new RuntimeException("strange: curr: " + curr);
            }
        }
    }

    @Override
    public App getApp() {
        return (App) path.peek();
    }

}
