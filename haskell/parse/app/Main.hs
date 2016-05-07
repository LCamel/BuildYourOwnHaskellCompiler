module Main where

import Lib
import Language.Haskell.Exts.Annotated.Parser (parseModule)
import Language.Haskell.Exts.Parser ( ParseResult(ParseOk, ParseFailed) )
import Language.Haskell.Exts.Annotated.Syntax ( Module(Module), ModuleName(ModuleName), ImportDecl(importModule) )
import Language.Haskell.Exts.SrcLoc (SrcSpanInfo)
-- import Data.Map (Map, empty)
import Data.Map (Map)
import qualified Data.Map as Map


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

type MyModuleName = String

parseModuleRecursive :: MyModuleName -> Map MyModuleName (Module foo) -> Map MyModuleName (Module foo)
parseModuleRecursive = undefined


-- parseModule :: String -> ParseResult (Module SrcSpanInfo)
-- data ParseResult a
--   ParseOk a
getImportsFromModule :: Module foo -> [String]
--getImportsFromModule :: Module SrcSpanInfo -> IO ()
getImportsFromModule (Module _ _ _ importDecls _) = map (getModuleName . importModule) importDecls

getModuleName :: ModuleName foo -> String
getModuleName (ModuleName foo name) = name


collector :: (Ord a) => a -> (a -> (b, [a])) -> Map a b
collector start f = collector0 [start] f Map.empty

collector0 :: (Ord a) => [a] -> (a -> (b, [a])) -> Map a b -> Map a b
collector0 []     f m = m
collector0 (a:as) f m = if Map.member a m
                          then collector0 as f m
                          else let
                                 (b, as2) = f a
                               in
                                 collector0 (as ++ as2) f (Map.insert a b m)




{--
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Parser.html
parseModule :: String -> ParseResult (Module SrcSpanInfo)

https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Parser.html#t:ParseResult
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Syntax.html#t:Module


--}
