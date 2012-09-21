;;; Base skeleton for rendering
(ns neverland.base
  (:use [net.cgrand.enlive-html
         :only [deftemplate defsnippet content substitute
                first-child nth-of-type do-> set-attr]]
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
  [{:keys [title main widgets]}]
  [:title] (maybe-content title default-title)
  [:#main] (maybe-content main)
  [:#widgets] (maybe-content widgets))

(def post-sel #{[:div.post] [:h5.post-date]})

(defsnippet post "neverland/template/post.html" post-sel
  [ctxt]
  [:h5.post-date] (content (str "Date: " (clojure.string/replace (format-local-time
                                                                  (to-local-date-time (:date ctxt))
                                                                  :date-hour-minute)
                                                                 #"[a-zA-Z]"
                                                                 " ")))
  [:div.post] (content (:content (:content-node ctxt))))

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
