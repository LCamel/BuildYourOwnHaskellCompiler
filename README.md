# BuildYourOwnHaskellCompiler
BuildYourOwnHaskellCompiler

SEE: https://github.com/CindyLinz/BYOHC-Workshop

## Lambda Calculus

沒有 boolean, 沒有 int. 所有的 value 都是 function. 但可設計出互動起來像 boolean/int 的 function value.

Interpreter / compiler 只要處理 function / lambda expression 就好, 剩下像boolean/整數/tuple...等都可以在 user level / library level 做, 不必內建在語言裡.

要做到這些, 就需要熟悉撰寫 lambda expression 的技巧.

### enumerated data type

某些 type 長的像 enum, 只有固定幾個值. 像 boolean 只有 true/false, 像 season 只有 春/夏/秋/冬.

Enum 的用途不在運算, 而在撰寫處理該 enum 的 function 中, 放在 if / switch / case 來判斷.

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
回顧一下: 為什麼會想把春夏秋冬設計成那個樣子? 是為了將來撰寫使用 season 的 function 時, 可以依照 season 的值得到對應的 val1 val2 val3 val4. 所以乾脆就讓 season 本身來挑選對應的 value.





### 記憶

lambda expression 可以利用外層定義的變數. 可以做出看似"記憶"的效果.

以 JavaScript 為例:
```
var f = function (x) {
    return function () {
        console.log("x was: " + x);
    };
}
var f1 = f(123);
f1(); // x was: 123
var f2 = f(456);
f2(); // x was: 456
f1(); // x was: 123
```
我們在呼叫 f1 f2 的時候沒有給參數, 可是卻可以叫出暗藏於其中的 x 值.

想要記住更多值也可以:
```
var f = function (x, y) {
    return function () {
        console.log("x: " + x + " y: " + y);
    }
};
var f1 = f(3, 4);
f1(); // x: 3 y: 4
```
或轉成每次只吃一個參數的:
```
var f = function (x) {
    return function (y) {
        return function () {
            console.log("x: " + x + " y: " + y);
        }
    }
}
var f1 = f(3)(4);
f1(); // x: 3 y: 4
```
但是 x y 光是暗藏在 function 裡面, 有辦法拿來用嗎?

我們可以預留一個洞, 要用時傳一個處理 x y 的 function 進去, 就能把暗藏的 x y 塞給它:
```
var f = function (x) {
    return function (y) {
        return function (processXY) {
            processXY(x, y);
        }
    }
}
var f1 = f(3)(4); // now f1 is loaded with 3 4

var proc = function (x, y) {
    console.log("someone gives me x: " + x + " y: " + y);
};
f1(proc); // someone gives me x: 3 y: 4
```
這就是 pair:
```
pair = \x \y \proc  proc x y
```
要記更多東西也可以如法炮製.


### recursively-defined data type

前面的 enum 雖然也可以列舉很多值, 但對於有無限個值的 type 來說, 用規則描述更輕鬆.

像自然數:
* zero 是個自然數
* 如果 n 是個自然數, 則 succ(n) 也是個自然數

因此 zero / succ(zero) / succ(succ(zero)) / succ(succ(succ(zero))) / ... 都是自然數.

像 list:
* [] 是個 list
* 如果 xs 是個 list, 則 x : xs (或 cons x xs) 也是個 list

因此 [] / a : [] / b : (a : []) / c : (b : (a : [])) / ... 都是 list.

第一條給出種子, 第二條則描述怎麼擴展.

因為所有值都是用這兩條生出來的, 所以拿到一個值時, 如果不是種子, 就一定是從某個舊值衍生出來的新值.
處理這種值的程式常寫成
```
function foo(x) {
    if (x is 種子) {
        return 邊界值;
    } else {
        // x 應該是從某較小的舊值 x0 組出來的 (或許再加料)
        return 某些處理(foo(x0))
    }
}
```
