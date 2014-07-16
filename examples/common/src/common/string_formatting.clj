(ns common.string-formatting)

;; https://gobyexample.com/string-formatting


(require '[clojure.pprint :as pp])
(defrecord Point [x y])

(def p (Point. 1 2))

(println p)
;>> #common.string_formatting.Point{:x 1, :y 2}
;=> nil

(printf p)


;; http://clojuredocs.org/clojure_core/clojure.pprint/pprint
(pp/pprint p)
;>> {:x 1, :y 2}
;=> nil


;; https://github.com/clojure/core.incubator
(require '[clojure.core.strint :as strint])

(strint/<< "p is ~{(pr-str p)}")
;=> "p is #common.string_formatting.Point{:x 1, :y 2}"



(let [w (java.io.StringWriter.)]
  (pp/pprint p w)
  (.toString w))

;=> "{:x 1, :y 2}\n"





;; http://clojuredocs.org/clojure_core/clojure.pprint/cl-format
