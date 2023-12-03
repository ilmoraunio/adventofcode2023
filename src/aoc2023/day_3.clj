(ns aoc2023.day-3
  (:require [aoc2023.util :as util]
            [clojure.set]))

(def nums #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9})
(def ignore-char #{\.})
(def non-symbols (clojure.set/union nums ignore-char))

(defn grid
  [ss]
  (letfn [(row [y s]
            (into [] (map-indexed
                      (fn [x char] {:value char
                                    :x x
                                    :y y})
                      s)))]
    (into [] (map-indexed
              (fn [y itm]
                (row y itm))
              ss))))

(defn get-radius-points
  [x y]
  [[(- x 1) (- y 1)]
   [x       (- y 1)]
   [(+ x 1) (- y 1)]
   [(- x 1) y]
   [(+ x 1) y]
   [(- x 1) (+ y 1)]
   [x       (+ y 1)]
   [(+ x 1) (+ y 1)]])

(defn update-point
  [m [x y] f]
  (if (get-in m [y x])
    (update-in m [y x] f)
    m))

(defn amend-adjacency
  [grid]
  (letfn [(radius-points [grid]
            (->> grid
                 (mapcat (partial remove (comp non-symbols :value)))
                 (mapcat (fn [{:keys [x y]}]
                           (get-radius-points x y)))))]
    (loop [grid grid
           radius-points (radius-points grid)]
      (if-let [point (first radius-points)]
        (recur (update-point grid point #(assoc % :part-number? true))
               (rest radius-points))
        grid))))

(def number-cand-empty [])

(defn tokenize
  [grid]
  (letfn [(tokenize-number [nums]
            (update (reduce (fn [m {:keys [value part-number?]}]
                              (cond-> (update m :value str value)
                                part-number? (assoc :part-number? true)))
                            {:value ""}
                            nums)
                    :value
                    #(Integer/valueOf %)))]
    (loop [grid grid
           row (first grid)
           number-cand number-cand-empty
           tokens []]
      (cond
        (seq row) (let [{:keys [value] :as char} (first row)]
                    (cond
                      (nums value) (recur grid
                                          (rest row)
                                          (conj number-cand char)
                                          tokens)
                      (not-empty number-cand) (recur grid
                                                     (rest row)
                                                     number-cand-empty
                                                     (conj tokens (tokenize-number number-cand)))
                      :else (recur grid (rest row) number-cand tokens)))
        (seq grid) (let [grid (rest grid)]
                     (recur grid (first grid) number-cand tokens))
        :else tokens))))

(defn sum-part-numbers
  [tokens]
  (reduce
   (fn [acc {:keys [value part-number?]}]
     (if part-number?
       (+ acc value)
       acc))
   0
   tokens))

(defn sum-all-part-numbers
  [filename]
  (->> (util/gulp filename)
       grid
       amend-adjacency
       tokenize
       sum-part-numbers))
