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
        redex = findLeftmostOutermostRedex(exp);
        if (redex does exist) {
            exp = exp with redex being reduced;
        else
            return exp;
        }
    }
}
```

