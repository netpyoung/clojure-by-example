(ns common.variables)


;; `def` declares 1 variables.
(def a "initial")
(println a)

(def d)
;=> #'common.variables/d

(println d)
;>> #<Unbound Unbound: #'common.variables/d>
;=> nil
