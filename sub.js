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

// "y" will not be modified
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
    } else if (exp[0] == "shr") {
        return freeVars(exp[2], bound);
    }
}

// exp will not be modified
function reduceLeftmostOutermost(exp) {
    if (exp[0] === "var") {
        return [false, exp];
    } else if (exp[0] === "lam") {
        var [reduceHappened, reducedBody] = reduceLeftmostOutermost(exp[2]);
        return [reduceHappened, [exp[0], exp[1], reducedBody]];
    } else {
        // app
        if (exp[1][0] === "lam") {
            // yes !
            return [true, sub(exp[1][2], exp[1][1], exp[2])];
        } else {
            var [reduceHappened, reducedBody] = reduceLeftmostOutermost(exp[1]);
            if (reduceHappened) {
                return [reduceHappened, ["app", reducedBody, exp[2]]];
            } else {
                var [reduceHappened2, reducedBody2] = reduceLeftmostOutermost(exp[2]);
                return [reduceHappened2, ["app", exp[1], reducedBody2]];
            }
        }
    }
}

function reduceLeftmostOutermostTillDone(exp) {
    while (true) {
        nextVarIndex = 0; //
        var [reduceHappened, reducedExp] = reduceLeftmostOutermost(exp);
        if (reduceHappened) {
            exp = reducedExp;
        } else {
            return reducedExp;
        }
    }
}
/*
function leftmostOutermost(exp, isWaiting = false) {
    if (exp[0] === "var") { return exp; }
    if (exp[0] === "lam") {
        return isWaiting ? exp : [exp[0], exp[1], leftmostOutermost(exp[2], false)];
    }
    var newLeft = leftmostOutermost(exp[1], true);
    if (newLeft[0] === "lam") {
        return leftmostOutermost(sub(newLeft[2], newLeft[1], exp[2]), isWaiting);
    } else {
        return ["app", newLeft, leftmostOutermost(exp[2], false)];
    }
}
*/
function leftmostOutermost(exp) {
    function me(exp, waitLam) {
        if (exp[0] === "var") { return exp; }
        if (exp[0] === "lam") {
            return waitLam ? exp : [exp[0], exp[1], me(exp[2], false)];
        }
        var newLeft = me(exp[1], true);
        if (newLeft[0] === "lam") {
            return me(sub(newLeft[2], newLeft[1], exp[2]), waitLam);
        } else {
            return ["app", newLeft, me(exp[2], false)];
        }
    }
    return me(exp, false);
}

function leftmostOutermostShared(exp) {
    function me(exp, waitLam) {
        console.log("=======");
        console.log("me: exp: " + JSON.stringify(exp) + " waitLam: " + waitLam);
        if (exp[0] === "var") { return exp; }
        if (exp[0] === "lam") {
            return waitLam ? exp : [exp[0], exp[1], me(exp[2], false)];
        }
        if (exp[0] == "shr") {
            if (exp[1] == false) {
                exp[1] = true;
                var tmp = exp[2] = me(exp[2], waitLam);
                console.log("shr going to return: 1 : " + JSON.stringify(tmp));
                return tmp;
            } else {
                //return exp[2];
                console.log("shr going to return: 2 : " + JSON.stringify(exp[2]));
                return exp[2];
            }
        }
        var newLeft = me(exp[1], true);
        if (newLeft[0] === "lam") {
            var shr = (exp[2][0] == "shr") ? exp[2] : ["shr", false, exp[2]];
            shr = (shr[1] == true) ? shr[2] : shr;
            var tmp = subShared(newLeft[2], newLeft[1], shr);
            //var tmp = subShared(newLeft[2], newLeft[1], ["shr", false, exp[2]]);
            console.log("subShared returns: " + JSON.stringify(tmp));
            return me(tmp, waitLam);
            //return me(subShared(newLeft[2], newLeft[1], ["shr", false, exp[2]]), waitLam);
        } else {
            return ["app", newLeft, me(exp[2], false)];
        }
    }
    return me(exp, false);
}
function subShared(y, x, a) {
    console.log(`sub: y: ${y} ## x: ${x} ## a: ${a}`);
    if (y[0] == "var") {
        if (y[1] == x) { return a; } else { return y; }
    } else if (y[0] == "app") {
        return ["app", subShared(y[1], x, a), subShared(y[2], x, a)];
    } else if (y[0] == "lam") {
        if (y[1] == x) return y;
        var fvs = freeVars(a[2], new Set()); // ?
        if (fvs.has(y[1])) { // oops
            // \x (\u ...x...) (\y u)
            var newVar = nextVar(); // cheat
            return ["lam", newVar, subShared(sub(y[2], y[1], ["var", newVar]), x, a)];
        } else {
            return ["lam", y[1], subShared(y[2], x, a)];
        }
    } else if (y[0] == "shr") {
        var tmp = subShared(y[2], x, a);
        return (tmp[0] == "shr" && tmp[1] == true) ? tmp[2] : tmp;
        //return subShared(y[2], x, a);
    }
}

var reduceLeftmostOutermostTestData = [
    ["(λx x)", false, "(λx x)"],
    ["((λx x) (λx x))", true, "(λx x)"],
    ["(λx ((λx x) (λx x)))", true, "(λx (λx x))"],
    ];
for (let [expText, expectedreduceHappened, expectedText] of reduceLeftmostOutermostTestData) {
    var exp = parse(expText);
    var [actualReduceHappened, actualReducedExp] = reduceLeftmostOutermost(exp);
    console.assert(actualReduceHappened == expectedreduceHappened);
    var actualReducedExpText = unparse(actualReducedExp); 
    console.assert(actualReducedExpText == expectedText, `actualReducedExpText: ${actualReducedExpText}`);
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

var a = ["app",["lam","+1",["app",["lam","0",["app",["lam","1",["app",["lam","2",["app",["lam","3",["app",["lam","4",["app",["lam","5",["app",["lam","6",["app",["lam","7",["app",["lam","8",["app",["lam","9",["app",["lam","nil",["app",["lam","cons",["app",["lam","Y",["app",["lam","take",["app",["lam","map",["app",["lam","0-1-2-",["app",["app",["var","take"],["var","5"]],["var","0-1-2-"]]],["app",["var","Y"],["lam","0-1-2-",["app",["app",["var","cons"],["var","0"]],["app",["app",["var","map"],["var","+1"]],["var","0-1-2-"]]]]]]],["lam","f",["app",["var","Y"],["lam","go",["lam","ls",["app",["app",["var","ls"],["lam","a",["lam","as",["app",["app",["var","cons"],["app",["var","f"],["var","a"]]],["app",["var","go"],["var","as"]]]]]],["var","nil"]]]]]]]],["app",["var","Y"],["lam","take",["lam","n",["lam","ls",["app",["app",["var","n"],["lam","n-",["app",["app",["var","ls"],["lam","a",["lam","as",["app",["app",["var","cons"],["var","a"]],["app",["app",["var","take"],["var","n-"]],["var","as"]]]]]],["var","nil"]]]],["var","nil"]]]]]]]],["lam","f",["app",["lam","x",["app",["var","f"],["app",["var","x"],["var","x"]]]],["lam","x",["app",["var","f"],["app",["var","x"],["var","x"]]]]]]]],["lam","a",["lam","as",["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["var","a"]],["var","as"]]]]]]]],["lam","is-cons",["lam","is-nil",["var","is-nil"]]]]],["app",["var","+1"],["var","8"]]]],["app",["var","+1"],["var","7"]]]],["app",["var","+1"],["var","6"]]]],["app",["var","+1"],["var","5"]]]],["app",["var","+1"],["var","4"]]]],["app",["var","+1"],["var","3"]]]],["app",["var","+1"],["var","2"]]]],["app",["var","+1"],["var","1"]]]],["app",["var","+1"],["var","0"]]]],["lam","s",["lam","z",["var","z"]]]]],["lam","n",["lam","s",["lam","z",["app",["var","s"],["var","n"]]]]]]
;

var b = ["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["var","z"]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]]]]],["lam","is-cons",["lam","is-nil",["app",["app",["var","is-cons"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["app",["var","s"],["lam","s",["lam","z",["var","z"]]]]]]]]]]]]]]]],["lam","is-cons",["lam","is-nil",["var","is-nil"]]]]]]]]]]]]]]]]]]
;

var _rename = require('./rename.js'),
    rename = _rename.rename;
//var _gen = require('./gen.js'),
//    genNoDup = _gen.genNoDup;
//
var generated_3_6 = require('./generated_3_6.js').generated_3_6;

console.log("1111111111");



//for (let exp of genNoDup(["u", "v", "w"], 6)) {
for (let exp of generated_3_6) {
    //if (unparse(exp) == "((λ_0 (_0 _0)) (λ_1 (_1 _1)))") continue;
    //if (unparse(exp) == "(λ_0 ((λ_1 (_1 _1)) (λ_2 (_2 _2))))") continue;
    //if (unparse(exp) == "((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _1))))") continue;
    //if (unparse(exp) == "((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _1))))") continue;
    //if (unparse(exp) == "((λ_0 (_0 _0)) (λ_1 (_1 (_1 _1))))") continue;
    //if (unparse(exp) == "((λ_0 (_0 _0)) (λ_1 ((_1 _1) _1)))") continue;
    //if (unparse(exp) == "((λ_0 (λ_1 (_0 _0))) (λ_2 (_2 _2)))") continue;
    //if (unparse(exp) == "((λ_0 (_0 (_0 _0))) (λ_1 (_1 _1)))") continue;
    //if (unparse(exp) == "((λ_0 ((_0 _0) _0)) (λ_1 (_1 _1)))") continue;
    // needs rename:  ((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _2))))
    console.log(">>>>>>>>>>>>>>>>>>");
    console.log("exp: "  + JSON.stringify(exp));
    console.log("exp: "  + unparse(exp));
console.log("222222222222");
    //var x = JSON.stringify(reduceLeftmostOutermostTillDone(exp));
    //var x = JSON.stringify(rename(leftmostOutermost(exp)));
    var x = JSON.stringify(rename(leftmostOutermostShared(exp)));
console.log("3333333");
    var y = JSON.stringify(rename(normal_form(exp)));
console.log("4444444");

    if (x !== y) {
        console.log("x: " + x);
        console.log("y: " + y);
        console.assert(x === y);
    }

    console.log("<<<<<<<<<<<<<<<<<<");
   
  //  console.log("exp: "  + exp);
}
console.log("bye~~~~");


//var x = JSON.stringify(rename(normal_form(a)));
//var y = JSON.stringify(rename(b));
//
//var x = JSON.stringify(normal_form(a));
//var x = JSON.stringify(reduceLeftmostOutermostTillDone(a));
var x = JSON.stringify(leftmostOutermost(a));
var y = JSON.stringify(b);
console.assert(x == y);
console.log(x);

