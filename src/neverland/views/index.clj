(ns neverland.views.post
    (:use [net.cgrand.enlive-html :only [defsnippet]]))

(deftemplate index-view "neverland/template/index.html"
  [ctxt]
  [:div.preview] (content :index))