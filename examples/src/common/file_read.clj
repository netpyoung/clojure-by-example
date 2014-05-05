(ns common.file-read)

;; https://gobyexample.com/reading-files
;; https://gobyexample.com/writing-files
;; https://gobyexample.com/line-filters


;; http://stackoverflow.com/questions/7756909/in-clojure-1-3-how-to-read-and-write-a-file

;; http://copperthoughts.com/p/clojure-io-p1/


(slurp "resources/test.txt")
;=> "hello\nclojure\n"

(use 'clojure.java.io)
(with-open [rdr (reader "resources/test.txt")]
  (doseq [line (line-seq rdr)]
    (println line)))
;>> hello
;>> clojure
;=> nil
