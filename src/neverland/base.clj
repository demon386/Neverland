;;; Base skeleton for rendering
(ns neverland.base
  (:require [clojure.data.xml :as xml]
            [neverland.config :as config])
  (:use [net.cgrand.enlive-html
         :only [deftemplate defsnippet content substitute html-content
                first-child nth-of-type do-> set-attr append]]
        [clj-time.local]
        [clj-time.format]))

(def default-title "M. Tong's Neverland")

;; ============================================================
;; These two macros are retrieved from
;; https://github.com/swannodette/enlive-tutorial
(defmacro maybe-substitute
  ([expr] `(if-let [x# ~expr] (substitute x#) identity))
  ([expr & exprs] `(maybe-substitute (or ~expr ~@exprs))))

(defmacro maybe-content
  ([expr] `(if-let [x# ~expr] (content x#) identity))
  ([expr & exprs] `(maybe-content (or ~expr ~@exprs))))
;; ============================================================

(deftemplate main "neverland/template/main.html"
  [{:keys [title main widgets comment]}]
  [:title] (maybe-content title default-title)
  [:#main] (maybe-content main)
  [:#widgets] (maybe-substitute widgets)
  [:#comment] (maybe-substitute comment))

(def post-sel #{[:div.post] [:.post-header] [:.post-tags]})

(defn- a-with-link [href content]
  (xml/element :a {:href href}
               content))

(defn- link-tags [tags]
   (apply vector
          (map #(a-with-link
                  (str "/tags.html#" %)
                  (config/tags-map %))
               tags)))

(defsnippet post "neverland/template/post.html" post-sel
  [ctxt]
  [:.post-header :.post-date] (content (str "Date: " (clojure.string/replace (format-local-time
                                                                  (to-local-date-time (:date ctxt))
                                                                  :date-hour-minute)
                                                                 #"[a-zA-Z]"
                                                                 " ")))
  [:.post-tags] (append (link-tags (:tags ctxt)))
  [:.title] (do-> (content (:title ctxt))
                  (set-attr :href (:link ctxt)))
  [:.post-content] (substitute (:content-node ctxt)))

(defsnippet comment "neverland/template/post.html" [:div#comment]
  [])

(def widgets-sel [:#widgets])

(defsnippet widgets "neverland/template/widget.html" widgets-sel
  [ctxt]
  [:#widgets] (content ctxt))

(defsnippet recent-link "neverland/template/recent.html" [:#recent :> :ul :> :li]
  [{:keys [title link]}]
  [:a] (do->
        (content title)
        (set-attr :href link)))

(defsnippet recent-widget "neverland/template/recent.html" [[:#recent]]
  [data]
  [:ul] (content (map recent-link data)))

(defsnippet recentcomments-widget "neverland/template/recentcomments.html" [:#recentcomments]
  [])


(defn- generate-item-in-tag-page [postrecord]
  (a-with-link (:link postrecord)
    (:title postrecord)))

(defsnippet tag-item "neverland/template/tag.html" [:.tagitem]
  [postrecord]
  [:.tagitem] (do-> (content (str "["
                                  (format-local-time
                                   (to-local-date-time (:date postrecord))
                                   :year-month-day)
                                  "]  "
                                  ))
                    (append (generate-item-in-tag-page postrecord))))

(defsnippet tag "neverland/template/tag.html" [:.tagname]
  [tagname postrecords]
  [:.tagname] (do-> (set-attr :id tagname)
                    (content (config/tags-map tagname))
                    (append (map tag-item postrecords))))
