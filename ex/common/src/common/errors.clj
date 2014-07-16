(ns common.errors)

;; https://gobyexample.com/panic
;; https://gobyexample.com/defer

;; http://docs.oracle.com/javase/7/docs/api/java/lang/Exception.html
;; http://clojuredocs.org/clojure_core/clojure.core/try
;; http://stackoverflow.com/questions/5459865/how-can-i-throw-an-exception-in-clojure


(defn myDivision [x y]
  (try
    (/ x y)
    (catch ArithmeticException e
      (println "Exception message: " (.getMessage e)))
    (finally
      (println "Done."))))

(myDivision 4 2)
;>> Done.
;=> 2


(myDivision 4 0)
;>> Exception message:  Divide by zero
;>> Done.
;=> nil
