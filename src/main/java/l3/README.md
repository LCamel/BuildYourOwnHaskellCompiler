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


把 freevar 換成 native integer 可以了.
再來是 native function.

print.
IO.
pair.

Pair 一般化: tuple

    function (x, y) {
        return new function(onXY) {
          onXY(x, y);
        }
    }

    \x \y (\onXY x y)

    node --harmony
    > mkPair = x => y => onXY => onXY(x)(y);
    [Function]
    > fst = x => y => x
    [Function]
    > snd = x => y => y
    [Function]
    > pair34 = mkPair(3)(4)
    [Function]
    > pair34(fst)
    3
    > pair34(snd)
    4


IO

IO () 是個 function, 給 application 用的是 ()
IO Int 是個 function, 給 application 用的是 Int

world -> (a, world)

print 是 Int -> IO ()
也就是 Int -> ( world -> ((), world) )

以前是 return 一個 JavaScript 的 function

現在 return 一個 Lam, 能 apply 的 Lam, 但不用 getParam, 甚至不用 getBody (吧)

結果發現, 原先 AppFinder 就有分支需要 getBody().
雖然在 app 下直接 lam 的 case 只會叫到 apply,
但是如果上面沒有 app 罩著, 就會想要 getBody(), 就爆炸了.

像去化簡 ((+ 3) 4) 還 OK
去化簡 (+ 3) 可能就掛了, 因為 (+ 3) 回來是個 Lam, 就會往下鑽.


實作了 "+", 覺得 Apply.Lam 繼承 Apply.Read 有點多餘, 多了用不到的 getBody().
用個 abstract class / default method.
或者是分家分更細. 在 finder 裡面分兩種 Lam: 沒要 body 要 apply 的 / 要 body 沒要 apply 的.
這樣和 finder 的用戶怎麼講呢?
"App 左下的 Lam 只要能 apply 就好, 其他地方的 Lam 則要能 getBody."





  



 











