(defn binary [x y func] (fn [args] (func (x args) (y args))))

(defn unary [x func] (fn [args] (func (x args))))
(defn constant [x] (fn [args] x))

(defn variable [x] (fn [args] (args x)))

(defn add [x y] (binary x y +))
(defn subtract [x y] (binary x y -))
(defn multiply [x y] (binary x y *))
(defn divide [x y] (fn [args] (/ (double (x args)) (double (y args)))))
(defn negate [x] (unary x -))
(defn exp [x] (unary x #(Math/exp %)))
(defn ln [x] (unary x #(Math/log %)))
(def operation {
                '+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'exp exp, 'ln ln
                })
(defn parseFunction [expression]
  (cond
    (string? expression) (parseFunction (read-string expression))
    (number? expression) (constant expression)
    (symbol? expression) (variable (str expression))
    (contains? operation (first expression)) (apply (operation (first expression)) (mapv parseFunction (rest expression)))
    )
  )
(defn proto-get
  ([obj key] (proto-get obj key nil))
  ([obj key default]
   (cond
     (contains? obj key) (obj key)
     (contains? obj :prototype) (proto-get (obj :prototype) key default)
     :else default)))
(defn proto-call
  [this key & args]
  (apply (proto-get this key) this args))
(defn constructor
  [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))
(defn field [key]
  (fn
    ([this] (proto-get this key))
    ([this def] (proto-get this key def))))
(defn method
  [key] (fn [this & args] (apply proto-call this key args)))
(def evaluate (method :evaluate))

(def toString (method :toString))

(def diff (method :diff))

(defn Prototype [func op l r rule] {
                                    :evaluate (fn [this args] (func (double (evaluate l args)) (double (evaluate r args))))
                                    :toString (fn [this] (str "(" op " " (toString l) " " (toString r) ")"))
                                    :diff     rule})
(defn Constant [x]
  {:evaluate (fn [this args] x)
   :toString (fn [args]
               (str x))
   :diff     (fn [this vari]
               (Constant 0.0))})

(defn Variable [x]
  {:evaluate (fn [this args]
               (args x))
   :toString (fn [args]
               (str x))
   :diff     (fn [this vari]
               (if (= vari x) (Constant 1.0) (Constant 0.0)))})


(defn Add [x y] (Prototype + '+ x y (fn [this vari]
                                      (Add (diff x vari) (diff y vari)))))
(defn Subtract [x y] (Prototype - '- x y (fn [this vari]
                                           (Subtract (diff x vari) (diff y vari)))))

(defn Multiply [x y]
  (Prototype * '* x y (fn [this vari]
                        (Add (Multiply (diff x vari) y) (Multiply x (diff y vari))))))
(defn Divide [x y]
  (Prototype (fn [l r] (/ (double l) (double r)))
             '/ x y (fn [this vari]
                      (Divide (Subtract (Multiply (diff x vari) y) (Multiply x (diff y vari))) (Multiply y y)))))
(defn UnaryPrototype [func op l rule] {
                                       :evaluate (fn [this args] (func (double (evaluate l args))))
                                       :toString (fn [this] (str "(" op " " (toString l) ")"))
                                       :diff     rule})
(defn Negate [x] (UnaryPrototype - 'negate x (fn [this vari]
                                               (Negate (diff x vari)))))
(defn Ln [x] (UnaryPrototype #(Math/log %) 'ln x (fn [this vari]
                                                   (Multiply (Divide (Constant 1.0) x) (diff x vari)))))
(defn Exp [x] (UnaryPrototype #(Math/exp %) 'exp x (fn [this vari]
                                                     (Multiply (Exp x) (diff x vari)))))
(def Operation {
                '+ Add, '- Subtract, '* Multiply, '/ Divide 'negate Negate 'ln Ln 'exp Exp
                })
(defn parseObject [Object]
  (cond
    (string? Object) (parseObject (read-string Object))
    (number? Object) (Constant Object)
    (symbol? Object) (Variable (str Object))
    (contains? Operation (first Object)) (apply (Operation (first Object)) (mapv parseObject (rest Object)))
    )
  )
;(println(toString (diff  (Constant 10.0) "x")))

;(println (Add (Variable "x") (Constant 2.0)))