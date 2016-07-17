對於
```
data A = B Int | C
```
這樣的宣告, 有辦法用程式取得 "A 有 B 和 C 這兩個 constructor" 的資訊嗎?

開啟 -XDeriveDataTypeable 來用 Data.Data + Data.Typeable 是一條路線.

這能力不在 language spec 裡面, 是 GHC 提供的. (咒罵)
https://downloads.haskell.org/~ghc/7.6.3/docs/html/users_guide/deriving.html#deriving-typeable

