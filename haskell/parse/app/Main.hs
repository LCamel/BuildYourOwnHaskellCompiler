module Main where

import Lib
import Language.Haskell.Exts.Annotated.Parser (parseModule)
import Language.Haskell.Exts.Parser ( ParseResult(ParseOk, ParseFailed) )
import Language.Haskell.Exts.Annotated.Syntax ( Module(Module), ModuleName(ModuleName), ImportDecl(importModule) )
import Language.Haskell.Exts.SrcLoc (SrcSpanInfo)
-- import Data.Map (Map, empty)
import Data.Map (Map)
import qualified Data.Map as Map

--main :: IO ()
-- main = nameToModuleAndNames "A" >>= putStrLn . show
-- main = putStrLn . show $ (collect (pure ["A"]) nameToModuleAndNames (pure Map.empty) )
main = do
         foo <- (collect (pure ["A"]) nameToModuleAndNames (pure Map.empty) )
         putStrLn (show foo)
         putStrLn (show $ Map.keys foo)

-- name -> path :: String -> IO String
-- path -> content :: String -> IO String
-- content -> Module and names :: String -> (Module, [String])
-- so...
-- name -> Module and names :: String -> IO (Module, [String])

nameToModuleAndNames :: String -> IO (Module SrcSpanInfo, [String])
nameToModuleAndNames name = fmap moduleSrcToModuleAndNames (readFile $ "sample_input/" ++ name ++ ".hs")

moduleSrcToModuleAndNames :: String -> (Module SrcSpanInfo, [String])
moduleSrcToModuleAndNames srcStr = case parseModule srcStr of
                                     ParseOk mod -> let
                                                       names = getImportsFromModule mod
                                                    in
                                                       (mod, names)
                                     ParseFailed _ _ -> error $ "bad src:\n" ++ srcStr

getImportsFromModule :: Module foo -> [String]
getImportsFromModule (Module _ _ _ importDecls _) = map (getModuleName . importModule) importDecls
getModuleName :: ModuleName foo -> String
getModuleName (ModuleName foo name) = name

-- now let's collect them
-- except the first module name, other module names might be contaminated by IO ...

collect :: IO [String] -> (String -> IO (Module SrcSpanInfo, [String])) -> IO (Map String (Module SrcSpanInfo)) -> IO (Map String (Module SrcSpanInfo))
collect ioNames f ioMap0 = do
                             names <- ioNames
                             map0 <- ioMap0
                             case names of
                               [] -> ioMap0
                               n:ns -> if Map.member n map0
                                         then collect (pure ns) f ioMap0
                                         else do
                                                (mod, ns2) <- f n
                                                collect (pure (ns ++ ns2)) f (pure (Map.insert n mod map0))
  
  

{--
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Parser.html
parseModule :: String -> ParseResult (Module SrcSpanInfo)

https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Parser.html#t:ParseResult
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Syntax.html#t:Module


--}
