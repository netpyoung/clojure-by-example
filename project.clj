(defproject clojure-by-example "0.1.0-SNAPSHOT"

  :dependencies
  [[org.clojure/clojure "1.6.0"]

   ;; FileSystem.
   [me.raynes/fs "1.4.6"]

   ;; Documentation.
   [marginalia "0.8.0-SNAPSHOT"]

   ;; Encoding.
   [org.clojure/tools.reader "0.8.5"]
   [selmer "0.6.8"]
   [org.commonjava.googlecode.markdown4j/markdown4j "2.2-cj-1.0"]

   [org.clojure/tools.namespace "0.2.5"]
   ]

  :plugins [[jonase/eastwood "0.1.4"]]
  :clean-targets [:target-path "public"]

  :main clojure-by-example.core)
