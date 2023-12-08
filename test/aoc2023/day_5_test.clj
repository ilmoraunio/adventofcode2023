(ns aoc2023.day-5-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-5 :refer :all]))

(deftest initial-input
  (= 35 (find-lowest-location "resources/5.example.txt")))
