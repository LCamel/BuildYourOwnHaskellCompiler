var nextVar = 0;
function graphviz(exp, buf) {
    if (exp[0] === "var") {
        var name = "" + nextVar++;
        buf.push(`${name} [label="${exp[1]}", shape=box];`);
        return name;
    }
    if (exp[0] === "lam") {
        var name = "" + nextVar++;
        buf.push(`${name} [label="lam"];`);

        var name1 = "" + nextVar++;
        buf.push(`${name1} [label="${exp[1]}"];`);
        buf.push(`${name} -> ${name1};`);

        var name2 = graphviz(exp[2], buf);
        buf.push(`${name} -> ${name2};`);

        return name;
    }
    if (exp[0] === "app") {
        var name = "" + nextVar++;
        buf.push(`${name} [label="app"];`);

        var name1 = graphviz(exp[1], buf);
        buf.push(`${name} -> ${name1};`);

        var name2 = graphviz(exp[2], buf);
        buf.push(`${name} -> ${name2};`);

        return name;
    }
}

//var _parse = require('./parse.js'),
//    parse = _parse.parse;
//var exp = parse(' (λ_0 (((λ_1 (λ_2 _1)) _0) _0))');
var exp = ["lam","_0",["app",["app",["lam","_1",["lam","_2",["var","_1"]]],["var","_0"]],["var","_0"]]];


var buf = [];
graphviz(exp, buf);

var s = `digraph foo {
${buf.join("\n")}
}`;
console.log(s);
