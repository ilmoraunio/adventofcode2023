(ns aoc2023.day-11-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-11 :refer :all]))

(deftest initial-input
  (= 374 (sum-of-galaxy-distances "resources/11.example.txt")))
