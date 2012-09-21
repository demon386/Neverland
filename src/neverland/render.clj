;;; Render applys the template and snippet, saving the
;;; generated html into file.

(ns neverland.render
  (:require [neverland.base :as base]
            [neverland.io :as io]
            [neverland.pager :as pager]
            [clojure.string])
  (:use [net.cgrand.enlive-html :only [emit* text]]))

(def page-posts-num 3)
(def recent-posts-num 5)

(def html-root "/Users/tongmuchenxuan/projects/neverland/html/")

(defn reverse-xml-str [x]
  "Reverse the escaped char."
  (-> x (.replace "&amp;" "&") (.replace "&lt;" "<") (.replace "&gt;" ">" )))

(defn to-str [nodes]
  (reverse-xml-str (apply str (emit* nodes))))

(defn recent-widget-render [postrecords]
  (base/recent-widget (take recent-posts-num
                            (pager/sort-posts postrecords))))

(defn widgets-render [postrecords]
  (base/widgets [(recent-widget-render postrecords)]))

(defn single-page-render [n postrecords]
  (let [postrecords-in-page (nth (partition page-posts-num page-posts-num nil
                                            (pager/sort-posts postrecords)) n)
        page-html (to-str (base/main {:main (map base/post postrecords-in-page)
                                      :widgets (widgets-render postrecords)}))]
    (if (= n 0)
      (io/save-to-file (str html-root "index.html")
                       page-html)
      (io/save-to-file (str html-root "page" (str n) ".html")
                       page-html))))

(defn pages-render [postrecords]
  ;; @todo
  (single-page-render 0 postrecords))

(defn posts-render [postrecords]
  (let [widgets (widgets-render postrecords)]
    (doseq [post postrecords]
      (io/save-to-file (.concat html-root (:link post))
                       (to-str (base/main {:title (:title post)
                                           :main (base/post post)
                                           :widgets widgets}))))))

(defn render [postrecords]
  (posts-render postrecords)
  (pages-render postrecords))