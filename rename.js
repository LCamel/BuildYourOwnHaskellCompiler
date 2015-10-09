"use strict";

class Env {
   constructor(outer, k, v) {
       this.outer = outer;
       this.k = k;
       this.v = v;
   }
   get(key) {
       return (key == this.k) ? this.v : this.outer.get(key);
   }
   static empty() { return new Env(new Map()); }
}
class NextVar {
    constructor() { this.nextVar = 0; }
    next() { return "_" + (this.nextVar++); }
}
function rename0(exp, env, nextVar) {
    if (exp[0] == "var") {
        var v = env.get(exp[1]);
	return ["var", (v == undefined) ? nextVar.next() : v];
    } else if (exp[0] == "lam") {
        var v = nextVar.next();
	var newEnv = new Env(env, exp[1], v);
	return ["lam", v, rename0(exp[2], newEnv, nextVar)];
    } else if (exp[0] == "app") {
        return ["app", rename0(exp[1], env, nextVar), rename0(exp[2], env, nextVar)];
    } 
}
function rename(exp) { return rename0(exp, Env.empty(), new NextVar()); }

// TODO: es6 import
exports.rename = rename;

/*
var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;

var testData = [
    //["x", "_0"],
    //["y", "_0"],
    //["(λv u)", "(λ_0 _1)"],
    // what about (v v) ?!
    ["(λv v)", "(λ_0 _0)"],
    ["(λv (v v))", "(λ_0 (_0 _0))"],
    ["(λv (λv v))", "(λ_0 (λ_1 _1))"],
    ["((λv v) (λv v))", "((λ_0 _0) (λ_1 _1))"],
    ["((λy y) (λx x))", "((λ_0 _0) (λ_1 _1))"],
    ["(λ_1 (_1 _1))", "(λ_0 (_0 _0))"],
    ["(λ_0 (_0 _0))", "(λ_0 (_0 _0))"],
    ["(λ_1 (λ_0 _0))", "(λ_0 (λ_1 _1))"],
    ];
for (let [orig, expected] of testData) {
    const actualExp = rename(parse(orig));
    const actual = unparse(actualExp);
    console.assert(actual == expected, `\nactual: ${actual}\nexpected: ${expected}`);
}
*/
