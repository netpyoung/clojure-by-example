(ns clojure-by-example.page
  (:import [org.markdown4j Markdown4jProcessor])
  (:require [hiccup.core :as hiccup]
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


(defn section->html [section]
  [:tr
   [:td {:class "docs"}
    (md (or (cond
             (is-debug-comment? section)  ""
             (= (:type section) :comment) (:raw section)
             :else                        (:docstring section))
            ""))
    ]

   [:td {:class "code"}
    [:pre
     [:code {:class "clojure"}
      (or (cond
           (= (:type section) :code)
           (marginalia.hiccup/escape-html (:raw section))

           (is-debug-comment? section)
           (marginalia.hiccup/escape-html (str ";; " (:raw section))))
          "")]]]])


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
         [:script "hljs.initHighlightingOnLoad();"]
         ]
        [:body
         [:div {:class "example" :id basename}
          [:h2 [:a {:href "http://www.google.com"} "google!?"] ":" (:ns doc)]
          ]

         (->> (:groups doc)
              (map (fn [group]
                     [:table (section->html group)])))]]
       (hiccup/html {:mode :html} (hiccup.page/doctype :html5))))
