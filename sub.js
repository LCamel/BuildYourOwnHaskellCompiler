"use strict";
var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;


function weak_normal_form(exp) {
    if (exp[0] == "var") { return exp; }
    if (exp[0] == "lam") { return exp; }
    if (exp[0] == "app") {
        const [maybeLam, x, y] = weak_normal_form(exp[1]);
        if (maybeLam == "lam") {
            return weak_normal_form(sub(y, x, exp[2]));
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

/*
var a = ["app",["lam","+1",["app",["lam","0",["app",["lam","1",["app",["lam","2",["app",["lam","3",["app",["lam","4",["app",["lam","5",["app",["lam","6",["app",["lam","7",["app",["lam","8",["app",["lam","9",["app",["lam","nil",["app",["lam","cons",["app",["lam","Y",["app",["lam","take",["app",["lam","map",["app",["lam","0-1-2-",["app",["app",["var","take"],["var","5"]],["var","0-1-2-"]]],["app",["var","Y"],["lam","0-1-2-",["app",["app",["var","cons"],["var","0"]],["app",["app",["var","map"],["var","+1"]],["var","0-1-2-"]]]]]]],["lam","f",["app",["var","Y"],["lam","go",["lam","ls",["app",["app",["var","ls"],["lam","a",["lam","as",["app",["app",["var","cons"],["app",["var","f"],["var","a"]]],["app",["var","go"],["var","as"]]]]]],["var","nil"]]]]]]]],["app",["var","Y"],["lam","take",["lam","n",["lam","ls",["app",["app",["var","n"],["lam","n-",["app",["app",["var","ls"],["lam","a",["lam","as",["app",["app",["var","cons"],["var","a"]],["app",["app",["var","take"],["var","n-"]],["var","as"]]]]]],["var","nil"]]]],["var","nil"]]]]]]]],["lam","f",["app",["lam","x",["app",["var","f"],["app",["var","x"],["var","x"]]]],["lam","x",["app",["var","f"],["app",["var","x"],["var","x"]]]]]]]],["lam","a",["lam","as",["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["var","a"]],["var","as"]]]]]]]],["lam","is-cons",["lam","is-nil",["var","is-nil"]]]]],["app",["var","+1"],["var","8"]]]],["app",["var","+1"],["var","7"]]]],["app",["var","+1"],["var","6"]]]],["app",["var","+1"],["var","5"]]]],["app",["var","+1"],["var","4"]]]],["app",["var","+1"],["var","3"]]]],["app",["var","+1"],["var","2"]]]],["app",["var","+1"],["var","1"]]]],["app",["var","+1"],["var","0"]]]],["lam","s",["lam","z",["var","z"]]]]],["lam","n",["lam","s",["lam","z",["app",["var","s"],["var","n"]]]]]]
;

var b = ["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["var","z"]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]]]]]]]],["lam","is-cons",["lam","is-nil",["var","is-nil"]]]]]]]]]]]]]]]]]]
;

var _rename = require('./rename.js'),
    rename = _rename.rename;

//var x = JSON.stringify(rename(normal_form(a)));
//var y = JSON.stringify(rename(b));
var x = JSON.stringify(normal_form(a));
var y = JSON.stringify(b);
console.assert(x == y);
*/
