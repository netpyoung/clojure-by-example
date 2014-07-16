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

    (->> markdown-str (.process md*))))


(defn ns-sym->cljfname
  "
  >> (ns-sym->cljfname :hello.clojure-world)
  ;=> \"hello/clojure_world.clj\"
  "
  [ns-sym]

  (-> (name ns-sym)
      (string/replace "." "/")
      (string/replace "-" "_")
      (str ".clj")))


(defn ns-sym->basename
  "
  >> (ns-sym->basename :hello.clojure-world)
  ;=> \"clojure_world\"
  "
  [ns-sym]

  (-> (name ns-sym)
      (string/replace "." "/")
      (string/replace "-" "_")
      (fs/base-name)))


(defn ns-sym->title
  "
  >> (ns-sym->title :hello.clojure-world)
  ;=> \"Clojure World\"
  "
  [ns-sym]

  (->> (-> (name ns-sym)
           (string/split #"\.")
           (last)
           (string/split #"-"))
       (map string/capitalize)
       (interpose " ")
       (apply str)))


(defn ns->parent-dir
  [namespace]

  (-> (str namespace)
      (string/split #"\.")
      (first)))



(defn ensure-dir!
  [target-dir]

  (or (fs/exists? target-dir)
      (fs/mkdirs target-dir)
      (throw (Exception. (str "[ERROR] couldn't create target dir!" target-dir)))))


(defn- raw-info->ns-info
  "
  (raw-info->ns-info {:a [:b-c]})
  ;=> {:a (\"a.b-c\")}
  "
  [info-dic]

  (->> info-dic
       (map (fn [[k v]]
              [k (map #(str (name k) "." (name %)) v)]))
       (into {})))


(defn get-ns-info
  [fname]

  (->> fname
       (slurp)
       (edn/read-string)
       (raw-info->ns-info)))
