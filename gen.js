"use strict";

// 4 == 3 + 1 // lam
//   == 0 + 1 + 3 == 1 + 1 + 2 == 2 + 1 + 1 == 3 + 1 + 0
function gen(vars, level) {
    var exps = [];
    exps[0] = vars.map(v => ["var", v]);
    
    for (var n = 1; n <= level; n++) {
        var lvN = [];
        for (let e of exps[n - 1]) {
            for (let v of vars) {
                lvN.push(["lam", v, e]);    
            }
        }
        for (let i = 0; i <= n - 1; i++) {
            let j = (n - 1) - i;
            for (let e1 of exps[i]) {
                for (let e2 of exps[j]) {
                    lvN.push(["app", e1, e2]);
                }
            }
        }
        exps[n] = lvN;
    }
    return exps;
}
// "bound" is a Set
function hasFree(exp, bound) {
    if (exp[0] == "var") { return ! bound.has(exp[1]); }
    if (exp[0] == "app") { return hasFree(exp[1], bound) || hasFree(exp[2], bound); }
    var newBound = new Set(bound);
    newBound.add(exp[1]);
    return hasFree(exp[2], newBound);
}

var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;
var _rename = require("./rename.js"),
    rename = _rename.rename;

function genNoDup(vars, level) {
    var dedupExps = new Set();
    for (let lvN of gen(vars, level)) {
        for (let e of lvN) {
            if (!hasFree(e, new Set())) {
                dedupExps.add(unparse(rename(e)));
            }
        }
    }
    var result = [];
    for (let x of dedupExps) {
        result.push(parse(x));
        //console.log(JSON.stringify(parse(x)));
    }
    return result;
}
//console.log(genNoDup(["u", "v"], 3));
exports.gen = gen;
exports.genNoDup = genNoDup;


/*
var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;
var _rename = require("./rename.js"),
    rename = _rename.rename;
var dedupExps = new Set();
for (let lvN of gen(["u", "v", "w"], 5)) {
    //console.log("======");
    for (let e of lvN) {
        //var free = hasFree(e, new Set());
        //console.log(free + " " + unparse(e));
	if (!hasFree(e, new Set())) {
	    //console.log(unparse(e));
	    dedupExps.add(unparse(rename(e)));
	}
    }
}
console.log(dedupExps);
*/
