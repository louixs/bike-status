;;utility functions
(ns bikes.util)

(defn split-by-space [str]
  (clojure.string/split str #"\s"))

;;function to clear white space between the words in a string
;;idea from http://www.markhneedham.com/blog/2013/09/22/clojure-stripping-all-the-whitespace/
(defn clean-blank-space [str]
  (->>
   str
   (split-by-space)
   (filter #(not (clojure.string/blank? %)))
   ;(str/join " ")
))
