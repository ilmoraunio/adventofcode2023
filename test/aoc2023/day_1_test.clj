(ns aoc2023.day-1-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-1 :refer :all]))

(deftest initial-input
  (= 142 (sum-calibration-values "resources/1.example.txt")))
