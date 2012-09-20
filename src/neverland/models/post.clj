(ns neverland.models.post)

(defrecord PostRecord [date content])

(defn to-postrecord-from-file [filename]
  ;; Right now the extracter for HTML exported by Org mode is used.
  ;; It's easy to support different formats with different extracter
  (PostRecord. (render
                (extracter/extract-date filename))
               (render
                (extracter/extract-content filename))))