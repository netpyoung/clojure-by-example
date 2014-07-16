(ns clojure-by-example.core
  (:require
   [me.raynes.fs :as fs]
   [clojure-by-example.page :as page]
   [clojure-by-example.util :as util])
  (:gen-class))


(def INFO_FNAME "examples.edn")
(def EXAMPLE_BASE_DIR "examples/src")
(def PUBLIC_BASE_DIR "public")


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


(defn -main
  []

  ;; main page.
  (->> (page/main-page)
       (spit "index.html"))

  (let [ns-info-dic (util/get-ns-info INFO_FNAME)]

    ;; main index page.
    (util/ensure-dir! PUBLIC_BASE_DIR)
    (->> (page/main-index-page ns-info-dic)
         (spit (str PUBLIC_BASE_DIR "/" "index.html")))


    ;; per sections
    (doseq [[section sub-page-namespaces :as ns-info] ns-info-dic]

      ;; example index page.
      (util/ensure-dir! (str PUBLIC_BASE_DIR "/" (name section)))
      (->> (page/section-index-page ns-info)
           (spit (str PUBLIC_BASE_DIR "/" (name section) "/index.html")))

      ;; example pages.
      (doseq [namespace sub-page-namespaces]
        (let [in  (ns->in-clj-path namespace) out (ns->out-html-path namespace)]

          ;; clj->html
          (util/ensure-dir! (fs/parent out))
          (->> in
               (page/example-page)
               (spit out)))))))
