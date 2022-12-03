(ns ad2022.01
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def data (slurp (io/resource "01")))

(defn sum-calories-group
  [s]
  (->> (string/split s #"\n")
       (map read-string)
       (reduce +)))

(defn sum-calories
  [s]
  (->> (string/split s #"\n\n")
       (map sum-calories-group)))

(defn part-one
  [coll]
  (apply max coll))

(defn part-two
  [coll]
  (->> coll
       (sort >)
       (take 3)
       (reduce +)))

(part-one (sum-calories data))                                  ; 66186
(part-two (sum-calories data))                                  ; 196804
