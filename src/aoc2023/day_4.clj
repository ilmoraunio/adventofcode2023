(ns aoc2023.day-4
  (:require [aoc2023.util :as util]
            [clojure.string :as str]))

(defn card
  [s]
  (let [[winning-numbers numbers] (->> (-> s
                                           (clojure.string/replace #"Card\h+\d+:\h+" "")
                                           (clojure.string/split #"\h+\|\h+"))
                                       (map #(clojure.string/split % #"\h+"))
                                       (map (partial map #(Integer. %))))]
    {:winning-numbers (apply hash-set winning-numbers)
     :numbers (apply hash-set numbers)}))

(defn cards
  [ss]
  (map card ss))

(defn single-card-points
  [{:keys [winning-numbers numbers]}]
  (let [matches (count (clojure.set/intersection winning-numbers numbers))]
    (int (cond
           (= matches 0) 0
           (= matches 1) 1
           (> matches 1) (Math/pow 2 (dec matches))))))

(defn points
  [cards]
  (apply + (map single-card-points cards)))

(defn total-points
  [filename]
  (let [input (util/gulp filename)
        cards (cards input)]
    (points cards)))
