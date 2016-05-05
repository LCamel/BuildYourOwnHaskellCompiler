module Main where

import Lib
import Language.Haskell.Exts.Annotated.Parser


main :: IO ()
-- main = someFunc
main = do
         lines <- readFile "app/Main.hs"
--         putStrLn lines
         putStrLn $ show $ parseModule lines
