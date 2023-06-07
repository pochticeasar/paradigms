"use strict";
const cnst = (value) => () => value;
const variable = (symbol) => (x, y, z) => symbol === 'x' ? x : (symbol === 'y' ? y : z);
const operation = (f) => (a, b) => (x, y, z) => f(a(x, y, z), b(x, y, z))
const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);
const negate = (a) => (x, y, z) => (-a(x, y, z));

let pi = cnst(Math.PI);
let e = cnst(Math.E);
const parse = function (str) {
    let stack = [];
    let arr = str.trim().split(/\s+/);
    for (let i = 0; i < arr.length; i++) {
        if (!isNaN(parseFloat(arr[i]))) {
            stack.push(cnst(parseFloat(arr[i])));
        } else if (arr[i] === 'x' || arr[i] === 'y' || arr[i] === 'z') {
            stack.push(variable(arr[i]));
        } else {
            let a = stack.pop();
            let b = stack.pop();
            if (arr[i] === '+')
                stack.push(add(b, a));
            else if (arr[i] === '-')
                stack.push(subtract(b, a));
            else if (arr[i] === '*')
                stack.push(multiply(b, a));
            else
                stack.push(divide(b, a));
        }
    }
    return stack.pop();
}

