module Main where

import Lib

main :: IO ()
-- main = someFunc
main = do
         lines <- readFile "app/Main.hs"
         putStrLn lines
