(ns aoc2023.day-2-test
  (:require [clojure.test :refer :all]
            [aoc2023.day-2 :refer :all]))

(deftest initial-input
  (= 8 (possible-games "resources/2.example.txt")))
