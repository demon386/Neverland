(ns neverland.extracter
  (:require [net.cgrand.enlive-html :as html])
  (:use [clojure.java.io :only (reader)]
        [clojure.string :only [split]]))

(defmacro defextract [name selector]
  `(defn ~name
     [filename#]
     (with-open [rdr# (reader filename#)]
       (first (html/select (html/html-resource rdr#)
                          ~selector)))))

(defextract extract-date [:p.date])

(defextract extract-title [:title])

(defextract extract-content [:div#content])