# Evaluation Strategy

好像有很多種 evaluation strategy

```
function f(x) {
    return 1;
}
f(1 / 0);
```

Applicative Order 先求參數所以會死, Normal Order 先求 body, 參數沒用到就不會去算所以不會死.

什麼是"求"/"算" ?

好像就是找 normal form.

什麼是 normal form?

好像就是 beta normal form. 就是沒有 beta reduction 可以做的 form. 就是沒有 ((\x foo) bar). 就是沒有 app 左下角有 lam 的 tree.

有些 lambda expression 可以算出 (beta) normal form, 有的會算不停, 就是沒有 (beta) normal form 吧. 大概也沒辦法自動判斷算不算的停. 10000 步沒停, 說不定 10001 步就停了.

有人說, normal order 是 "只要有 (beta) normal form 就必定能被求出來" 的厲害 strategy.

有人說, normal order 就是每次化簡 leftmost outermost redex 的 strategy.

這和 "先求 body" 的概念一樣嗎?

感覺像這樣 (先不管效能)(也不管遞迴)
```
function getBetaNormalFormWithNormalOrderEvaluationStrategy(exp) {
    while (true) {
        redex = findLeftmostOutermostRedex(exp); // pseudo code
        if (redex does exist) {
            exp = exp with redex being reduced;
        else
            return exp;
        }
    }
}
```

那, 怎麼找到所謂 leftmost outermost 的 redex 呢?

Redex: ( (\x ...) ... ) 就是一個 redex. 也就是 tree 上有 ... ["app", ["lam", ...] ...] ... .

什麼是 leftmost ? 據說是寫成一行文字時來比左右的.

什麼是 outermost ? 據說是沒被別的 redux 包住的 redex. (所以光被別的 lambda 包沒關係)

如果我們這樣做, 找出來的是 leftmost outermost redex 嗎?
```
function findLeftmostOutermostRedex(exp) {
    if (exp[0] == "var") {
        // not here
    } else if (exp[0] == "app") {
        if (exp[1][0] == "lam") { // is my left child a "lam" ?
            // found it !!
        } else {
            // go left, then go right
        }
    } else {
        // go right (the body)
    }
}
```



