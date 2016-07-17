對於
```
data A = B Int | C
```
這樣的宣告, 有辦法用程式取得 "A 有 B 和 C 這兩個 constructor" 的資訊嗎?

開啟 -XDeriveDataTypeable 來用 Data.Data + Data.Typeable 是一條路線.

這能力不在 language spec 裡面, 是 GHC 提供的. (= =)
https://downloads.haskell.org/~ghc/7.6.3/docs/html/users_guide/deriving.html#deriving-typeable

不過這有個限制: 被看的 data 要有自覺, 要 deriving (Typeable, Data) 才給看.

```
stack ghci --ghci-options "-XDeriveDataTypeable"
> import Data.Data
> import Data.Typeable
> data A = B Int | C deriving (Typeable, Data)

> typeOf (B 10)
A
> dataTypeOf (B 10)
DataType {tycon = "A", datarep = AlgRep [B,C]}

> :t typeOf (B 10)
typeOf (B 10) :: TypeRep
> :t dataTypeOf (B 10)
dataTypeOf (B 10) :: DataType
```

我們有興趣的 Language.Haskell.Exts.Annotated 有嗎?

從 /root/.stack/indices/Hackage/packages/haskell-src-exts/1.17.1/haskell-src-exts-1.17.1.tar.gz 中的 ./src/Language/Haskell/Exts/Annotated/Syntax.hs 來看, 有!

