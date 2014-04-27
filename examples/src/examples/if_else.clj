(ns examples.if-else)
;; switch.clj


(if (= (rem 7 2) 0)
  (println "7 is even")
  (println "7 is odd"))


;; You can have an if statement without an else.
(if (zero? (rem 8 2))
  (println "8 is divisible by 4"))


(when (zero? (rem 8 2))
  (println "8 is divisible by 4"))



;; A statement can precede conditionals; any variables declared in this statement are available in all branches.

(def num 9)
(cond (neg? num)
      (println num "is negative")

      (< num 10)
      (println num "has 1 digit")

      :else
      (println num "has multiple digits"))
;>> 9 has 1 digit
;=> nil
