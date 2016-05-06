module Main where

import Lib
import Language.Haskell.Exts.Annotated.Parser (parseModule)
import Language.Haskell.Exts.Parser ( ParseResult(ParseOk, ParseFailed) )
import Language.Haskell.Exts.Annotated.Syntax ( Module(Module) )
import Language.Haskell.Exts.SrcLoc (SrcSpanInfo)


main :: IO ()
-- main = someFunc
main = do
         lines <- readFile "app/Main.hs"
--         putStrLn lines
--         putStrLn $ show $ parseModule lines

         case parseModule lines of
--           ParseOk moduleWithSrcSpanInfo -> putStrLn "Ok"
           ParseOk moduleWithSrcSpanInfo -> getImportsFromModule moduleWithSrcSpanInfo
           ParseFailed _ _ -> putStrLn "Failed"

-- parseModule :: String -> ParseResult (Module SrcSpanInfo)
-- data ParseResult a
--   ParseOk a
-- getImportsFromModule :: Module SrcSpanInfo -> [String]
getImportsFromModule :: Module SrcSpanInfo -> IO ()
getImportsFromModule (Module _ _ _ importDecl _) = putStrLn $ show importDecl

{--
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Parser.html
parseModule :: String -> ParseResult (Module SrcSpanInfo)

https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Parser.html#t:ParseResult
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Syntax.html#t:Module


--}
