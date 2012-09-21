(ns neverland.post
  (:require [neverland.extracter :as extracter])
  (:use [clojure.string :only [split]]
        [clojure.string :only [split]]))

(defrecord PostRecord [date title content-node link])

(defn filename-to-link [filename]
  (.concat "posts/"  (-> filename
                         (split (re-pattern "/"))
                         (last)
                         (split (re-pattern "\\."))
                         (first)
                         (.concat ".html"))))

(defn get-pure-date-str [str]
  (nth (split str #": ") 1))

(defn to-postrecord-from-file [filename]
  ;; Right now the extracter for HTML exported by Org mode is used.
  ;; It's easy to support different formats with different extracter
  (PostRecord. (get-pure-date-str (first (:content (extracter/extract-date filename))))
               (first (:content (extracter/extract-title filename)))
               (extracter/extract-content filename)
               (filename-to-link filename)))