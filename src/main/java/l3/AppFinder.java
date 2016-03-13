package l3;


// 就像 sort algorithm 要求被 sort 的 element 要 implement "compare" 一樣
// 這裡表達 search algorithm 對被 search 的東西的要求
//
// 其實不一定要有 left / right / body 的名字, 只要能傳回 children 就好了
// 分別是 2 / 1 / 0 個 children
// 不過這樣說不定跑起來 effort 比較大?
// 用法:
// appFinder.init(root);
// if (appFinder.find()) { // deal with appFinder.getApp() }
//
public interface AppFinder {
    public void init(Nodes.Node root);
    public boolean find();
    public Nodes.Read.App getApp();
}
