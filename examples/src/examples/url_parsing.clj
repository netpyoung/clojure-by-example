(ns examples.url-parsing)

;; https://gobyexample.com/url-parsing
;; https://github.com/wtetzner/exploding-fish


;; http://docs.oracle.com/javase/7/docs/api/java/net/URI.html

;; http://en.wikipedia.org/wiki/URI_scheme

;; https://github.com/cemerick/url
(require '[cemerick.url :as url])



;;
;;
(require '[org.bovinegenius [exploding-fish :as uri]])

(def s "postgres://user:pass@host.com:5432/path?k=v#f")
(def u (uri/uri s))

(:scheme u)
;=> "postgres"

(:user-info u)
;=> "user:pass"


(:host u)
;=> "host.com"

(:port u)
;=> 5432

(:path u)
;=> "/path"

(:fragment u)
;=> "f"

(:query u)
;=> "k=v"


(->> "k"
     (get (->> (uri/query-pairs u)
               (into {}))))
;=> "v"
