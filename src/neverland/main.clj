(ns neverland.main
  (:use [net.cgrand.enlive-html :only [emit* defsnippet deftemplate content]]
        [clojure.java.io :only (reader writer)])
  (:require [neverland.extracter :as extracter]
            [neverland.io])
  (:gen-class))

(defn reverse-xml-str [x]
  "Reverse the escaped char."
  (-> x (.replace "&amp;" "&") (.replace "&lt;" "<") (.replace "&gt;" ">" )))

(defn render [nodes]
  (apply str (emit* (:content nodes))))

(defn get-all-orghtml []
  (neverland.io/walk "orghtml" #"[^.].*\.html"))

(defn post-map-from-file [filename]
  (println filename (render
                     (extracter/extract-date filename)))
  {:date (render
          (extracter/extract-date filename)),
   :content (render
             (extracter/extract-content filename))})

(def *post-sel* #{[:div.post] [:h5.post-date]})

(defsnippet post-model "neverland/template/post.html" *post-sel*
  [ctxt]
  [:h5.post-date] (content (:date ctxt))
  [:div.post] (content (:content ctxt)))

(deftemplate index-model "neverland/template/index.html"
  []
  [:div.preview] (content (map (comp post-model post-map-from-file) (get-all-orghtml))))


(defn -main [& args]
  (let [index-file "/Users/tongmuchenxuan/projects/neverland/html/index.html"]
    (with-open [wtr (writer index-file)]
      (.write wtr (apply (comp reverse-xml-str str) (index-model))))))
