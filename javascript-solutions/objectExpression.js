// :NOTE: прототипы?
// :NOTE: парсер не обрабатывает все ошибки
let operand = {
    "+": Add,
    "-": Subtract,
    "/": Divide,
    "*": Multiply,
    "negate": Negate,
    "min3": Min3,
    "max5": Max5,
    "sinh": Sinh,
    "cosh": Cosh
}

let arrgs = {
    "+": 2,
    "-": 2,
    "/": 2,
    "*": 2,
    "negate": 1,
    "min3": 3,
    "max5": 5,
    "sinh": 1,
    "cosh": 1
}


let ConstPrototype = {
    prefix: function () {
        return this.name.toString();
    },
    toString: function () {
        return this.name.toString();
    },
    evaluate: function (x, y, z) {
        return parseFloat(this.name);
    }
}

function Const(a) {
    let cnst = Object.create(ConstPrototype)
    cnst.name = a;
    return cnst;
}
let VariablePrototype = {
    prefix: function () {
        return this.name.toString();
    },
    toString: function () {
        return this.name.toString();
    },
    evaluate: function (x, y, z) {
        return parseFloat(this.name === 'x' ? x : (this.name === 'y' ? y : z));
    }
}
function Variable(a) {
    let variable = Object.create(VariablePrototype)
    variable.name = a;
    return variable;
}

function Add(a, b) {
    return Object.create(operationVararg(function (a, b) {
        return a + b
    }, "+", a, b))
}

function Subtract(a, b) {
    return Object.create(operationVararg(function (a, b) {
        return a - b
    }, "-", a, b))
}

function Multiply(a, b) {
    return Object.create(operationVararg(function (a, b) {
        return a * b
    }, "*", a, b))
}

function Divide(a, b) {
    return Object.create(operationVararg(function (a, b) {
        return a / b
    }, "/", a, b))
}

function Negate(a) {
    return Object.create(operationVararg(function (a) {
        return -1 * a
    }, "negate", a))
}

function Sinh(a) {
    return Object.create(operationVararg(function (a) {
        return Math.sinh(a)
    }, "sinh", a))
}

function Cosh(a) {
    return Object.create(operationVararg(function (a) {
        return Math.cosh(
            a)
    }, "cosh", a))
}

function operationVararg(func, oper, ...arg) {
    return {
        prefix: function () {
            let array = [...arg];
            let string = "(" + oper + " ";
            for (let i = 0; i < array.length; i++) {
                string += (array[i].prefix());
                if (i !== array.length - 1)
                    string += " ";
            }
            return string + ")";
        },
        toString: function () {
            let array = [...arg];
            let string = "";
            for (let i = 0; i < array.length; i++) {
                string += (array[i] + " ")
            }
            return string + oper;
        },
        evaluate: function (x, y, z) {
            let array = [...arg]
            for (let i = 0; i < array.length; i++) {
                array[i] = parseFloat(array[i].evaluate(x, y, z));
            }
            return func(...array);
        }
    }
}

function Min3(...arg) {
    return Object.create(operationVararg(function (...arg) {
        return Math.min(...arg)
    }, "min3", ...arg))
}

function Max5(...arg) {
    return Object.create(operationVararg(function (...arg) {
        return Math.max(...arg)
    }, "max5", ...arg))
}

const parse = function (str) {
    let stack = [];
    let arr = str.trim().split(/\s+/);
    for (let i = 0; i < arr.length; i++) {
        if (!isNaN(parseFloat(arr[i]))) {
            stack.push(new Const(parseFloat(arr[i])));
        } else if (arr[i] === 'x' || arr[i] === 'y' || arr[i] === 'z') {
            stack.push(new Variable(arr[i]));
        } else if (arr[i] === "negate") {
            let a = stack.pop();
            stack.push(new Negate(a));
        } else {
            let a = stack.pop();
            let b = stack.pop();
            if (arr[i] === "min3") {
                let c = stack.pop();
                stack.push(new (operand[arr[i]])(c, b, a));
            } else if (arr[i] === "max5") {
                let c = stack.pop();
                let d = stack.pop();
                let e = stack.pop();
                stack.push(new (operand[arr[i]])(e, d, c, b, a));
            } else stack.push(new (operand[arr[i]])(b, a));
        }
    }
    return stack.pop();
}
let parsePrefix = function (string) {
    let global = 0;
    let a = []
    let balance = 0;
    while (global < string.length) {
        let ans = next(string);
        while (ans === "" && global < string.length)
            ans = next(string);
        //console.log(ans);
        if (ans !== "") {
            //console.log(ans);
            a.push(ans)
        }
    }

    function next(string) {
        let ans = "";
        for (let i = global; i < string.length; i++) {
            if ((string[i] === ")" || string[i] === "(") && ans.length !== 0) {
                return ans;
            } else if (string[i] === ")" || string[i] === "(") {
                global++;
                return string[i];
            } else if (string[i] !== " ") {
                ans += string[i];
                global++;
            } else {
                global++;
                return ans;
            }

        }
        return ans;
    }

    let pos = {s: 0}
    let stack = [];
    let operations = [];


    function recursive(pos) {
        while (pos.s < a.length) {
            if (a[pos.s] === '(') {
                balance++;
                pos.s++;
                recursive(pos);
            } else if (a[pos.s] === ')') {
                balance--;
                if (balance < 0) {
                    throw error("Brackets");
                }
                let number = arrgs[operations[operations.length - 1]];
                if (stack.length < number) {
                    throw error("Incorrect number of arguments");
                } else {
                    let args = [];
                    for (let i = 0; i < number; i++) {
                        args.push(stack.pop());
                    }
                    if (args[args.length - 1] instanceof Const || args[args.length - 1] instanceof Variable) {
                        throw error("No operation")
                    }
                    args.reverse();
                 //   function ew = new operand[operations.pop()];
                    stack.push(new (operand[operations.pop()])(...args))

                }
                return;
            } else if (a[pos.s] in operand) {
                operations.push(a[pos.s]);
            } else if (a[pos.s] === "x" || a[pos.s] === "y" || a[pos.s] === "z") {
                stack.push(new Variable(a[pos.s]));
            } else if (!isNaN(parseFloat(a[pos.s]))) {
                if (parseFloat(a[pos.s]).toString() !== a[pos.s]) {
                    throw error("Not a number");
                }
                stack.push(new Const(parseFloat(a[pos.s])));
            }
             else {
                throw error("Incorrect expression");
                // console.log(string);
            }
            pos.s++;
        }
    }

    recursive(pos);
    if (balance !== 0)
        throw error("Brackets");
    if ( operations.length !== 0) {
        throw error("Invalid number of operations");
    }
    if (stack.length !== 1 ) {
        throw error("Invalid number of arguments");
    }
    return stack.pop()
};
// console.log(parsePrefix("C(- 3 y)").evaluate(0, 0, 0))
//G_[]
//op_[+]
function error(string) {
    return string + " error"
}