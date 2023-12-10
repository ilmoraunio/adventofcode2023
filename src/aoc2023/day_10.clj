(ns aoc2023.day-10
  (:require [aoc2023.util :as util]))

(def char->type
  {\. :ground
   \| :pipe
   \- :pipe
   \L :pipe
   \J :pipe
   \7 :pipe
   \F :pipe
   \S :start})

(def char->directions
  {\. nil
   \| #{:north :south}
   \- #{:west :east}
   \L #{:north :east}
   \J #{:north :west}
   \7 #{:west :south}
   \F #{:south :east}
   \S #{:north :east :south :west}})

(defn get-connected-tiles
  [[x y] direction]
  (case direction
    :north [x (- y 1)]
    :east [(+ x 1) y]
    :south [x (+ y 1)]
    :west [(- x 1) y]))

(defn amend-connected-cells
  ([board]
   (->> board
        (map (partial amend-connected-cells board))
        (into {})))
  ([board [coord {:keys [type directions] :as tile}]]
   (letfn [(source->target-cell [connected-cells]
             (->> connected-cells
                  (mapcat (fn [source-cell]
                            (for [target-cell connected-cells
                                  :when (not= source-cell target-cell)]
                              [source-cell target-cell])))
                  (into {})))]
     [coord (cond-> tile
              (#{:start :pipe} type) (assoc :connected-cells (->> directions
                                                                  (map (partial get-connected-tiles coord))
                                                                  (filter (fn [cell]
                                                                            (#{:start :pipe}
                                                                             (:type (board cell)))))
                                                                  (source->target-cell))))])))

(defn traverse-pipes
  [board]
  (let [start-cell (first (keep (fn [[_coord cell]] (when (= :start (:type cell)) cell)) board))]
    (loop [previous-cell nil
           current-cell start-cell
           acc {:length 1 :traversal-sequence [start-cell]}]
      (let [next-cell (->> (:connected-cells current-cell)
                           (remove (fn [[_source-coord target-coord]]
                                     (= (:coord previous-cell) target-coord)))
                           first
                           second
                           (board))]
        (if (= :start (:type next-cell))
          (-> acc
              (update :traversal-sequence conj next-cell)
              (update :length inc))
          (recur current-cell next-cell (-> acc
                                            (update :traversal-sequence conj next-cell)
                                            (update :length inc))))))))

(defn amend-steps
  [{:keys [length traversal-sequence]}]
  (reduce (fn [traversal-sequence i]
            (let [inverse-i (dec (- length i))]
              (-> traversal-sequence
                  (update-in [i :steps] (fnil conj #{}) i)
                  (update-in [inverse-i :steps] (fnil conj #{}) i))))
          traversal-sequence
          (range length)))

(defn get-farthest-steps
  [traversal-sequence]
  (-> (filter (fn [cell]
                (= 1 (count (:steps cell))))
              traversal-sequence)
      first
      :steps
      first))

(defn calc-steps-from-starting-point-to-farthest-point
  [filename]
  (let [board (->> (util/gulp filename)
                   (map-indexed
                    (fn [y row]
                      (map-indexed
                       (fn [x char]
                         {:type (char->type char)
                          :directions (char->directions char)
                          :coord [x y]})
                       row)))
                   (mapcat identity)
                   (group-by :coord)
                   (map (fn [[k [m]]] [k m]))
                   (into {})
                   (amend-connected-cells))]
    (-> (traverse-pipes board)
        (amend-steps)
        (get-farthest-steps))))
