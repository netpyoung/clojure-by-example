(ns common.codec)

;; https://gobyexample.com/base64-encoding

;; https://github.com/clojure/data.codec


(require '[clojure.data.codec.base64 :as b64])

(->> "abc123!?$*&()'-=@~"
     (map byte)
     (byte-array)
     (b64/encode)
     (String.))
;=> "YWJjMTIzIT8kKiYoKSctPUB+"


(->> "YWJjMTIzIT8kKiYoKSctPUB+"
     (map byte)
     (byte-array)
     (b64/decode)
     (String.))
;=> "abc123!?$*&()'-=@~"
