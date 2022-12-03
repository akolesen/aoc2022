(ns ad2022.02
  (:require [clojure.set :as set]
            [clojure.java.io :as io]))

(def data
  (->> (io/reader (io/resource "03"))
       (line-seq)))

(defn sum-prio
  [coll]
  (let [item-prio (fn [c]
                    (if (>= c (int \a))
                      (+ (- c (int \a)) 1)
                      (+ 27 (- c (int \A)))))]
    (->> coll
         (map int)
         (map item-prio)
         (reduce +))))

(defn x-sum-prio
  [coll]
  (->> coll
       (map (partial map set))
       (mapcat (partial apply set/intersection))
       (sum-prio)))

(defn split-half
  [s]
  (-> s
      (count)
      (/ 2)
      (split-at s)))

(defn part-one
  [data]
  (->> data
       (map split-half)
       (x-sum-prio)))

(defn part-two
  [data]
  (->> data
       (partition 3)
       (x-sum-prio)))

(part-one data)                         ; 8039
(part-two data)                         ; 2510
