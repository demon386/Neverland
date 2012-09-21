(ns neverland.pager
  (:use [clj-time.local]))


(defn sort-posts [postrecords]
  (reverse (sort-by #(to-local-date-time (:date %)) postrecords)))