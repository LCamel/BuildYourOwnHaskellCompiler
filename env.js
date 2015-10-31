"use strict";

var generated_3_6_normal = JSON.parse(require("fs").readFileSync("generated_3_6_normal.json"));

console.log(JSON.stringify(generated_3_6_normal[14]));

class Env {
   constructor(outer, k, v) {
       this.outer = outer;
       this.k = k;
       this.v = v;
   }
   get(key) {
       //return (key == this.k) ? this.v : this.outer.get(key);
       //console.log("get: key : " + key + " this.k: " + this.k + ((key === this.k) ? "eq" : "ne"));
       return (key === this.k) ? this.v : (this.outer ? this.outer.get(key) : undefined);
   }
   //static empty() { return new Env(new Map()); }
   static show(e) {
//       return e ? JSON.stringify(e.k) + "->" + JSON.stringify(e.v) + " / " + Env.show(e.outer): "END";
       return e ? `
_0 -> ${JSON.stringify(e.get("_0"))}
_1 -> ${JSON.stringify(e.get("_1"))}
_2 -> ${JSON.stringify(e.get("_2"))}
` : "xxx e is undefined xxx";
   }
   static then(e1, e2) {
       return { "get": (x) => (e1 ? e1.get(x) : undefined) || (e2 ? e2.get(x) : undefined) };
   }
}
/*
var a = new Env(undefined, "a", 1);
var b = new Env(a, "b", 2);
var c = new Env(undefined, "c", 3);
console.log(a.get("a"));
console.log(a.get("b"));
console.log(b.get("a"));
console.log(b.get("b"));
console.log(b.get("c"));
console.log("=====");
var bc = Env.then(b, c);
console.log(bc.get("a"));
console.log(bc.get("b"));
console.log(bc.get("c"));
process.exit(0);
*/

/*
function beta(lamExp, exp) {
    var oldEnv = lamExp["env"];
    var newEnv = new Env(oldEnv, lamExp[1], exp);
    // mutable ?!!?
    var tmp = lamExp[2];
    tmp["env"] = newEnv;
    return tmp;
}
*/
// sub.js:        return bound.has(exp[1]) ? new Set() : new Set([exp[1]]);
// sub.js:        return new Set([...freeVars(exp[1], bound), ...freeVars(exp[2], bound)]);
function leftmost(exp, waitLam, env, bound) {
    if (exp["env"]) { env = Env.then(exp["env"], env); }
    console.log("####entering leftmost: exp: " + unparse(exp));
    console.log("    " + JSON.stringify(exp));
    console.log("    env show: " + Env.show(env));

    if (exp[0] === "var") {
        if (bound.has(exp[1])) {
            return exp;
        }
        console.log("var: going to get: " + JSON.stringify(exp[1]));
        var tmp = env.get(exp[1]);
        console.log("var: env.get returns: " + JSON.stringify(tmp));
        return leftmost(tmp, waitLam, env, bound);
    }
    if (exp[0] === "lam") {
        //return waitLam ? exp : [exp[0], exp[1], leftmost(exp[2], false)];
        if (waitLam) {
            console.log("~~ quick return ~~");
            return exp;
        } else {
            return [exp[0], exp[1], leftmost(exp[2], false, env, new Set([exp[1], ...bound]))];
        }
    }
    if (exp[0] === "app") {
        var newLeft = leftmost(exp[1], true, env, bound);
        console.log("newLeft returned: " + unparse(newLeft));
        console.log(" and it has env : " + Env.show(newLeft["env"]));

        if (newLeft[0] === "lam") {
            console.log("~~ lam under app returned ~~");
            exp[2]["env"] = env;
            var newEnv = new Env(undefined, newLeft[1], exp[2]);
            console.log("0newEnv: " + Env.show(newEnv));
            //newEnv = Env.then(newEnv, newLeft["env"]);
            //newEnv = Env.then(newEnv, exp[2]["env"]);
            newEnv = Env.then(newEnv, env);
            //newEnv = Env.then(exp[2]["env"], newEnv);
            console.log("1newEnv: " + Env.show(newEnv));
            //var newEnv = Env.then(newLeft[2]["env"], newLeft["env"])
            //var newEnv = Env.then(newLeft[2]["env"], undefined);
            //newEnv = Env.then(newEnv, new Env(undefined, newLeft[1], exp[2]));
            
            //newEnv = Env.then(newLeft["env"], newEnv)
            console.log("newEnv: " + Env.show(newEnv));

            //return leftmost(newLeft[2], waitLam, newEnv, bound);
            //return leftmost(beta(newLeft, exp[2]), waitLam);
            //return withEnv(newLeft[2], new Env(newLeft[2]["env"], new Env(undefined, newLeft[1], exp[2])));
            // better return leftmost(withEnv(newLeft[2], newEnv), waitLam, newEnv, bound);
            return leftmost(withEnv(newLeft[2], newEnv), waitLam, env, bound);
        } else {
            return ["app", newLeft, leftmost(exp[2], false, env, bound)];
        }
    }
    1 / 0;
}
function withEnv(exp, env) {
    var tmp;
    if (exp[0] === "var") {
        tmp = [exp[0], exp[1]];
    } else if (exp[0] === "lam") {
        tmp = [exp[0], exp[1], exp[2]];
    } else if (exp[0] === "app") {
        tmp = [exp[0], exp[1], exp[2]];
    }
    tmp["env"] = env;
    return tmp;
}

var _parse = require("./parse.js"),
    parse = _parse.parse, unparse = _parse.unparse;
var _rename = require("./rename.js"),
    rename = _rename.rename;
//var exp = parse("((λx (λy (x y)))(λx x))");
//var exp = parse("((λx (((x x) (x x)) ((x x) (x x)))) (λu u))");
var exp = parse("((λx (x x)) (λu u))");
//var dummyEnv = { "get": (x) => ["var", x] };
var dummyEnv = new Env(undefined, "dummy", "dummy");
//exp["env"] = dummyEnv;
//exp[1]["env"] = dummyEnv;
//exp[2]["env"] = dummyEnv;
console.log(unparse(leftmost(exp, false, undefined, new Set())));

for (let [exp, normal] of generated_3_6_normal) {
    console.log("================================================");
    console.log("exp: "  + JSON.stringify(exp));
    console.log("exp: "  + unparse(exp));
//    exp["env"] = dummyEnv;
//    var x = JSON.stringify(rename(leftmost(exp, false, undefined, new Set())));
    var x0 = leftmost(exp, false, undefined, new Set());
    var x = JSON.stringify(rename(x0));
    var y = JSON.stringify(normal);
    if (x !== y) {
        console.log("x0: " + JSON.stringify(x0));
        console.log("x: " + x);
        console.log("y: " + y);
        console.assert(x === y);
    }
}
