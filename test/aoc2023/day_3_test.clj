(ns aoc2023.day-3-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-3 :refer :all]))

(deftest initial-input
  (= 4361 (sum-all-part-numbers "resources/3.example.txt")))
