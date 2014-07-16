(ns clojure-by-example.page
  (:require
   [clojure.string :as string]
   [selmer.parser :as selmer]
   [marginalia.core :as marginalia]
   [marginalia.hiccup]
   [clojure.tools.namespace.find :as find]

   [clojure-by-example.util :as util]))



(defn- is-debug-comment?
  [section]

  (and (= (:type section) :comment)
       (->> section
            :raw
            (re-find #"^(>>|=>)")
            (some?))))


;;; ======
;;; pages.

(defn main-page
  []

  (selmer/render-file
   "tpl/main.tpl"

   {:items [{:link "http://clojure.org/"
             :img "http://clojure.org/file/view/clojure-icon.gif"}
            {:link "http://leiningen.org/"
             :img "http://leiningen.org/img/leiningen.jpg"}
            {:link "http://www.youtube.com/user/ClojureTV"
             :img "https://developers.google.com/youtube/images/YouTube_logo_standard_white.png"}
            {:link "http://www.reddit.com/r/clojure"
             :img "http://icons.iconarchive.com/icons/chrisbanks2/cold-fusion-hd/128/reddit-icon.png"}

            ;; "http://tryclj.com/"
            ;; http://tryclj.com/resources/public/clojure-logo.png
            ;; #63b132
            ;; Try
            ;; 72px
            ;; color: #5881d8;
            ;; Clo <em>j</em>ure

            {:link "http://tryclj.com/"
             :title "Try Clo <em>j</em>ure"}
            {:link "http://himera.herokuapp.com/"
             :title "ClojureScript compiler as web service"}
            {:link "https://www.4clojure.com/"
             :img "https://www.4clojure.com/images/4clj-logo-small.png"}
            {:link "http://clojuredocs.org/"
             :img "http://clojuredocs.org/images/cd_logo.png"}]}))


(defn main-index-page
  [sections]

  (selmer/render-file
   "tpl/main-index.tpl"

   {:sections sections}))


(defn section-index-page
  [section sub-page-namespaces]

  (selmer/render-file
   "tpl/section-index.tpl"

   {:section section
    :items   (->> sub-page-namespaces
                  (map (fn [namespace]
                         [(util/ns-sym->basename namespace) (util/ns-sym->title namespace)]))
                  (map (fn [[basename title]]
                         {:basename basename :title title})))}))


(defn example-page
  [in-clj]

  (let [doc       (marginalia/path-to-doc in-clj)
        namespace (:ns doc)]

    (selmer/render-file
     "tpl/example.tpl"

     {:parent-dir (util/ns->parent-dir namespace)
      :basename (util/ns-sym->basename namespace)
      :cljfname (util/ns-sym->cljfname namespace)
      :doctables (->> doc
                      :groups
                      (map (fn [section]
                             {:docs (util/md (or (cond
                                                  (is-debug-comment? section)  ""
                                                  (= (:type section) :comment) (:raw section)
                                                  :else                        (:docstring section))

                                                 ""))

                              :code (or (cond
                                         (= (:type section) :code)
                                         (marginalia.hiccup/escape-html (:raw section))

                                         (is-debug-comment? section)
                                         (marginalia.hiccup/escape-html (str ";; " (:raw section))))
                                        "")})))})))
