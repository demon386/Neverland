(ns neverland.main
  (:use [net.cgrand.enlive-html :only [emit* defsnippet deftemplate content]]
        [clojure.java.io :only (reader writer)]
        [neverland.rss :only (rss)])
  (:require [neverland.extracter :as extracter]
            [neverland.post :as post]
            [neverland.pager :as pager]
            [neverland.render :as render]
            [fs.core :as fs])
  (:gen-class))

(def reserve-file-patterns ["bootstrap" "org.css" "mycss.css"])

(defn init-dirs []
  "Clean the html folder, delete everything except reserved. Create new dirs."
  (let [to-del (filter (comp not #(some #{%} reserve-file-patterns))
                       (fs/list-dir "html"))]
    (dorun (map fs/delete-dir
                (map #(str "html/" %) to-del)))
    (fs/mkdir "html/posts")))

(defn all-orghtml-files []
  (neverland.io/walk "orghtml" #"[^.].*\.html"))

(defn publish-resources []
  "Publish all the folders in orghtml."
  (let [res-dirs (filter fs/directory?
                         (map #(str "orghtml/" %)
                              (fs/list-dir "orghtml")))]
    (dorun (map #(fs/copy-dir % "html") res-dirs))))

(defn -main [& args]
  (when-let [postrecords (map post/to-postrecord-from-file
                              (all-orghtml-files))]
    (init-dirs)
    (render/render postrecords)
    (rss postrecords)
    (publish-resources)))