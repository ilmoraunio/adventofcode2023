(ns aoc2023.day-8
  (:require [aoc2023.util :as util]))

(defn count-steps
  [filename]
  (let [[instructions-raw _ & network-raw] (util/gulp filename)
        instructions (cycle (map {\L :l \R :r} instructions-raw))
        network (->> network-raw
                     (map (partial re-seq #"(.{3}) = \((.{3}), (.{3})\)"))
                     (map (fn [[[_ node left right]]]
                            {(keyword node) {:l (keyword left) :r (keyword right)}}))
                     (apply merge))]
    (loop [current-node :AAA
           instructions instructions
           steps 0]
      (if (= current-node :ZZZ)
        steps
        (let [next-instruction (first instructions)
              next-node (get-in network [current-node next-instruction])]
          (recur next-node (rest instructions) (inc steps)))))))
