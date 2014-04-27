(ns examples.variables)


;; `def` declares 1 variables.
(def a "initial")
(println a)

(def d)
;=> #'examples.variables/d

(println d)
;>> #<Unbound Unbound: #'examples.variables/d>
;=> nil
