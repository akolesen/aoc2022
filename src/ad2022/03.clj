(ns ad2022.03
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

(->> data (map split-half) (x-sum-prio))                         ; 8039
(->> data (partition 3) (x-sum-prio))                            ; 2510
