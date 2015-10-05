"use strict";
// x      -> ["var", "x"]
// (\x M) -> ["lam", "x", M]
// (M N)  -> ["app", M, N]

function tokens(line) {
    return line.replace(/\(/g, ' ( ').replace(/\)/g, ' ) ').trim().split(/\s+/);
}
function parseOne(toks) {
    var tok = toks.shift();
    if (tok == '(') {
        var tmp = /^[\\λ\/]/.test(toks[0])
	    ? ["lam", toks.shift().substr(1), parseOne(toks)]
	    : ["app", parseOne(toks),         parseOne(toks)];
	if (toks.shift() != ')') { throw "no matching ')'"; }
	return tmp;
    } else if (tok == ')') {
        throw "bad ')'";
    } else {
        return ["var", tok];
    }
}
function parse(line) { return parseOne(tokens(line)); }

function unparse(exp) {
    if (exp[0] == "var") return exp[1];
    if (exp[0] == "lam") return `(λ${exp[1]} ${unparse(exp[2])})`;
    return `(${unparse(exp[1])} ${unparse(exp[2])})`;
}

// TODO: es6 import
exports.parse = parse;
exports.unparse = unparse;

/*
function show(line) { console.log(JSON.stringify(parse(line))); }
show("x");
show("(\\x y)");
show("((\\y z)(\\x y))");
show("((λy z)(λx y))");
show("((/y z)(/x y))");
console.log(unparse(parse("((/y z)(/x y))")));
*/
