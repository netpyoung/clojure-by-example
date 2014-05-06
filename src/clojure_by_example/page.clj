(ns clojure-by-example.page
  (:import [org.markdown4j Markdown4jProcessor])
  (:require [clojure.string :as string]
            [hiccup.core :as hiccup]
            [me.raynes.fs :as fs]
            [hiccup.page]))


(let [md* (Markdown4jProcessor.)]
  (defn md
    [markdown-str]
    (->> markdown-str
         (.process md*))))


(defn is-debug-comment?
  [section]
  (and (= (:type section)
          :comment)
       (->> section
            (:raw)
            (re-find #"^(>>|=>)")
            (some?))))


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

;; pages
(defn main-page [ns-info]
  (->> [:html
        [:head
         [:meta {:charset "utf-8"}]
         [:link {:rel "icon" :type "image/x-icon"
                 :href "resources/favicon.ico"}]
         ]
        [:body
         [:h1 "Under Construction"]
         [:ul
          (->> [[:a {:href "http://clojure.org/"} "clojure"
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
               (map (fn [e] [:li e])))]]]
       (hiccup/html {:mode :html} (hiccup.page/doctype :html5))
       )
  )


(defn main-index-page [ns-info-dic]
  (->> [:html
        [:body
         (for [[k v] ns-info-dic]
           [:div {:id k}
            [:ul
             [:li [:a {:href k} k]]
             (->> (take 5 v)
                  (map (fn [namespace] [(ns-sym->basename namespace) (ns-sym->title namespace)]))
                  (map (fn [[basename title]] [:li [:a {:href (str "./" (name k) "/" basename ".html")} title]])))
             ]
            [:br]
            ]

           )
         ]
        ]
       (hiccup/html {:mode :html} (hiccup.page/doctype :html5))
       )
  )


(defn section-index-page [[section sub-page-namespaces :as ns-info]]
  (->> [:html
        [:body
         [:ul
          (->> sub-page-namespaces
               (map (fn [namespace] [(ns-sym->basename namespace) (ns-sym->title namespace)]))
               (map (fn [[basename title]] [:li [:a {:href (str "./" basename ".html")} title]])))]]
        ]
       (hiccup/html {:mode :html} (hiccup.page/doctype :html5))
       )
  )



(defn example-page [basename doc]
  (->> [:html

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
         [:div {:class "title" :id basename}
          [:h2 [:a {:href "http://www.google.com"} "google!?"] ":" (:ns doc)]]
         [:div {:class "example"}
          (->> (:groups doc)
               (map (fn [group]
                      [:table (section->html group)])))]]]
       (hiccup/html {:mode :html} (hiccup.page/doctype :html5))))
