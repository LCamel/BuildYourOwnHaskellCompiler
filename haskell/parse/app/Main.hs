module Main where

import Lib
import Language.Haskell.Exts.Annotated.Parser (parseModule)
import Language.Haskell.Exts.Parser ( ParseResult(ParseOk, ParseFailed) )
import Language.Haskell.Exts.Annotated.Syntax ( Module(Module), ModuleName(ModuleName), ImportDecl(importModule) )
import Language.Haskell.Exts.SrcLoc (SrcSpanInfo)


main :: IO ()
-- main = someFunc
main = do
         lines <- readFile "app/Main.hs"
--         putStrLn lines
--         putStrLn $ show $ parseModule lines

         case parseModule lines of
--           ParseOk moduleWithSrcSpanInfo -> putStrLn "Ok"
--           ParseOk moduleWithSrcSpanInfo -> getImportsFromModule moduleWithSrcSpanInfo
           ParseOk moduleWithSrcSpanInfo -> putStrLn $ show $ getImportsFromModule moduleWithSrcSpanInfo
           ParseFailed _ _ -> putStrLn "Failed"

-- parseModule :: String -> ParseResult (Module SrcSpanInfo)
-- data ParseResult a
--   ParseOk a
getImportsFromModule :: Module SrcSpanInfo -> [String]
--getImportsFromModule :: Module SrcSpanInfo -> IO ()
getImportsFromModule (Module _ _ _ importDecls _) = map (getModuleName . importModule) importDecls

getModuleName :: ModuleName l -> String
getModuleName (ModuleName l name) = name



{--
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Parser.html
parseModule :: String -> ParseResult (Module SrcSpanInfo)

https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Parser.html#t:ParseResult
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Syntax.html#t:Module


--}
