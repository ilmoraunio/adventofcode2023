(ns aoc2023.day-2
  (:require [aoc2023.util :as util]))

(defn possible-games
  [filename]
  (letfn [(parse [line]
            (let [id (Integer/valueOf (second (re-find #"Game (\d+)" line)))
                  reds (map (comp #(Integer. %) second) (re-seq #"(\d+) red" line))
                  blues (map (comp #(Integer. %) second) (re-seq #"(\d+) blue" line))
                  greens (map (comp #(Integer. %) second) (re-seq #"(\d+) green" line))]
              {:id id
               :red (apply max reds)
               :blue (apply max blues)
               :green (apply max greens)}))]
    (let [lines (util/gulp filename)
          reveals (mapv parse lines)
          needle-fn (fn [{:keys [red blue green]}]
                      (and (<= red 12)
                           (<= blue 14)
                           (<= green 13)))]
      (let [{possible-games true} (group-by needle-fn reveals)]
        (reduce (fn [acc {:keys [id]}]
                  (+ acc id))
                0
                possible-games)))))
