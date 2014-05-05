(ns clojure-by-example.core
  (:gen-class)
  (:require [clojure.string :as string]
            [hiccup.core :as hiccup]
            [hiccup.page]
            [marginalia.core :as cc]
            [marginalia.html :as html]
            [marginalia.parser :as parser]
            [stencil.core :as stencil]
            [me.raynes.fs :as fs]
            [clojure.tools.reader.edn :as edn]
            )
  (:import [org.markdown4j Markdown4jProcessor]))

(def example-base-dir* "examples")

(defn is-debug-comment? [section]
  (and (= (:type section)
          :comment)
       (->> section
            (:raw)
            (re-find #"^(>>|=>)")
            (some?))))

(let [md* (Markdown4jProcessor.)]
  (defn md [markdown-str]
    (. md* (process markdown-str))))

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


(defn clj->html [clj]
  (let [basename (fs/base-name clj true)
        html (str "public/" basename ".html")
        template (slurp "templates/example.html")
        doc (cc/path-to-doc clj)]
    (->> [:html

          [:head
           [:meta {:charset "utf-8"}]

           [:link {:rel "stylesheet" :href "../css/style.css"}]
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
         (hiccup/html {:mode :html} (hiccup.page/doctype :html5))
         (spit html))))


(defn ^:private munge-ns [ns-sym]
  ;; https://github.com/clojure/jvm.tools.analyzer/blob/master/src/main/clojure/clojure/jvm/tools/analyzer.clj#L863
  (-> (name ns-sym)
      (string/replace "." "/")
      (string/replace "-" "_")
      (str ".clj")))


(defn xxx [info-dic]
  (->> info-dic
       (mapcat (fn [[k v]]
                 (->> v
                      (map #(str example-base-dir* "/"
                                 (name k) "." (name %))))))))


(defn -main [& args]
  (clj->html "examples/functions.clj")

  (->> (slurp "examples.edn")
       (edn/read-string)
       (xxx)
       (map munge-ns)
       (println))

  (println (cc/find-processable-file-paths "examples" #".clj"))
  )
