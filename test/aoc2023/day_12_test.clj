(ns aoc2023.day-12-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-12 :refer :all]))

(deftest initial-input
  (= 21 (calc-sum-of-arrangements "resources/12.example.txt")))
