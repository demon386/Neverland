(ns neverland.extracter
  (:require [net.cgrand.enlive-html :as html])
  (:use [clojure.java.io :only (reader)]
        [clojure.string :only [split]]))

(defmacro defextract [name selector]
  `(defn- ~name
     [filename#]
     (with-open [rdr# (reader filename#)]
       (first (html/select (html/html-resource rdr#)
                          ~selector)))))

(defextract extract-date-node [:p.date])

(defextract extract-title-node [:title])

(defextract extract-content-node [:div#content])


(def extract-title (comp first :content extract-title-node))

(defn- get-pure-date-str [str]
  (nth (split str #": ") 1))

(def extract-date (comp get-pure-date-str first :content extract-date-node))

(defn remove-title-from-content [content]
  (remove #(= (:attrs %) {:class "title"})
          (rest content)))

(def extract-content (comp remove-title-from-content
                           :content
                           extract-content-node))
