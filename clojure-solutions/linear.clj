(defn operation [oper] (fn[l r] (mapv oper l r)))
(defn product [oper] (fn [l r] (mapv oper (vec (repeat (count l) r)) l)))
(def v+ (operation +))
(def v- (operation -))
(def v* (operation *))
(def vd (operation /))

(defn scalar [l, r] (reduce + (v* l r)))

(def v*s (product *))
(def m+ (operation v+))
(def m- (operation v-))
(def m* (operation v*))
(def md (operation vd))

(defn vect [l r] (vector (- (* (nth l 1) (nth r 2)) (* (nth l 2) (nth r 1)))
                         (- (* (nth l 2) (nth r 0)) (* (nth l 0) (nth r 2)))
                         (- (* (nth l 0) (nth r 1)) (* (nth l 1) (nth r 0)))))

(defn transpose [m] (apply mapv vector m))

(def m*v (product scalar))

(defn m*m [l r] (mapv (partial m*v (transpose r)) l))
(defn m*s [l r] (mapv (fn [v] (v*s v r)) l))

(def c+ (operation m+))
(def c- (operation m-))
(def c* (operation m*))
(def cd (operation md))