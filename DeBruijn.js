"use strict";

function p(x) {
    console.log("###" + x + "###");
    return p;
}
function getInternal(name) {
    console.log("getInternal: name: " + "##" + name + "##");
    if (name === "+") {
        return ["int", (x) => ["int", (y) => ["int", 
            deBruijnLeftmost(x, false)[1] + 
            deBruijnLeftmost(y, false)[1]
        ]]];
    }
    if (name === "bind") {
console.log("bind~~~~~~~~~~~~~~~~");
        return ["int", function (ioa) {
console.log("got ioa~~~~~~");
            return ["int", function(f) {
console.log("got f~~~~~~");
                return ["int", function(w0) {
                    var _w0 = deBruijnLeftmost(w0, false)[1];
                    var _f = deBruijnLeftmost(f, false);
console.log("00000");
console.log(JSON.stringify(_f));
console.log("1111");
          _f = _f[1];
                    var _ioa = deBruijnLeftmost(ioa, false)[1];
console.log(">>>>>>");
console.log("_w0:");
console.log(_w0);
console.log("_f:");
console.log(_f.toString());
console.log("_ioa.toString()");
console.log(_ioa.toString());
console.log("<<<<<<<");
                    
                    var [a, w1] = _ioa(_w0);
                    return ["int", _f(a)(w1)];
                }];
            }];
        }];
    }
            
    if (name === "print") {
        return ["int", (a) => ["int", (world) => {
            console.log("print: " + deBruijnLeftmost(a, false)[1]); 
            console.log("world: ");
            console.log(world);
            var io = [88, deBruijnLeftmost(world, false)[1]];
            var tmp = ["int", io];
            console.log("print returns: ")
            console.log(tmp);
            return tmp;
        }]];
    }
    if (name === "arrGet") {
        return ["int", (arr) => ["int", (idx) => {
            console.log("11111111");
            var theArr = deBruijnLeftmost(arr, false)[1];
            console.log("type of theArr: " + typeof(theArr));
            console.log("value of theArr: " + theArr);
            var theIdx = deBruijnLeftmost(idx, false)[1];
            console.log("type of theIdx: " + typeof(theIdx));
            console.log("value of theIdx: " + theIdx);
            var tmp = ["int", theArr[theIdx]];
            console.log("arrGet returns:");
            console.log(tmp);
            return tmp;
        }]];
    }


    return ["int", parseInt(name)];
}


function toDeBruijn(exp, binds) {
    if (exp[0] === "var") {
        const name = exp[1];
        const len = binds.length;
        var i = 1;
        while (true) {
            if (i > len) { return getInternal(name); }

            if (binds[len - i] === name) {
                return i; // TODO: 1 .. n (without the "var" tag?)
            }
            i++;
        }
    } 
    if (exp[0] === "app") {
        return ["app", toDeBruijn(exp[1], binds), toDeBruijn(exp[2], binds)];
    }
    if (exp[0] === "lam") {
        var newBinds = [...binds, exp[1]]; // TODO: better data structure
        return ["lam", toDeBruijn(exp[2], newBinds)];
    }
    throw "not here";
}
function fromDeBruijn(exp, binds, nextVarRef) {
    if (exp[0] === "app") {
        return ["app", fromDeBruijn(exp[1], binds, nextVarRef)
                     , fromDeBruijn(exp[2], binds, nextVarRef)];
    }
    if (exp[0] === "lam") {
        const newVar = "_" + (nextVarRef[0]++);
        return ["lam", newVar
                     , fromDeBruijn(exp[1], [...binds, newVar], nextVarRef)];
    }
    if (exp[0] === "int") {
        return exp;
    }
    // var exp is a number
    return ["var", binds[binds.length - exp]];
}




function beta(A, B) {
    return doA(A, B, 1);
}
function doA(a, B, aLamDepth) {
    if (a[0] === "app") {
        return ["app", doA(a[1], B, aLamDepth), doA(a[2], B, aLamDepth)];
    }
    if (a[0] === "lam") {
        return ["lam", doA(a[1], B, aLamDepth + 1)];
    }
    if (a[0] === "int") {
        return a;
    }
    // var is just a number
    if (a === aLamDepth) {
        return doB(B, aLamDepth, 0);
    } else if (a > aLamDepth) {
        return a - 1;
    } else {
        return a;
    }
}
function doB(b, aLamDepth, bLamDepth) {
    if (b[0] === "app") {
        return ["app", doB(b[1], aLamDepth, bLamDepth), doB(b[2], aLamDepth, bLamDepth)];
    }
    if (b[0] === "lam") {
        return ["lam", doB(b[1], aLamDepth, bLamDepth + 1)];
    }
    if (b[0] === "int") {
        return b;
    }
    // var is just a number
    if (b > bLamDepth) {
        return b + aLamDepth - 1;
    } else {
        return b;
    }
}
function deBruijnLeftmost(exp, waitLam) {
    if (exp[0] === "lam") {
        if (waitLam) {
            return exp;
        } else {
            return ["lam", deBruijnLeftmost(exp[1], false)];
        }
    }
    if (exp[0] === "app") {
        var newLeft = deBruijnLeftmost(exp[1], true);
        if (newLeft[0] === "lam") {
            return deBruijnLeftmost(beta(newLeft[1], exp[2]), waitLam);
        } else if (newLeft[0] === "int" && newLeft[1] instanceof Function) {
            return deBruijnLeftmost(newLeft[1](exp[2]), waitLam);
        } else {
            return ["app", newLeft, deBruijnLeftmost(exp[2], false)];
        }
    }
    // ["int" ...] can also be just returned as-is
    
    // var exp is just a number
    return exp;
}


var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse, parseFile = _parse.parseFile;
var _rename = require("./rename.js"),
    rename = _rename.rename;
var generated_3_6_normal = JSON.parse(require("fs").readFileSync("generated_3_6_normal.json"));
for (let [exp, normal] of generated_3_6_normal) {
    /*
    // conversion test
    var db = toDeBruijn(exp, []);
    var exp2 = fromDeBruijn(db, [], [0]);
    var x = JSON.stringify(exp);
    var y = JSON.stringify(rename(exp2));
    if (x !== y) {
        console.log("x: " + x);
        console.log("y: " + y);
        throw "die";
    }
    */
    //console.log("==== normalize test ===="); 
    //console.log("exp: " + JSON.stringify(exp));
    var db = toDeBruijn(exp, []);
    //console.log("db: " + JSON.stringify(db));
    //console.log("----");
    var normalDB = deBruijnLeftmost(db, false);
    var exp2 = fromDeBruijn(normalDB, [], [0]);
    var x = JSON.stringify(exp2);
    var y = JSON.stringify(normal);
    if (x !== y) {
        console.log("----");
        console.log("exp: " + JSON.stringify(exp));
        console.log("db: " + JSON.stringify(db));
        console.log("normalDB: " + JSON.stringify(normalDB));
        console.log("x: " + x);
        console.log("y: " + y);
        throw "die";
    }

}
console.log("done");

/*
var exp = parse("((λx (λy (x y)))(λx x))");
console.log(JSON.stringify(exp));
var db = toDeBruijn(exp, []);
console.log(JSON.stringify(db));
var exp2 = fromDeBruijn(db, [], [0]);
console.log(JSON.stringify(exp2));
*/

//var exp = parse("((λx (λy (x y)))(λx 5))");
//var exp = parse("( ( +   ( (λy y) 2) )   ( (λy y) 3)    )");
//var exp = parse("( ( +   2 )  3    )");

//var bind = "(λioa (λf (λw0 (  (λa_w1  ((f  ((arrGet a_w1) 0))  ((arrGet a_w1) 1))    )    (ioa w0)     ))))"
//var exp = parse(`((λbind ( print 3 )) ${bind}   )`);
var exp = parseFile("DeBruijn.ulc");
console.log("exp: " + JSON.stringify(exp));
var db = toDeBruijn(exp, []);
console.log("de Bruijn: "); // + JSON.stringify(db));
console.log(db);
console.log("=========");
var db = deBruijnLeftmost(db, false);

var exp2 = fromDeBruijn(db, [], [0]);
//console.log(JSON.stringify(exp2));
console.log("======= going to apply world");
exp2[1](["int", 999999]);
/*
function timeDiff(f) {
    const t0 = Date.now();
    console.log(f());
    console.log(Date.now() - t0);
}

console.log("=================================");
var w = "(\\x (x (x (x (x (x (x (x (x (x x))))))))))";
w = `(${w} (${w} (${w} (${w} (${w} (${w} (\\x x)))))))`;
w = parse(w);
w = toDeBruijn(w, []);
//console.log(JSON.stringify(deBruijnLeftmost(w, false)));
console.log("1111");
timeDiff(() => deBruijnLeftmost(w, false));
console.log("2222");
*/
