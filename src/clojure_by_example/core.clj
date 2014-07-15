(ns clojure-by-example.core
  (:require
   [me.raynes.fs :as fs]
   [clojure.tools.reader.edn :as edn]
   [clojure-by-example.page :as page])
  (:gen-class))


(def INFO_FNAME "examples.edn")
(def EXAMPLE_BASE_DIR "examples/src")
(def PUBLIC_BASE_DIR "public")


(defn ensure-dir!
  [target-dir]

  (or (fs/exists? target-dir)
      (fs/mkdirs target-dir)
      (throw (Exception. (str "[ERROR] couldn't create target dir!" target-dir)))))


(defn raw-info->ns-info
  "
  (raw-info->ns-info {:a [:b-c]})
  ;=> {:a (\"a.b-c\")}
  "
  [info-dic]

  (->> info-dic
       (map (fn [[k v]]
              [k (map #(str (name k) "." (name %)) v)]))
       (into {})))


(defn ns->in-clj-path
  "
  >> (ns->in-clj-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/examples/src/hello/a_b.clj\"
  "
  [ns-sym]

  (->> (name ns-sym)
       (str EXAMPLE_BASE_DIR "/")
       (fs/ns-path)
       (str)))


(defn ns->out-html-path
  "
  >> (ns->out-html-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/public/hello/a_b.html\"
  "
  [ns-sym]

  (let [ns-path (->> (name ns-sym)
                     (str PUBLIC_BASE_DIR "/")
                     (fs/ns-path))]

    (str (fs/parent ns-path)
         "/"
         (fs/base-name ns-path true) ".html")))


(defn clj->html
  [in-clj out-html]

  ;; TODO(kep) need refactoring.
  (->> in-clj
       (page/example-page)
       (spit out-html)))


(defn -main
  []

  ;; main page.
  (->> (page/main-page)
       (spit "index.html"))

  (let [ns-info-dic (->> INFO_FNAME
                         (slurp)
                         (edn/read-string)
                         (raw-info->ns-info))]

    ;; main index page.
    (ensure-dir! PUBLIC_BASE_DIR)
    (->> (page/main-index-page ns-info-dic)
         (spit (str PUBLIC_BASE_DIR "/" "index.html")))


    (doseq [[section sub-page-namespaces :as ns-info] ns-info-dic]

      ;; example index page.
      (ensure-dir! (str PUBLIC_BASE_DIR "/" (name section)))
      (->> (page/section-index-page ns-info)
           (spit (str PUBLIC_BASE_DIR "/" (name section) "/index.html")))

      ;; example pages.
      (doseq [namespace sub-page-namespaces]
        (let [in  (ns->in-clj-path namespace)
              out (ns->out-html-path namespace)]
          (ensure-dir! (fs/parent out))

          (clj->html in out))))))
