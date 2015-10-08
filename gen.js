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

exports.gen = gen;

/*
var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;
for (let lvN of gen(["u", "v"], 2)) {
    console.log("======");
    for (let e of lvN) {
        console.log(unparse(e));
    }
}
*/
