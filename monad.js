// IO a :: World -> (a, World)
//
// print :: Show a => a -> IO ()
// print :: Show a => a -> (World -> ((), World)) 
//
// getChar :: IO Char
//
// (>>=) :: Monad m => m a -> (a -> m b) -> m b
//
"use strict";
function print(a) {
    return function (world) {
        console.log("print: " + a);
        return [undefined, world];
    };
}
//print = (a) => (world) => [undefined, [...world, a]]; // bug

var sget = require("sget");
function getStr() {
    return function(world) {
        var s = sget();
        return [s, world];
    }
}

/*
function bind(ioa) {
    return function(f) {
        return function(w0) {
            var [a, w1] = ioa(w0);
            return f(a)(w1);
        };
    };
}
*/
//var bind = ioa => f => w0 => { var [a, w1] = ioa(w0); return f(a)(w1); };
var bind = ioa => f => w0 => ( a_w1 => f(a_w1[0])(a_w1[1]) )(ioa(w0));


//var io1 = print("asdf");
//var io2 = bind(io1)((a) => getStr());
//var io3 = bind(io2)((a) => print(a));
var foo = bind(print("First number?"))(function (x1) {
    return bind(getStr())(function (x2) {
        return bind(print("Got " + parseInt(x2) + ". Second number? "))(function (x3) {
            return bind(getStr())(function (x4) {
                return print("Got " + parseInt(x4) + ". Sum: " + (parseInt(x2) + parseInt(x4)));
            });
        });
    });
});

console.log("====");
foo("world");
console.log("done");
