(ns aoc2023.day-6-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-6 :refer :all]))

(deftest initial-input
  (= 288 (determine-winning-scenarios "resources/6.example.txt")))
