"use strict";

function toDeBruijn(exp, binds) {
    if (exp[0] === "var") {
        const name = exp[1];
        const len = binds.length;
        var i = 1;
        while (true) {
            if (binds[len - i] === name) {
                return i;
            }
            i++;
        }
    } 
    if (exp[0] === "app") {
        return ["app", toDeBruijn(exp[1], binds), toDeBruijn(exp[2], binds)];
    }
    if (exp[0] === "lam") {
        var newBinds = [...binds, exp[1]];
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
    // var exp is a number
    return ["var", binds[binds.length - exp]];
}


var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;
var _rename = require("./rename.js"),
    rename = _rename.rename;
var generated_3_6_normal = JSON.parse(require("fs").readFileSync("generated_3_6_normal.json"));
for (let [exp, normal] of generated_3_6_normal) {
    var db = toDeBruijn(exp, []);
    var exp2 = fromDeBruijn(db, [], [0]);
    var x = JSON.stringify(exp);
    var y = JSON.stringify(rename(exp2));
    if (x !== y) {
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
