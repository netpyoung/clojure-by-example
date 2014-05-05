(ns common.string)


(require '[clojure.string :as s])


(some #{\e \s} "test")
;=> \e


(count (filter #{\t} "test"))
;=> 2



(re-find #"^te\w*" "test")
;=> "test"


(re-find #"\w*st$" "testx")
;=> "test"


(.indexOf (seq "test") \e)
;=> 1


(->> ["a" "b"]
     (interpose "-")
     (apply str))
;=> "a-b"


(repeat 5 "a")
;=> ("a" "a" "a" "a" "a")


(s/replace "foo" "o" "0")
;=> "f00"

(s/replace-first "foo" "o" "0")
;=> "f0o"


(s/split "a-b-c-d-e" #"-")
;=> ["a" "b" "c" "d" "e"]

(s/lower-case "TEST")
;=> "test"

(s/upper-case "test")
;=> "TEST"


(count "hello")
;=> 5

(nth "hello" 1)
;=> \e

(get "hello" 1)
;=> \e
