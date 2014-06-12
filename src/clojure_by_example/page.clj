(ns clojure-by-example.page
  (:import
   [org.markdown4j Markdown4jProcessor])

  (:require
   [clojure.string :as string]
   [selmer.parser :as selmer]
   [marginalia.core :as marginalia]
   [marginalia.hiccup]
   [hiccup.core :as hiccup]
   [me.raynes.fs :as fs]
   [hiccup.page]
   [hiccup.compiler]
   [clojure.tools.namespace.find :as find]))


(let [md* (Markdown4jProcessor.)]
  (defn md
    [markdown-str]
    (->> markdown-str (.process md*))))


(defn is-debug-comment?
  [section]
  (and (= (:type section)
          :comment)
       (->> section
            (:raw)
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


(defn section->html [section]
  [:tr
   [:td {:class "docs"}
    (md (or (cond
             (is-debug-comment? section)  ""
             (= (:type section) :comment) (:raw section)
             :else                        (:docstring section))
            ""))]

   [:td {:class "code"}
    [:pre [:code {:class "clojure"}
           (or (cond
                (= (:type section) :code)
                (marginalia.hiccup/escape-html (:raw section))

                (is-debug-comment? section)
                (marginalia.hiccup/escape-html (str ";; " (:raw section))))
               "")]]]])


;;; ======
;;; pages.

(defn main-page []
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


(defn main-index-page [ns-info-dic]
  (hiccup.page/html5
   [:head
    [:link {:rel "stylesheet" :href "../../resources/css/style.css"}]]
   [:body
    [:div {:class "main"}
     (for [[k v] ns-info-dic]
       [:div {:id k :style "float:left;"}

        [:h3 [:a {:href k} (string/capitalize (name k))]]
        [:ul
         (->> (take 5 v)
              (map (fn [namespace] [(ns-sym->basename namespace) (ns-sym->title namespace)]))
              (map (fn [[basename title]] [:li [:a {:href (str "./" (name k) "/" basename ".html")} title]])))

         [:li [:a {:href k}] "..."]
         ]
        ]
       )
     ]]
   )
  )


(defn section-index-page [[section sub-page-namespaces :as ns-info]]
  (hiccup.page/html5
   [:head
    [:link {:rel "stylesheet" :href "../../resources/css/style.css"}]]
   [:body
    [:div {:class "main" :id section}

     [:h1 [:a {:href "../"} "public"]]
     [:h2 section]
     [:ul
      (->> sub-page-namespaces
           (map (fn [namespace]
                  [(ns-sym->basename namespace) (ns-sym->title namespace)]))
           (map (fn [[basename title]]
                  [:li [:a {:href (str "./" basename ".html")} title]])))
      ]]]))


(defn example-page [in-clj]
  (let [doc
        (marginalia/path-to-doc in-clj)

        namespace
        (:ns doc)

        basename
        (ns-sym->basename namespace)

        parent-dir
        (-> (str namespace)
            (string/split #"\.")
            (first))]

    (hiccup.page/html5
     [:head
      [:meta {:charset "utf-8"}]
      [:link {:rel "icon" :type "image/x-icon"
              :href "../../resources/favicon.ico"}]
      [:link {:rel "stylesheet" :href "../../resources/css/style.css"}]
      [:link {:rel "stylesheet" :href "http://yandex.st/highlightjs/8.0/styles/default.min.css"}]
      [:script {:src "http://yandex.st/highlightjs/8.0/highlight.min.js"}]
      [:script {:src "http://yandex.st/highlightjs/8.0/languages/clojure.min.js"}]
      [:script "hljs.initHighlightingOnLoad();"]]

     [:body
      [:div {:class "title" :id namespace}
       [:h1 [:a {:href "../"} "Clojure By Example"]]
       [:h2 [:a {:href "./"} parent-dir] "." basename]
       [:h3 [:a {:href (str "https://github.com/netpyoung/clojure-by-example/blob/gh-pages/examples/src/" (ns-sym->cljfname namespace))} "source-github"]]
       [:h3 [:a {:href (str "https://raw.githubusercontent.com/netpyoung/clojure-by-example/gh-pages/examples/src/" (ns-sym->cljfname namespace))} "source-raw"]]
       ]
      [:div {:class "example"}
       (->> (:groups doc)
            (map (fn [group]
                   [:table (section->html group)])))]])))



(defn get-example-namespace-info-dic [dirname]
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
