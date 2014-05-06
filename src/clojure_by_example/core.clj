(ns clojure-by-example.core
  (:gen-class)
  (:require [clojure.string :as string]
            [marginalia.core :as marginalia]
            [me.raynes.fs :as fs]
            [clojure.tools.reader.edn :as edn]
            [clojure-by-example.page :as page]
            ))


(def info-fname* "examples.edn")
(def example-base-dir* "examples/src")
(def public-base-dir* "public")


(defn clj->html [in-clj out-html]
  (let [parent-dir (fs/parent out-html)]
    (when-not (fs/exists? parent-dir)
      (when-not (fs/mkdirs parent-dir)
        (throw (Exception.
                (str "[ERROR] couldn't create output dir!" [in-clj out-html]))))))

  (let [basename (fs/base-name in-clj true)
        doc (marginalia/path-to-doc in-clj)]

    (->> (page/example-page basename doc)
         (spit out-html))))


(defn raw-info->ns-info [info-dic]
  (->> info-dic
       (map (fn [[k v]]
              [k (->> v
                      (map #(str (name k) "." (name %))))]))
       (into {})))


(defn ns->in-clj-path [namespace]
  (->> namespace
       (str example-base-dir* "/")
       (fs/ns-path)
       (str)))


(defn ns->out-html-path [namespace]
  (let [v (str public-base-dir* "/" namespace)]
    (str (fs/parent (fs/ns-path v)) "/"
         (fs/base-name (fs/ns-path v) true) ".html")))


(defn ns-sym->basename
  "
  >> (munge-ns :hello.clojure-world)
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


(defn -main []
  (let [ns-info
        (->> info-fname*
             (slurp)
             (edn/read-string)
             (raw-info->ns-info))

        clj-html-paths
        (->> ns-info
             (vals)
             (flatten)
             (map (fn [namespace]
                    [(ns->in-clj-path namespace)
                     (ns->out-html-path namespace)])))]

    ;; make example-files
    (doseq [[in out] clj-html-paths]
      (clj->html in out))))
