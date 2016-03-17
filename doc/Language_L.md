Language L

先前寫 Lambda expression 求值的時候, 在沒有用 free variable 來代表如 int / print 等 native object 時, 大家的程式是一致的.

(當然也不是說互通一致是非要不可的特性啦)

有沒有辦法描述這個語言的語意呢? ("語意" 兩字非 formal)

我想藉由定義這個語言 (就叫做 L 好了) 的過程, 想清楚一些先前沒想清楚的東西, 像 lazy / 重複求值 / 求值的方法與順序等.

先把 L 的程式從文字 parse 成一棵樹, 基本有三種 node: app / lam / var.

先想 free variable 要怎麼辦. 目前考慮 parse 完以後, 有一個 phase 是把這些 free var 都換掉. (雖然實際的代換時間不一定在此時)

換成什麼呢? 換出來的東西有什麼特性? 這似乎和求值那邊需要判斷哪些特性有關.

求值: 先考慮沒有 native 的, 也就沒有副作用的情況. 看看有沒有辦法 generalize 到有副作用的情況. (lambda calculus 本身沒有 IO (像C一樣), 現在是要擴充)


把所有能代入 function 的地方都代光. 雖無副作用, 但順序仍有差別.

加入 native 呢?


        isApp()
    isLam()









====
2016-03-12

嘗試著在 algorithm 端寫出對 Node 的需求, 比方說 AppFinder.Lam.
但是後來其他地方不是在 find, 也引用了這樣的宣告.

後來想, 應該是依照其性質功能來命名, 不是跟著 algorithm 走.
就像 Comparable 也不是跟著 sort 走一樣.

分支: 有些 algorithm 用不到 Lam 的 param, 用不到 Var 的 name.
分支: 有些 algorithm 的寫法是 immutable 的, 不需要 setBody / setLeft / ..

明確寫出 algorithm 對 data 的需求, 相較於只有一種 App/Lam/Var, 得到了什麼?
演算法真的容易重用了嗎?

像走 sub/deb/env 路線的, 能共用的東西還剩多少?

又卡在這邊太久了.
