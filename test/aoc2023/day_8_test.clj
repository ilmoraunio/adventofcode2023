(ns aoc2023.day-8-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-8 :refer :all]))

(deftest initial-input
  (= 2 (count-steps "resources/8.example.1.txt"))
  (= 6 (count-steps "resources/8.example.2.txt")))
