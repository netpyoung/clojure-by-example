(ns clojure-by-example.page
  (:import
   [org.markdown4j Markdown4jProcessor])

  (:require
   [clojure.string :as string]
   [selmer.parser :as selmer]
   [marginalia.core :as marginalia]
   [marginalia.hiccup]
   [hiccup.page]
   [hiccup.compiler]
   [me.raynes.fs :as fs]
   [clojure.tools.namespace.find :as find]))


(let [md* (Markdown4jProcessor.)]
  (defn md
    [markdown-str]

    (->> markdown-str (.process md*))))


(defn is-debug-comment?
  [section]

  (and (= (:type section) :comment)
       (->> section
            :raw
            (re-find #"^(>>|=>)")
            (some?))))


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


(defn section->html
  [section]

  {:docs (md (or (cond
                  (is-debug-comment? section)  ""
                  (= (:type section) :comment) (:raw section)
                  :else                        (:docstring section))
                 ""))
   :code (or (cond
              (= (:type section) :code)
              (marginalia.hiccup/escape-html (:raw section))

              (is-debug-comment? section)
              (marginalia.hiccup/escape-html (str ";; " (:raw section))))
             "")})


;;; ======
;;; pages.

(defn main-page
  []

  (selmer/render-file
   "tpl/main.tpl"

   {:items
    (->> [[:a {:href "public"} "public"]
          [:a {:href "http://clojure.org/"} "clojure"
           [:img {:src "http://clojure.org/file/view/clojure-icon.gif"}]]
          [:a {:href "http://leiningen.org/"} "leiningen"
           [:img {:src "http://leiningen.org/img/leiningen.jpg"}]]
          [:a {:href "http://www.youtube.com/user/ClojureTV"}
           [:img {:src "https://developers.google.com/youtube/images/YouTube_logo_standard_white.png"}]]
          [:a {:href "http://www.reddit.com/r/clojure"} "reddit"
           [:img {:src "http://icons.iconarchive.com/icons/chrisbanks2/cold-fusion-hd/128/reddit-icon.png"}]]
          [:a {:href "http://tryclj.com/"} "tryclj"]
          ;; http://tryclj.com/resources/public/clojure-logo.png
          ;; #63b132
          ;; Try
          ;; 72px
          ;; color: #5881d8;
          ;; Clo <em>j</em>ure
          [:a {:href "http://himera.herokuapp.com/"}
           "ClojureScript compiler as web service"]
          [:a {:href "https://www.4clojure.com/"}
           [:img {:src "https://www.4clojure.com/images/4clj-logo-small.png"}]]
          [:a {:href "http://clojuredocs.org/"}
           [:img {:src "http://clojuredocs.org/images/cd_logo.png"}]]]
         (map hiccup.compiler/compile-html)
         (map eval))}))


(defn main-index-page
  [ns-info-dic]

  (selmer/render-file
   "tpl/main-index.tpl"

   {:items
    (for [[k v] ns-info-dic]
      {:section (name k)})}))


(defn section-index-page
  [[section sub-page-namespaces :as ns-info]]

  (selmer/render-file
   "tpl/section-index.tpl"

   {:section section
    :items   (->> sub-page-namespaces
                  (map (fn [namespace]
                         [(ns-sym->basename namespace) (ns-sym->title namespace)]))
                  (map (fn [[basename title]]
                         {:basename basename :title title})))}))


(defn example-page
  [in-clj]

  (let [doc       (marginalia/path-to-doc in-clj)
        namespace (:ns doc)]

    (selmer/render-file
     "tpl/example.tpl"

     {:parent-dir (-> (str namespace)
                      (string/split #"\.")
                      (first))
      :basename (ns-sym->basename namespace)
      :cljfname (ns-sym->cljfname namespace)
      :doctables (->> doc :groups (map section->html))})))


(defn get-example-namespace-info-dic
  [dirname]

  (->> (java.io.File. dirname)
       (find/find-namespaces-in-dir)
       (group-by (fn [x]
                   (-> (name x)
                       (string/replace "." "/")
                       (string/split #"/")
                       (first))))
       (reduce-kv (fn [acc k v]
                    (assoc acc (keyword k)
                           (->> v
                                (mapv (fn [x]
                                        (-> (name x)
                                            (string/replace "." "/")
                                            (string/split #"/")
                                            (second)
                                            (keyword)))))))
                  {})))
