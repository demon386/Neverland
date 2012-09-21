(ns neverland.rss
  (:require [neverland.io :as io]
            [clojure.data.xml :as xml]
            [neverland.pager :as pager])
  (:use [neverland.render :only [to-str]]
        [clojure.data.xml :only [element cdata]]
        [net.cgrand.enlive-html :only [emit* text remove-class]]))

(def site-root "http://www.mtong.me")
(def html-root "/Users/tongmuchenxuan/projects/neverland/html/")

(defn generate-items [postrecords]
  (apply vector (for [post postrecords]
                  (element :item {}
                           (element :title {} (:title post))
                           (element :link {} (.concat site-root (:link post)))
                           (element :description {} (cdata (-> post
                                                               :content-node
                                                               emit*
                                                               to-str)))))))

(defn rss [postrecords]
  (let [rss-content (xml/indent-str (element :rss {:version "2.0"}
                                           (element :channel {}
                                                    (element :title {} "M. Tong's Neverland")
                                                    (element :link {} "http://www.mtong.me")
                                                    (element :description {} "Learning, Thinking, Programming.")
                                                    (generate-items (pager/sort-posts
                                                                     postrecords)))))]
    (io/save-to-file (.concat html-root "rss.xml") rss-content)))