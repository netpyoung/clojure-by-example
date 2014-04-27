(ns examples.functions)

(defn plus [a b]
  (+ a b))

(def ret (plus 1 2))

(println "1+2 =" ret)
;>> 1+2 = 3
;=> nil


(defn ret-vals []
  [3 7])


(let [[a b] (ret-vals)]
  (println a)
  (println b))
;>> 3
;>> 7
;=> nil


(let [[_ c] (ret-vals)]
  (println c))
;>> 7
;=> nil


(defn sum [& nums]
  (print nums "")
  (println (reduce + nums)))

(sum 1 2)
;>> (1 2) 3
;=> nil

(sum 1 2 3)
;>> (1 2 3) 6
;=> nil

(apply sum [1 2 3 4])
;>> (1 2 3 4) 10
;=> nil
