(ns examples.digest)

(require 'digest)
;; https://gobyexample.com/sha1-hashes

;; https://github.com/tebeka/clj-digest
;; http://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html

(def s "sha1 this string")
(digest/sha1 s)
;=> "cf23df2207d99a74fbe169e3eba035e633b65d94"


(digest/md5 "hello world")
;=> "5eb63bbbe01eeed093cb22bb8f5acdc3"


(digest/sha-256 "hello world")
;=> "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"


(digest/sha1 "hello world")
;=> "2aae6c35c94fcfb415dbe95f408b9ce91ee846ed"
