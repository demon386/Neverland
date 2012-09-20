(ns neverland.io
  (:use [clojure.java.io]
        [clojure.string :only [split]]))

;; This code is retrieved and modified from Rosetta
;; http://rosettacode.org/wiki/Walk_a_directory/Recursively

(defn walk [dirpath pattern]
  (->> (file-seq (file dirpath))
       (filter #(re-matches pattern (.getName %)))
       (doall)
       (map #(.getPath %))))

(defn get-filename [path]
  (last (split path (re-pattern "/"))))

(defn save-to-file [path content]
  (with-open [wtr (writer path)]
    (.write wtr content)))