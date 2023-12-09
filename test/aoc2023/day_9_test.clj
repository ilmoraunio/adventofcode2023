(ns aoc2023.day-9-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-9 :refer :all]))

(deftest initial-input
  (= 114 (sum-of-next-values "resources/9.example.txt")))
