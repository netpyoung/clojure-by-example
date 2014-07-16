(ns common.closures)


(defn int-seq []
  (let [i (atom 0)]
    (fn []
      (swap! i inc))))

(def next-int (int-seq))

(next-int)
;=> 1

(next-int)
;=> 2

(next-int)
;=> 3


(def next-int (int-seq))

(next-int)
;=> 1
