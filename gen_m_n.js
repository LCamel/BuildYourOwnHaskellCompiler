"use strict";
var _gen = require('./gen.js'),
    genNoDup = _gen.genNoDup;
var _parse = require('./parse.js'),
    unparse = _parse.unparse;

const varCount = 3;
const depth = 6;
const name = `gen_${varCount}_${depth}`;
var vars = [];
for (var i = 0; i < varCount; i++) { vars.push("a" + i); }


console.log(`var ${name}  = [`);


for (let exp of genNoDup(vars, 6)) {
    const ue = unparse(exp)    
    if (ue == "((λ_0 (_0 _0)) (λ_1 (_1 _1)))") continue;
    if (ue == "(λ_0 ((λ_1 (_1 _1)) (λ_2 (_2 _2))))") continue;
    if (ue == "((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _1))))") continue;
    if (ue == "((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _1))))") continue;
    if (ue == "((λ_0 (_0 _0)) (λ_1 (_1 (_1 _1))))") continue;
    if (ue == "((λ_0 (_0 _0)) (λ_1 ((_1 _1) _1)))") continue;
    if (ue == "((λ_0 (λ_1 (_0 _0))) (λ_2 (_2 _2)))") continue;
    if (ue == "((λ_0 (_0 (_0 _0))) (λ_1 (_1 _1)))") continue;
    if (ue == "((λ_0 ((_0 _0) _0)) (λ_1 (_1 _1)))") continue;
    // needs rename:  ((λ_0 (_0 _0)) (λ_1 (λ_2 (_1 _2))))
    console.log("    "  + JSON.stringify(exp) + ",");
}
console.log("];");
console.log(`exports.${name} = ${name};`);
