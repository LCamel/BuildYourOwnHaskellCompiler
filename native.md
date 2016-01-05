加入 native 的構造.

一些 opaque 的東西.

不再永遠是 function 了.

加入一些特別的 value, 和能處理這些 value 的 function.

如整數, +-*/.

如字串, 一些字串操作.

如boolean, 一些 boolean functions.

如list, 一些 list functions.

這些 value 和 function 可以被原來求 normal form 的過程處理.

((\x x) 1) -> 1

((+ 2) 3) -> 5

((+ 2) ((\x x) 3)) -> 5

((\f ((f 2) 3) +) -> 5

不過以前 1 是個吃兩個 input 的 function 這件事情就不成立了.

考慮 \x + 2 x
