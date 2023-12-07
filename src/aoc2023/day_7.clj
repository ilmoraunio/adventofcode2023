(ns aoc2023.day-7
  (:require [aoc2023.util :as util]
            [clojure.string :as str]))

(def labels->strengths
  {\2 2
   \3 3
   \4 4
   \5 5
   \6 6
   \7 7
   \8 8
   \9 9
   \T 10
   \J 11
   \Q 12
   \K 13
   \A 14})

(defn amend-type
  [{:keys [hand] :as game}]
  (let [card-types {{1 5} :high-card
                    {2 1, 1 3} :one-pair
                    {2 2, 1 1} :two-pairs
                    {3 1, 1 2} :three-of-a-kind
                    {2 1, 3 1} :full-house
                    {4 1, 1 1} :four-of-a-kind
                    {5 1} :five-of-a-kind}]
    (assoc game :type (card-types (frequencies (vals (frequencies hand)))))))

(defn amend-ranks
  [games]
  (->> games
       (sort-by (juxt #(condp = (:type %)
                         :high-card 0
                         :one-pair 1
                         :two-pairs 2
                         :three-of-a-kind 3
                         :full-house 4
                         :four-of-a-kind 5
                         :five-of-a-kind 6)
                      :hand))
       (map-indexed (fn [i game] (assoc game :rank (inc i))))))

(defn amend-total-winnings
  [{:keys [bid rank] :as game}]
  (assoc game :winnings (* bid rank)))

(defn calculate-total-winnings
  [filename]
  (->> (util/gulp filename)
       (map (comp #(-> {:hand-raw (first %)
                        :hand (mapv labels->strengths (first %))
                        :bid (Integer. (second %))})
                  #(str/split % #"\h")))
       (map amend-type)
       (amend-ranks)
       (map amend-total-winnings)
       (reduce (fn [total-winnings {:keys [winnings]}] (+ total-winnings winnings)) 0)))
