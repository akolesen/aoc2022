(ns ad2022.02
  (:require [clojure.java.io :as io]))

(def data
  (->> (io/reader (io/resource "02"))
       (line-seq)))

(def m {"B X" 1
        "C Y" 2
        "A Z" 3
        "A X" 4
        "B Y" 5
        "C Z" 6
        "C X" 7
        "A Y" 8
        "B Z" 9})

(def mm {"B X" 1
         "C X" 2
         "A X" 3
         "A Y" 4
         "B Y" 5
         "C Y" 6
         "C Z" 7
         "A Z" 8
         "B Z" 9})

(->> data (map m) (reduce +))  ; 13446
(->> data (map mm) (reduce +)) ; 13509
