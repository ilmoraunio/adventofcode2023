(ns ilmoraunio.adventofcode2023-1
  (:require [clojure.string :as str]))

(def nums #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9})

(defn trace-forward-num [s]
  (when-let [hit (re-find #"\d" s)]
    (Integer/valueOf hit)))

(defn trace-back-num [s]
  (let [cand (last s)]
    (cond
      (nil? cand) nil
      (nums cand) (Character/digit cand 10)
      :else (recur (butlast s)))))

(defn sum-calibration-values [input]
  (reduce
   (fn [acc v]
     (let [first-digit (trace-forward-num v)
           last-digit (trace-back-num v)]
       (+ acc (+ (* first-digit 10)
                 last-digit))))
   0
   (str/split-lines input)))
