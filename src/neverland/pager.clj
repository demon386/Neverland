(ns neverland.pager)

(defn sort-posts [postrecords]
   (sort-by :date postrecords))