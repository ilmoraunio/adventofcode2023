(ns aoc2023.day-6
  (:require [clojure.string :as str]))

(defn times-and-distances
  [filename]
  (->> (->> (slurp filename)
            (re-seq #"Time:(.*)\nDistance:(.*)")
            first
            ((juxt second #(nth % 2)))
            (map str/trim))
       (map #(str/split % #"\h+"))
       (map (partial map #(Integer. %)))
       (apply zipmap)
       (map #(-> {:time (first %) :distance (second %)}))))

(defn determine-winning-scenarios
  [filename]
  (->> (times-and-distances filename)
       (map (fn [{:keys [time distance]}]
              (keep (fn [time-held-ms]
                      (let [speed (* time-held-ms 1)
                            time-left-ms (- time time-held-ms)
                            distance-travelled-mm (* speed time-left-ms)]
                        (when (> distance-travelled-mm distance)
                          {:time-held-ms time-held-ms
                           :speed speed
                           :distance distance-travelled-mm})))
                    (range time))))
       (map count)
       (apply *)))
