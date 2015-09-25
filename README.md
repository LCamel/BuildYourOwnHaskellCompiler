# BuildYourOwnHaskellCompiler
BuildYourOwnHaskellCompiler

SEE: https://github.com/CindyLinz/BYOHC-Workshop

## Lambda Calculus

沒有 boolean, 沒有 int. 所有的 value 都是 function. 但可設計出互動起來像 boolean/int 的 function value.

Interpreter / compiler 只要處理 function / lambda expression 就好, 剩下像boolean/整數/tuple...等都可以在 user level / library level 做, 不必內建在語言裡.

要做到這些, 就需要熟悉撰寫 lambda expression 的技巧.

### enumerated data type

某些 type 長的像 enum, 只有固定幾個值. 像 boolean 只有 true/false, 像 season 只有 春/夏/秋/冬.

Enum 用途不在運算, 而在撰寫處理該 enum 的 function 中, 放在 if / switch / case 來判斷.

打個不太精確的比方:
```
function foo(season) {
if (season == 春) { return val1; }
if (season == 夏) { return val2; }
if (season == 秋) { return val3; }
if (season == 冬) { return val4; }
}
```
其實我們也不是真的想知道 "season == 春" 的 boolean value, 而是想要隨著 season 的不同傳回不同的 value, 做不同的事.
```
result = case season of 春 -> val1
                        夏 -> val2
                        秋 -> val3
                        冬 -> val4                        
```
在 lambda calculus 裡, "春" 要用哪個 function 來代表不是重點, 而是要能實作上面的 case season of. 也就是我們想要一個 "case" function, 吃一個 season 的 value 和四個 val1 val2 val3 val4, 能傳回對應的 valN.
```
case = \season \val1 \val2 \val3 \val4 (...)
```
這裡的 ... 要怎麼寫? 技巧: 我們把那四個 value 直接餵給 season 這個 function (所有東西都是 function), 叫它自己挑一個對的回來.
```
case = \season \val1 \val2 \val3 \val4  season val1 val2 val3 val4
```
因此, "春" 是一個吃四個參數的 function, 簡單傳回第一個參數, 夏秋冬也類似:
```
春 = \val1 \val2 \val3 \val4  val1
夏 = \val1 \val2 \val3 \val4  val2
秋 = \val1 \val2 \val3 \val4  val3
冬 = \val1 \val2 \val3 \val4  val4
```
如果我們選了這個設計, 再熟一點就可以跳過 case 這個 function, 直接寫 function body: season val1 val2 val3 val4, 就可以拿到對的值.

這個設計只要是 4 個 value 的 enumeration 都可以用, 不管是春夏秋冬, 還是黑桃紅心磚塊梅花.

也可以延伸到其他 n 個 value 的 enumeration, 像只有兩個 value 的 boolean.
```
TRUE  = \val1 \val2  val1
FALSE = \val1 \val2  val2
```




