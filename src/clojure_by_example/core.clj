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


(defn ns->in-clj-path
  "
  >> (ns->in-clj-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/examples/src/hello/a_b.clj\"
  "
  [ns-sym]

  (->> (name ns-sym)
       (str example-base-dir* "/")
       (fs/ns-path)
       (str)))



(defn ns->out-html-path
  "
  >> (ns->out-html-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/public/hello/a_b.html\"
  "
  [ns-sym]

  (let [v (str public-base-dir* "/" (name ns-sym))]
    (str (fs/parent (fs/ns-path v)) "/"
         (fs/base-name (fs/ns-path v) true) ".html")))


(defn -main []
  (let [ns-info-dic (->> info-fname*
                         (slurp)
                         (edn/read-string)
                         (raw-info->ns-info))]

    ;; main page.
    (->> (page/main-page ns-info-dic)
         (spit "index.html"))

    ;; main index page.
    (->> (page/main-index-page ns-info-dic)
         (spit (str public-base-dir* "/" "index.html")))

    ;; example pages.
    (doseq [[section sub-page-namespaces :as ns-info] ns-info-dic]
      (->> (page/section-index-page ns-info)
           (spit (str public-base-dir* "/" (name section) "/index.html")))
      ;;   (doseq [[in out] (->> sub-page-namespaces
      ;;                         (map (fn [namespace]
      ;;                                [(ns->in-clj-path namespace)
      ;;                                 (ns->out-html-path namespace)])))]
      ;;     (clj->html in out))
      )
    )
  )
