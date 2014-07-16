(ns clojure-by-example.util
  (:import
   [org.markdown4j Markdown4jProcessor])
  (:require
   [clojure.tools.reader.edn :as edn]
   [clojure.string :as string]
   [me.raynes.fs :as fs]))


(let [md* (Markdown4jProcessor.)]
  (defn md
    [markdown-str]

    (.process md* markdown-str)))


(defn ns->cljfname
  "
  >> (ns->cljfname :hello.clojure-world)
  ;=> \"hello/clojure_world.clj\"
  "
  [namespace]

  (-> (name namespace)
      (string/replace "." "/")
      (string/replace "-" "_")
      (str ".clj")))


(defn ns->basename
  "
  >> (ns->basename :hello.clojure-world)
  ;=> \"clojure_world\"
  "
  [namespace]

  (-> (name namespace)
      (string/replace "." "/")
      (string/replace "-" "_")
      (fs/base-name)))


(defn ns->title
  "
  >> (ns->title :hello.clojure-world)
  ;=> \"Clojure World\"
  "
  [namespace]

  (-> (name namespace)
      (string/split #"\.")
      (last)
      (string/split #"-")
      (->> (map string/capitalize)
           (interpose " ")
           (apply str))))


(defn ns->parent-dir
  [namespace]

  (-> namespace
      (string/split #"\.")
      (first)))


(defn ensure-dir!
  [target-dir]

  (or (fs/exists? target-dir)
      (fs/mkdirs target-dir)
      (throw (Exception. (str "[ERROR] couldn't create target dir!" target-dir)))))
