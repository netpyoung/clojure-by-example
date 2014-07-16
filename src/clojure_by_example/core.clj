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
  >> (ns->in-clj-path :hello.a-b)
  ;=> \"/home/pyoung/prj/clojure-by-example/examples/src/hello/a_b.clj\"
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

    (str (fs/parent ns-path)
         "/"
         (fs/base-name ns-path true) ".html")))

(defn -main
  []

  ;; main page.
  (->> (page/main-page)
       (spit "index.html"))

  (let [sections (-> (str SECTION_BASE_DIR "/index.txt")
                     (slurp)
                     (clojure.string/trim)
                     (clojure.string/split #"\n"))]

    ;; main index page.
    (util/ensure-dir! PUBLIC_BASE_DIR)
    (->> (page/main-index-page sections)
         (spit (str PUBLIC_BASE_DIR "/" "index.html")))


    (doseq [section sections]


      (let [namespaces (-> (clojure.string/join "/" [SECTION_BASE_DIR section "index.txt"])
                           (slurp)
                           (clojure.string/trim)
                           (clojure.string/split #"\n"))]

        ;; example index page.
        (util/ensure-dir! (str PUBLIC_BASE_DIR "/" (name section)))
        (->> (page/section-index-page section namespaces)
             (spit (clojure.string/join "/" [PUBLIC_BASE_DIR section "index.html"])))


        (doseq [namespace namespaces]
          (let [in  (ns->in-clj-path section namespace) out (ns->out-html-path namespace)]

            ;; example page.
            (util/ensure-dir! (fs/parent out))
            (->> in
                 (page/example-page)
                 (spit out))))))))
