(ns neverland.views.post
    (:use [net.cgrand.enlive-html :only [defsnippet]]))

(def *post-sel* #{[:div.post] [:h5.post-date]})

(defsnippet post-snippet "neverland/template/post.html" *post-sel*
  [ctxt]
  [:h5.post-date] (content (:date ctxt))
  [:div.post] (content (:content ctxt)))