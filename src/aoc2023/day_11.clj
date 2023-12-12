(ns aoc2023.day-11
  "My notes on this puzzle: there was a lot of recontructing and slicing the data
   in various ways. Probably contributes to it being so slow (1060 msecs using `(time …)`)."
  (:require [aoc2023.util :as util]))

(def char->type
  {\. :space
   \# :galaxy})

(def type->char
  {:space \.
   :galaxy \#})

(defn form-coordinate-map
  [indexed-map]
  (->> indexed-map
       (mapcat identity)
       (group-by :coord)
       (map (fn [[k [v]]] [k (select-keys v [:coord :type])]))
       (into (sorted-map-by (fn [a b]
                              (compare (into [] (reverse a))
                                       (into [] (reverse b))))))))

(defn reconstruct-coords
  [indexed-map]
  (map-indexed (fn [a row]
                 (map-indexed (fn [b space]
                                (merge space {:coord [b a]}))
                              row))
               indexed-map))

(defn insert-empty-rows
  [state]
  (->> state
       vals
       (partition-by (fn [{:keys [coord]}] (second coord)))
       (reduce (fn [acc row]
                 (if (every? #(-> % :type (= :space)) row)
                   (conj acc row row)
                   (conj acc row)))
               [])
       (reconstruct-coords)
       (form-coordinate-map)))

(defn insert-empty-columns [state]
  (->> state
       vals
       (sort-by :coord)
       (partition-by (fn [{:keys [coord]}] (first coord)))
       (reduce (fn [acc column]
                 (if (every? #(-> % :type (= :space)) column)
                   (conj acc column column)
                   (conj acc column)))
               [])
       (mapcat identity)
       (sort-by :coord (fn [a b]
                         (compare (into [] (reverse a))
                                  (into [] (reverse b)))))
       (partition-by (fn [{:keys [coord]}] (second coord)))
       (reconstruct-coords)
       (form-coordinate-map)))

(defn draw
  [state]
  (->> state
       vals
       (partition-by (fn [{:keys [coord]}] (second coord)))
       (mapcat (fn [row]
                 (into (mapv (fn [tile]
                               (type->char (:type tile)))
                             row)
                       [(System/lineSeparator)])))
       (apply str)
       (println)))

(defn enlarge-space
  [state]
  (-> state
      insert-empty-rows
      insert-empty-columns))

(defn sum-of-galaxy-distances
  [filename]
  (let [state (->> (map-indexed (fn [y row]
                                  (map-indexed (fn [x char]
                                                 {:type (char->type char)
                                                  :coord [x y]})
                                               row))
                                (util/gulp filename))
                   (form-coordinate-map)
                   (enlarge-space))
        galaxies (->> state
                      (filter (fn [[_ space]] (-> space :type (= :galaxy))))
                      (map first))
        lengths (->> (for [a-galaxy galaxies
                           another-galaxy galaxies]
                       (when (not= a-galaxy another-galaxy)
                         #{a-galaxy another-galaxy}))
                     (remove nil?)
                     (into #{})
                     (map (fn [x]
                            (->> (map vector (first x) (second x))
                                 (map (fn [[a b]] (Math/abs (- a b))))
                                 (apply +)))))]
    (draw state)
    (apply + lengths)))
