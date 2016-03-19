切細的 interface, 想要讓 algorithm 對被應用的 data 有最小的需求

但是真的有辦法被廣泛應用嗎?

finder 和 replacer
這個"表面"看起來幾乎是通用的, 在找 app/lam 和找 free variables 上
不過實作上可以重用嗎?

想要不對 mutable 和 immutable 做假設, 真的可行嗎?

想要不對 sub / de bruijn / env 做假設, 真的可行嗎?

想要自然的支援 native 而不太需要動 code.

兩個 dimension: data (app/lam/var) 和 algorithm (find/replace/apply/free).
雖然 algorithm 面似乎比較常擴充, 但 data 面也是可能有 native.

在 algorithm 面做 data 的 pattern matching / switch ?
還是想辦法把 algorithm 分配到不同的 data 上?

想越多, 動越慢, 越挫折.

====

====
來到 native.
要找 free variable, replace 成別的東西.
這邊如果用 mutable 的方法來寫, 麻煩就是要知道 parent, 甚至要知道是 app 的左邊還是右邊.
用 finder/replacer 來寫?
是不是要把 "找到 app/lam pair" 和 "path/isAppLeft" 分開呢? 這兩個會不會綁很緊?

另外 app/lam 比較有 "順序" 的味道, 描述上就比較 imperative.
雖然 algorithm 可以等價做到純 recursive 不用 return 出來, 但是和描述對起來就不是那麼直觀.


