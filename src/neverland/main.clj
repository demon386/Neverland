(ns neverland.main
  (:use [net.cgrand.enlive-html :only [emit* defsnippet deftemplate content]]
        [clojure.java.io :only (reader writer)]
        [neverland.rss :only (rss)])
  (:require [neverland.extracter :as extracter]
            [neverland.post :as post]
            [neverland.pager :as pager]
            [neverland.render :as render])
  (:gen-class))

(defn all-orghtml-files []
  (neverland.io/walk "orghtml" #"[^.].*\.html"))

(defn -main [& args]
  (when-let [postrecords (map post/to-postrecord-from-file
                              (all-orghtml-files))]
    ;(render/render postrecords)
    (rss postrecords)))