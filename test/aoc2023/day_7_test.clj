(ns aoc2023.day-7-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-7 :refer :all]))

(deftest initial-input
  (= 6440 (calculate-total-winnings "resources/7.example.txt")))
