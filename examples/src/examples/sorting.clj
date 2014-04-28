(ns examples.sorting)


;; https://gobyexample.com/sorting

(def strs ["c" "a" "b"])

(println strs)

(def ints [7 2 4])

(sort ints)
;=> (2 4 7)


;; https://gobyexample.com/sorting-by-functions
(->> ["peach", "banana", "kiwi"]
     (sort (fn [a b]
             (compare (count a) (count b)))))
;=> ("kiwi" "peach" "banana")


;; http://clojuredocs.org/clojure_core/clojure.core/sort-by
(->> ["peach", "banana", "kiwi"]
     (sort-by count))
;=> ("kiwi" "peach" "banana")
