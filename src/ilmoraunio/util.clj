(ns ilmoraunio.util
  (:require [clojure.string :as str]))

(defn gulp [filename]
  (str/split-lines (slurp filename)))
