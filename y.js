"use strict";
console.log(
  (g => g(g))
  (
  f => x => (x == 0 || x == 1 ? 1 : f(f)(x - 1) + f(f)(x - 2))
  )(6)
);
//  f => x => y => (x == 0 ? y : f(f)(x-1)(y+1)) // add
/*
console.log(
  (f => x => y => f(f)(x)(y))
  (
  f => x => y => (x == 0 ? y : f(f)(x-1)(y+1))
  )(2)(3)
  
);
*/
/*
console.log(
    (f => n => f(f)(n))
    (  f => n => (n == 0 ? 0 : n + f(f)(n - 1))   ) ( 5)
);
*/
/*
console.log(
    function (f) {
         return function (n) {
           return f(f)(n);
	}
    } ( 
        function (f) {
	      return function (n) {
	           return n == 0 ? 0 : n + f(f)(n - 1); }
	      }
	      )(5)

);
*/
/*
console.log(
    function (f) {
         return function (n) {
           return f(f)(n);
	}
    } ( 
        function (f) {
	      return function (n) {
	           return n == 0 ? 0 : n + f(f)(n - 1); }
	      }
	      )(5)

);
*/
/*
console.log(
    function (f, n) {
        //return n == 0 ? 0 : n + f(f, n - 1);
        return f(f)(n);
    } ( function (f) { return function (n) { return n == 0 ? 0 : n + f(f)(n - 1); } }, 5)

);
*/

/*

console.log(
    function (f, n) {
        //return n == 0 ? 0 : n + f(f, n - 1);
        return f(f, n);
    } ( function (f, n) { return n == 0 ? 0 : n + f(f, n - 1); }, 5)

);

*/
