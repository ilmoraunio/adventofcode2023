(ns aoc2023.day-9
  (:require [aoc2023.util :as util]
            [clojure.string :as str]))

(defn gen-sub-sequence
  [history]
  (loop [history history
         acc []]
    (if (= 1 (count history))
      acc
      (recur (rest history)
             (conj acc (- (second history)
                          (first history)))))))

(defn form-sequences
  [history]
  (let [[coll [zeros & _]] (partition-by (partial every? zero?)
                                         (iterate gen-sub-sequence history))
        sequences (conj (into '() coll) zeros)]
    sequences))

(defn fill-last-spots
  [sequences]
  (reduce (fn [acc coll]
            (if (seq acc)
              (conj acc (conj coll (+ (last coll)
                                      (-> acc first last))))
              (conj acc (conj coll 0))))
          '()
          sequences))

(defn get-next-value
  [sequences]
  (-> sequences first last))

(defn sum-of-next-values
  [filename]
  (let [histories (->> (util/gulp filename)
                       (mapv (comp
                              (partial mapv #(Integer. %))
                              #(str/split % #"\h+"))))]
    (apply + (map (comp get-next-value fill-last-spots form-sequences) histories))))
