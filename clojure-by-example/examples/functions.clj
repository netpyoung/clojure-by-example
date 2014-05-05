(ns examples.functions
  "ns comment")

(defn plus
  "function comment 한글주석."
  [a b]
  (+ a b))

(def ret (plus 1 2))



(println "1+2 =" ret)
;; __3__

;; **7**

; 1

;; 2

;;; 3

;;   hello
;;   world
;;   (inc 1)


;; => nil




(defn ret-vals []
  [3 7])


(let [[a b] (ret-vals)]
  (println a)
  (println b))


(let [[_ c] (ret-vals)]
  (println c))
;; >> 7
;; => nil


(defn sum [& nums]
  (print nums "")
  (println (reduce + nums)))

(sum 1 2)
(chan)
(comment "helloworld")
;>> (1 2) 3
;=> nil


;; # 이런이런.
;; * hello world.
;; 여기서 다음은 다음과 같다.

;; ```clojure
;; (defn hello
;;   []
;;   (+ 1 1))
;; ; ;>> hello
;; ; ;=> world
;; ```

(sum 1 2 3)
;>> (1 2 3) 6
;=> nil

(apply sum [1 2 3 4])
;>> (1 2 3 4) 10
;=> nil
