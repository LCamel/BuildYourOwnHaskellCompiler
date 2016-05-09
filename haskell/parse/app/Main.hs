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
main = nameToModuleAndNames "A" >>= putStrLn . show

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
                                         then ioMap0
                                         else ioMap0
  
  
--                                       (mod, newNames) <- f n
--                                       map0
--collect ioNames f map0 = ioNames >>= (\names -> case names of
--                                                  [] -> map0
--                                                  n:ns -> do
--                                                            (mod, names) <- f n
--                                                            map0
--
--                                     )


{--
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



--collectModule :: String -> Map (String (Module SrcSpanInfo))
--collectModule name = collector name moduleNameToModuleAndNames


moduleSrcToModuleAndNames :: String -> (Module SrcSpanInfo, [String])
moduleSrcToModuleAndNames srcStr = case parseModule srcStr of
                                     ParseOk mod -> let
                                                       names = getImportsFromModule mod
                                                    in
                                                       (mod, names)
                                     ParseFailed _ _ -> error $ "bad src:\n" ++ srcStr

moduleNameToModuleAndNames :: String -> IO (Module SrcSpanInfo, [String])
moduleNameToModuleAndNames name = fmap moduleSrcToModuleAndNames (findModuleSource name)

       
-- parseModule :: String -> ParseResult (Module SrcSpanInfo)
-- data ParseResult a
--   ParseOk a
getImportsFromModule :: Module foo -> [String]
--getImportsFromModule :: Module SrcSpanInfo -> IO ()
getImportsFromModule (Module _ _ _ importDecls _) = map (getModuleName . importModule) importDecls

getModuleName :: ModuleName foo -> String
getModuleName (ModuleName foo name) = name


findModuleSource :: String -> IO String
findModuleSource name = readFile $ "sample_input/" ++ name ++ ".hs"

srcToModule :: String -> Module SrcSpanInfo
srcToModule srcStr = case parseModule srcStr of
                       ParseOk blah -> blah
                       ParseFailed _ _ -> error $ "bad src:\n" ++ srcStr

collector :: (Ord a) => a -> (a -> IO (b, [a])) -> IO (Map a b)
collector start f = collector0 [start] f Map.empty

collector0 :: (Ord a) => [a] -> (a -> IO (b, [a])) -> IO (Map a b) -> IO (Map a b)
collector0 []     f m = m
collector0 (a:as) f m = if Map.member a m
                          then collector0 as f m
                          else let
                                 (b, as2) = f a
                               in
                                 collector0 (as ++ as2) f (Map.insert a b m)



--}

{--
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Parser.html
parseModule :: String -> ParseResult (Module SrcSpanInfo)

https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Parser.html#t:ParseResult
https://hackage.haskell.org/package/haskell-src-exts-1.17.1/docs/Language-Haskell-Exts-Annotated-Syntax.html#t:Module


--}
