(ns aoc2023.day-4-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-4 :refer :all]))

(deftest initial-input
  (is (= 13 (total-points "resources/4.example.txt"))))
