(ns aoc2023.day-5
  (:require [clojure.string :as str]))

(defn parse-map
  [s name]
  (->>
   (-> (re-seq (re-pattern (str name " map:\\n(\\d+ \\d+ \\d+\\n)+")) s)
       ffirst
       (str/replace (re-pattern (str name " map:\n")) "")
       (str/split #"\n"))
   (mapv #(str/split % #" "))
   (mapv (fn [[destination source range]]
           {:destination (BigInteger. destination)
            :source (BigInteger. source)
            :range (BigInteger. range)}))))

(defn source->destination
  [s name]
  (let [index (into {} (map (fn [{:keys [destination source range]}]
                              [[source (+ source (dec range))]
                               (fn [n]
                                 (+ destination (- n source)))])
                            (parse-map s name)))]
    (fn [n]
      (if-let [match-fn (->> index
                             (filter (fn [[[source source-max] _]] (<= source n source-max)))
                             first
                             second)]
        (match-fn n)
        n))))

(defn seed->location
  [s]
  (let [seeds (map #(BigInteger. %)
                   (-> (re-seq #"seeds: (.*)" s)
                       first
                       second
                       (str/split #" ")))
        seed->soil (source->destination s "seed-to-soil")
        soil->fertilizer (source->destination s "soil-to-fertilizer")
        fertilizer->water (source->destination s "fertilizer-to-water")
        water->light (source->destination s "water-to-light")
        light->temperature (source->destination s "light-to-temperature")
        temperature->humidity (source->destination s "temperature-to-humidity")
        humidity->location (source->destination s "humidity-to-location")]
    (zipmap
     seeds
     (map #(-> %
               seed->soil
               soil->fertilizer
               fertilizer->water
               water->light
               light->temperature
               temperature->humidity
               humidity->location)
          seeds))))

(defn find-lowest-location
  [filename]
  (let [s (slurp filename)]
    (apply min (vals (seed->location s)))))
