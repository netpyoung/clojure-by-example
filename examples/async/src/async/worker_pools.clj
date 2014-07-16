(ns async.worker-pools)


;; https://gobyexample.com/worker-pools

;; (->> (async/to-chan [1 2 3 4])
;;      (async/map< identity )
;;      (async/into [] )
;;      (<!! )
;;      (println ))

(require '[clojure.core.async :as async :refer [<! >! <!! >!! timeout chan alt! go go-loop put! pipe close! map<]])

(defn worker [id jobs results]
  (go-loop []
    (when-let [j (<! jobs)]
      (println "worker" id, "processing job" j)
      (<! (timeout 3000))
      (>! results (* j 2))
      (recur))))

;; http://tgk.github.io/2013/10/inspect-core-async-channels.html
(def jobs* (chan 100))
(def results* (chan 100))

(dotimes [w 3]
  (worker w jobs* results*))

(dotimes [j 9]
  (>!! jobs* j))

(close! jobs*)

(->> results*
     (async/take 9)
     (async/into [])
     (<!!)
     (println "result:" ))




;>> worker 0 processing job 0
;>> worker 1 processing job 1
;>> worker 2 processing job 2
;>> worker 0 processing job 3
;>> worker 1 processing job 4
;>> worker 2 processing job 5
;>> worker 0 processing job 6
;>> worker 1 processing job 7
;>> worker 2 processing job 8
;=> nil


;>> result: [0 2 4 6 8 10 12 14 16]
