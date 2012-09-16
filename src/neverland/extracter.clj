(ns neverland.extracter
  (:require [net.cgrand.enlive-html :as html])
  (:use [clojure.java.io :only (reader)]))

(defmacro defextract [name selector]
  `(defn ~name
     [filename#]
     (with-open [rdr# (reader filename#)]
       (first (html/select (html/html-resource rdr#)
                          ~selector)))))

(defextract extract-date [:p.date])

(defextract extract-content [:div#content])