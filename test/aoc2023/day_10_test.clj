(ns aoc2023.day-10-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-10 :refer :all]))

(deftest initial-input
  (is (= 4 (calc-steps-from-starting-point-to-farthest-point "resources/10.example.1.txt")))
  (is (= 8 (calc-steps-from-starting-point-to-farthest-point "resources/10.example.2.txt"))))
