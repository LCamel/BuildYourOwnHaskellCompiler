"use strict";
var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;

function weak_normal_form(exp) {
    if (exp[0] == "var") { return exp; }
    if (exp[0] == "lam") { return exp; }
    if (exp[0] == "app") {
        const [/*app*/, f, a] = exp;
        const [maybeLam, x, y] = weak_normal_form(f); // todo
	if (maybeLam == "lam") {
            return sub(y, x, a);
	} else {
	    return exp;
	}
    }
}
function normal_form(exp) {
    if (exp[0] == "var") { return exp; }
    if (exp[0] == "lam") { return [exp[0], exp[1], normal_form(exp[2])]; }
    if (exp[0] == "app") {
        const [maybeLam, x, y] = weak_normal_form(exp[1]);
        if (maybeLam == "lam") {
	    return normal_form(sub(y, x, exp[2])); 
	} else {
	    return ["app", normal_form(exp[1]), normal_form(exp[2])];
	}
    }
}
var nextVarIndex = 0;
function nextVar() { return "nv" + (nextVarIndex++); }

function sub(y, x, a) {
    // console.log(`sub: y: ${y} ## x: ${x} ## a: ${a}`);
    if (y[0] == "var") {
        if (y[1] == x) { return a; } else { return y; }
    } else if (y[0] == "app") {
        return ["app", sub(y[1], x, a), sub(y[2], x, a)];
    } else if (y[0] == "lam") {
        if (y[1] == x) return y;
	var fvs = freeVars(a, new Set()); // ?
	if (fvs.has(y[1])) { // oops
	    // \x (\u ...x...) (\y u)
	    var newVar = nextVar(); // cheat
	    return ["lam", newVar, sub(sub(y[2], y[1], ["var", newVar]), x, a)];
	} else {
	    return ["lam", y[1], sub(y[2], x, a)];
	}
    }
}
function freeVars(exp, bound) {
    if (exp[0] == "var") {
        return bound.has(exp[1]) ? new Set() : new Set([exp[1]]);
    } else if (exp[0] == "app") {
        return new Set([...freeVars(exp[1], bound), ...freeVars(exp[2], bound)]);
    } else if (exp[0] == "lam") {
        return freeVars(exp[2], new Set([...bound, exp[1]]));
    }
}

function check(e0, e1) {
    var e0s = JSON.stringify(e0);
    var e1s = JSON.stringify(e1);
    console.assert(e0s == e1s, "\n" + e0s + "\n" + e1s);
}

var subTestData = [
    ["u", "u", "v", "v"],
    ["(u v)", "u", "v", "(v v)"],
    ["(v v)", "u", "v", "(v v)"],
    ["(λv u)", "u", "v", "(λnv0 v)"],
    ["(λv v)", "u", "v", "(λnv0 nv0)"],
    ];
for (let [y, x, a, expected] of subTestData) {
    nextVarIndex = 0; //
    //console.log("#########0");
    const actualExp = sub(parse(y), x, parse(a));
    //console.log("actualExp: " + JSON.stringify(actualExp));
    const actual = unparse(actualExp);
    //console.log("#########1");
    //console.log(JSON.stringify(actual));
    //console.log("#########2");
    console.assert(actual == expected, `y: ${y}\nx: ${x}\na: ${a}\nexpected: ${expected}\nactual: ${actual}`)
}

//function nextVar(vars) {
//    return [...s].sort().pop() + "_";
//}
/*
var ida = ["lam", "a", ["var", "c"]];
var idb = ["lam", "b", ["var", "b"]];
var exp = ["app", ida, idb];

//console.log(normal(exp));
check(sub(["var", "c"], "a", ["lam", "b", ["var", "b"]]),
    ["var", "c"]);
check(sub(["var", "a"], "a", ["lam", "b", ["var", "b"]]),
    ["lam", "b", ["var", "b"]]);
// (\u (\v v) u) (\w w)  ->  (\v v) (\w w)
check(sub(["app", ["lam", "v", ["var", "v"]], ["var", "u"]],  "u",  ["lam", "w", ["var", "w"]]),
    ["app", ["lam", "v", ["var", "v"]], ["lam", "w", ["var", "w"]]]);
// (\u (\u u) u) (\w w)  ->  (\u u) (\w w)

*/

