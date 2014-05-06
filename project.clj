(defproject clojure-by-example "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; for filesystem.
                 [me.raynes/fs "1.4.4"]

                 ;; for doc.
                 [marginalia "0.8.0-SNAPSHOT"]

                 ;; for encoding.
                 [org.clojure/tools.reader "0.8.4"]
                 [hiccup "1.0.5"]
                 [stencil "0.3.3"]
                 [org.commonjava.googlecode.markdown4j/markdown4j "2.2-cj-1.0"]

                 ]
  :clean-targets [:target-path "public"]
  :main clojure-by-example.core)
