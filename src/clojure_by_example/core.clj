(ns clojure-by-example.core
  (:require
   [clojure.string :as str]
   [me.raynes.fs :as fs]
   [clojure-by-example.page :as page]
   [clojure-by-example.util :as util])
  (:gen-class))


(def SECTION_BASE_DIR "examples")
(def PUBLIC_BASE_DIR "public")


(defn ns->in-clj-path
  "
  >> (ns->in-clj-path \"hello\" :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/examples/hello/src/hello/a_b.clj\"
  "
  [section namespace]

  (->> [SECTION_BASE_DIR section "src" namespace]
       (str/join "/" )
       (fs/ns-path)
       (str)))


(defn ns->out-html-path
  "
  >> (ns->out-html-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/public/hello/a_b.html\"
  "
  [namespace]

  (let [ns-path (->> namespace
                     (str PUBLIC_BASE_DIR "/")
                     (fs/ns-path))]
    (str (fs/parent ns-path) "/" (fs/base-name ns-path true) ".html")))


(defn get-sections
  [section-base-dir]

  (-> (str section-base-dir "/index.txt")
      (slurp)
      (str/trim)
      (str/split #"\n")))


(defn get-section-namespaces
  [section-base-dir section]

  (-> (str/join "/" [section-base-dir section "index.txt"])
      (slurp)
      (str/trim)
      (str/split #"\n")))


(defn -main
  []

  ;; generate index.html
  (->> (page/main-page)
       (spit "index.html"))

  (let [sections (get-sections SECTION_BASE_DIR)]

    ;; generate public/index.html
    (util/ensure-dir! PUBLIC_BASE_DIR)
    (->> (page/main-index-page sections)
         (spit (str PUBLIC_BASE_DIR "/" "index.html")))

    (doseq [section sections]
      (let [namespaces (get-section-namespaces SECTION_BASE_DIR section)]

        ;; generate public/section/index.html
        (util/ensure-dir! (str PUBLIC_BASE_DIR "/" (name section)))
        (->> (page/section-index-page section namespaces)
             (spit (str/join "/" [PUBLIC_BASE_DIR section "index.html"])))

        (doseq [namespace namespaces]
          (let [src-path (ns->in-clj-path section namespace)
                dst-path (ns->out-html-path namespace)]

            ;; generate public/section/{namespace-path}.html
            (util/ensure-dir! (fs/parent dst-path))
            (->> src-path
                 (page/example-page)
                 (spit dst-path))))))))
