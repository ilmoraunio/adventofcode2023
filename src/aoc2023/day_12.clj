(ns aoc2023.day-12
  "Notes: Once again, this wasn't very optimal code. The amount of cartesian products could well be a factor.
   A sample run took about 13~15 seconds."
  (:refer-clojure :exclude [==])
  (:require [aoc2023.util :as util]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(defn calc-sum-of-arrangements
  [filename]
  (let [records (->> (util/gulp filename)
                     (map (partial re-seq #"(.+) (.+)"))
                     (map (fn [[[match record sequence]]]
                            {:record (str/replace record #" " "")
                             :raw match
                             :sequence (map #(Integer. %) (str/split sequence #","))})))]
    (->> records
         (map (fn [{:keys [record] :as m}]
                (assoc m :arrangements
                         (->> record
                              (map (fn [char]
                                     (if (= \? char)
                                       (for [x [\. \#]] x)
                                       [char])))
                              (apply combo/cartesian-product)
                              (map (partial apply str))
                              (map (partial partition-by #{\. \#}))
                              (map (partial map frequencies))
                              (map (partial remove (fn [x] (= (ffirst x) \.))))
                              (map (partial map (comp second first)))))))
         (map (fn [{:keys [arrangements sequence] :as m}]
                (assoc m :arrangements-count (count (filter (partial = sequence) arrangements)))))
         #_(map (juxt :record :arrangements-count)) ; debug
         (reduce (fn [acc {:keys [arrangements-count]}] (+ acc arrangements-count)) 0))))
