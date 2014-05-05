(ns clojure-by-example.core
  (:gen-class)
  (:import [org.markdown4j Markdown4jProcessor])
  (:require [clojure.string :as string]
            [hiccup.core :as hiccup]
            [hiccup.page]
            [marginalia.core :as cc]
            [marginalia.html :as html]
            [marginalia.parser :as parser]
            [stencil.core :as stencil]
            [me.raynes.fs :as fs]
            [clojure.tools.reader.edn :as edn]))


(def example-base-dir* "examples/src")
(def public-base-dir* "public")


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


(defn clj->html [in-clj out-html]
  (let [parent-dir (fs/parent out-html)]
    (when-not (fs/exists? parent-dir)
      (when-not (fs/mkdirs parent-dir)
        (throw (Exception.
                (str "[ERROR] couldn't create output dir!" [in-clj out-html]))))))

  (let [basename (fs/base-name in-clj true)
        doc (cc/path-to-doc in-clj)]
    (->> [:html

          [:head
           [:meta {:charset "utf-8"}]

           [:link {:rel "stylesheet" :href "../../css/style.css"}]
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
         (spit out-html))))


(defn info->ns [info-dic]
  (->> info-dic
       (mapcat (fn [[k v]]
                 (->> v
                      (map #(str (name k) "." (name %))))))))


(defn ns->in-clj-path [namespace]
  (->> namespace
       (str example-base-dir* "/")
       (fs/ns-path)
       (str)))


(defn ns->out-html-path [namespace]
  (let [v (str public-base-dir* "/" namespace)]
    (str (fs/parent (fs/ns-path v)) "/"
         (fs/base-name (fs/ns-path v) true) ".html")))


(defn -main [& args]

  (let [ns-info (->> (slurp "examples.edn")
                     (edn/read-string)
                     (info->ns))
        clj-html-paths (->> ns-info
                            (map (fn [namespace]
                                   [(ns->in-clj-path namespace)
                                    (ns->out-html-path namespace)])))]

    ;; make example-files
    (doseq [[in out] clj-html-paths]
      (clj->html in out))))

(hiccup/html [:h1
              (for [i (range 10)]
                [:td i])
              ])
(->> (slurp "examples.edn")
     (edn/read-string)
     )
