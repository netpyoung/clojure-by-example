(ns examples.switch)



(def i 2)
(println "write " i " as ")
;>> write  2  as
;=> nil

(case i
  1 (println "one")
  2 (println "two")
  3 (println "three")
  nil)
;>> two
;=> nil


;; http://www.java2s.com/Code/JavaAPI/java.util/CalendarSUNDAY.htm

(import [java.util Calendar Date])

(case (-> (Date.) bean :day)

  #{Calendar/SATURDAY Calendar/SUNDAY}
  (println "it's the weekend")

  (println "it's a weekday"))
;>> it's a weekday
;=> nil


(cond
 (< (-> (Date.) bean :hours) 12)
 (println "it's before noon")

 (println "it's after noon"))


;; https://github.com/clj-time/clj-time
